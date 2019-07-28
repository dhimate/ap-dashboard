package org.dhimate.mule.organization;

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

@Component("AnypointOrganizationService")
@DependsOn("AnypointConnectionFactory")
@Data
@Slf4j
@ConfigurationProperties
@EnableConfigurationProperties
public class AnypointOrganizationService {

	@Autowired
	AnypointConnectionFactory acf;

	@Autowired
	AnypointOrganizationRepository repository;

	@Value("${api.baseuri}")
	public String apiBaseUri;

	@PostConstruct
	void init() {
		log.info("Initializing organization");

		AnypointOrganizationEntity anypointOrganizationEntity = fetchAnypointOrganization();
		repository.save(anypointOrganizationEntity);
		log.info(anypointOrganizationEntity.toString());
		acf.getConnection().setOrganizationId(anypointOrganizationEntity.getOrganizationId());
		
		log.info("Initialized organization");
	}

	public AnypointOrganizationEntity fetchAnypointOrganization() {

		log.debug("Getting organization details from Anypoint Platform");

		WebClient client = WebClient.builder().baseUrl(apiBaseUri)
				.defaultHeader("Authorization", "Bearer " + acf.getConnection().getAccessToken()).build();

		Mono<AnypointOrganization> mono = client.get().uri("/accounts/api/profile").retrieve()
				.bodyToMono(AnypointOrganization.class);
		AnypointOrganization apo = mono.block();

		log.debug("Retrieved organization details from Anypoint Platform");

		return new AnypointOrganizationEntity(apo.getOrganizationId(), apo.getOrganizationName(),
				apo.getTotalSubOrganizations());
	}
}
