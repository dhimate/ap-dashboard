package org.dhimate.mule.designcenter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.dhimate.mule.session.AnypointConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component("AnypointDesignCenterService")
@DependsOn(value = { "AnypointConnectionFactory", "AnypointOrganizationService" })
@Data
@Slf4j
@ConfigurationProperties
@EnableConfigurationProperties
public class AnypointDesignCenterService {

	@Autowired
	AnypointConnectionFactory acf;

	@Autowired
	AnypointDesignCenterRepository repository;

	@Value("${api.baseuri}")
	public String apiBaseUri;

	@PostConstruct
	void init() {

		List<AnypointDesignCenterEntity> anypointDesignCenterEntity = fetchAnypointDesignCenter();
		repository.saveAll(anypointDesignCenterEntity);

		log.info("Initialized design center");
	}

	public List<AnypointDesignCenterEntity> fetchAnypointDesignCenter() {

		log.info("Getting design center details from Anypoint Platform");

		WebClient client = WebClient.builder().baseUrl(apiBaseUri)
				.defaultHeader("Authorization", "Bearer " + acf.getConnection().getAccessToken()).build();

		Mono<List<AnypointDesignCenter>> mono = client.get().uri("/designcenter/api-designer/projects").header("x-organization-id", acf.getConnection().getOrganizationId()).retrieve()
				.bodyToFlux(AnypointDesignCenter.class).collectList();

		List<AnypointDesignCenter> adc = (List<AnypointDesignCenter>) mono.block();

		log.info("Retrieved design center details from Anypoint Platform");

		List<AnypointDesignCenterEntity> dc = new ArrayList<AnypointDesignCenterEntity>();

		for (AnypointDesignCenter item : adc) {
			AnypointDesignCenterEntity temp = new AnypointDesignCenterEntity();
			temp.setName(item.getName());
			temp.setType(item.getType());
			temp.setCreatedDate(
					LocalDateTime.ofInstant(Instant.ofEpochMilli(item.getCreatedDate()), ZoneId.systemDefault()));
			temp.setModifiedDate(
					LocalDateTime.ofInstant(Instant.ofEpochMilli(item.getLastUpdateDate()), ZoneId.systemDefault()));
			dc.add(temp);

		}

		return dc;

	}

}
