package org.dhimate.mule.cs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

@Component("AnypointCoreServicesUsageService")
@DependsOn(value = { "AnypointConnectionFactory", "AnypointOrganizationService" })
@Data
@Slf4j
@ConfigurationProperties
@EnableConfigurationProperties
public class AnypointCoreServicesUsageService {

	@Autowired
	AnypointConnectionFactory acf;
	
	@Autowired
	AnypointCoreServicesUsageRepository repository;

	@Value("${api.baseuri}")
	public String apiBaseUri;

	@PostConstruct
	void init() {
		/*
		 * do { List<AnypointExchangeEntity> anypointExchangeEntity =
		 * fetchAnypointExchange(offset, limit);
		 * repository.saveAll(anypointExchangeEntity); size =
		 * anypointExchangeEntity.size(); offset = offset + limit; log.info("offset " +
		 * offset); } while (size == limit);
		 * 
		 */
		repository.save(fetchAnypointCoreServicesUsage());
		log.info("Initialized core services usage");
	}

	public AnypointCoreServicesUsageEntity fetchAnypointCoreServicesUsage() {

		log.info("Getting anypoint coreservices details from Anypoint Platform");

		WebClient client = WebClient.builder().baseUrl(apiBaseUri)
				.defaultHeader("Authorization", "Bearer " + acf.getConnection().getAccessToken()).build();

		Mono<AnypointCoreServicesUsage> mono = client.get()
				.uri("/accounts/api/cs/organizations/" + acf.getConnection().getOrganizationId() + "/usage").retrieve()
				.bodyToMono(AnypointCoreServicesUsage.class);

		AnypointCoreServicesUsage acsu = mono.block();

		log.info("Retrieved anypoint core services usage details from Anypoint Platform");

		log.info(acsu.toString());

		AnypointCoreServicesUsageEntity entity = new AnypointCoreServicesUsageEntity();

		entity.setProductionVCoresConsumed(acsu.getProductionVCoresConsumed());
		entity.setDesignVCoresConsumed(acsu.getDesignVCoresConsumed());
		entity.setSandboxVCoresConsumed(acsu.getSandboxVCoresConsumed());
		entity.setLoadBalancersConsumed(acsu.getLoadBalancersConsumed());
		entity.setStaticIpsConsumed(acsu.getStaticIpsConsumed());
		entity.setVpcsConsumed(acsu.getVpcsConsumed());
		entity.setVpnsConsumed(acsu.getVpnsConsumed());

		return entity;

	}

	public LocalDateTime formatDate(String input) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		LocalDateTime date = LocalDateTime.parse(input, dtf);

		return date;
	}
}
