package org.dhimate.mule.environment;

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

@Component
@Data
@Slf4j
@DependsOn(value = { "AnypointConnectionFactory", "AnypointOrganizationService" })
@ConfigurationProperties
@EnableConfigurationProperties
public class AnypointEnvironmentService {

	@Autowired
	AnypointConnectionFactory acf;

	@Autowired
	AnypointEnvironmentRepository repository;
	
	@Value("${api.baseuri}")
	public String apiBaseUri;

	@PostConstruct
	void init() {
		List<AnypointEnvironment> anypointEnvironment = fetchAnypointEnvironment();

		for (AnypointEnvironment ape : anypointEnvironment) {
			AnypointEnvironmentEntity apeDB = new AnypointEnvironmentEntity();
			apeDB.setEnvironmentId(ape.getEnvironmentId());
			apeDB.setName(ape.getName());
			apeDB.setProduction(ape.isProduction());
			apeDB.setType(ape.getType());
			repository.save(apeDB);
		}
		log.info("Initialised environment");
	}

	public List<AnypointEnvironment> fetchAnypointEnvironment() {

		log.info("Getting environment details from Anypoint Platform");
		WebClient client = WebClient.builder().baseUrl(apiBaseUri)
				.defaultHeader("Authorization", "Bearer " + acf.getConnection().getAccessToken()).build();

		Mono<AnypointEnvironmentWrapper> mono = client.get()
				.uri("/accounts/api/organizations/" + acf.getConnection().getOrganizationId() + "/environments")
				.retrieve().bodyToMono(AnypointEnvironmentWrapper.class);

		AnypointEnvironmentWrapper aew = mono.block();

		log.info("Retrieved environment details from Anypoint Platform");
		return aew.getAnypointEnvironment();

	}
}
