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

@Component("AnypointCoreServicesSubscriptionService")
@DependsOn(value = { "AnypointConnectionFactory", "AnypointOrganizationService" })
@Data
@Slf4j
@ConfigurationProperties
@EnableConfigurationProperties
public class AnypointCoreServicesSubscriptionService {

	@Autowired
	AnypointConnectionFactory acf;

	@Autowired
	AnypointCoreServicesSubscriptionRepository repository;

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
		log.info("Initializing core services");

		repository.save(fetchAnypointCoreServicesEntitlements());

		log.info("Initialized core services");
	}

	public AnypointCoreServicesSubscriptionEntity fetchAnypointCoreServicesEntitlements() {

		log.debug("Getting anypoint coreservices details from Anypoint Platform");

		WebClient client = WebClient.builder().baseUrl(apiBaseUri)
				.defaultHeader("Authorization", "Bearer " + acf.getConnection().getAccessToken()).build();

		Mono<AnypointCoreServicesSubscription> mono = client.get().uri("/accounts/api/cs").retrieve()
				.bodyToMono(AnypointCoreServicesSubscription.class);

		AnypointCoreServicesSubscription acs = mono.block();

		log.debug("Retrieved anypoint core services details from Anypoint Platform");

//		log.info(acs.toString());

		AnypointCoreServicesSubscriptionEntity entity = new AnypointCoreServicesSubscriptionEntity();

		entity.setVCoresProductionAssigned(acs.getVCoresProductionAssigned());
		entity.setVCoresProductionReassigned(acs.getVCoresProductionReassigned());

		entity.setVCoresSandboxAssigned(acs.getVCoresSandboxAssigned());
		entity.setVCoresSandboxReassigned(acs.getVCoresSandboxReassigned());

		entity.setVCoresDesignAssigned(acs.getVCoresDesignAssigned());
		entity.setVCoresDesignReassigned(acs.getVCoresDesignReassigned());

		entity.setStaticIpsAssigned(acs.getStaticIpsAssigned());
		entity.setStaticIpsReassigned(acs.getStaticIpsReassigned());

		entity.setVpcsAssigned(acs.getVpcsAssigned());
		entity.setVpcsReassigned(acs.getVpcsReassigned());

		entity.setVpnsAssigned(acs.getVpnsAssigned());
		entity.setVpnsReassigned(acs.getVpnsReassigned());

		entity.setLoadBalancersAssigned(acs.getLoadBalancersAssigned());
		entity.setLoadBalancersReassigned(acs.getLoadBalancersReassigned());

		return entity;

	}

	public LocalDateTime formatDate(String input) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		LocalDateTime date = LocalDateTime.parse(input, dtf);

		return date;
	}
}
