package org.dhimate.mule.apimanager;

import java.util.List;

import javax.annotation.PostConstruct;

import org.dhimate.mule.environment.AnypointEnvironmentRepository;
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

@Component("AnypointAPIManagerService")
@DependsOn(value = { "AnypointConnectionFactory", "AnypointOrganizationService", "AnypointEnvironmentService" })
@Data
@Slf4j
@ConfigurationProperties
@EnableConfigurationProperties
public class AnypointAPIManagerService {

	@Autowired
	AnypointConnectionFactory acf;

	@Autowired
	AnypointEnvironmentRepository environmentRepository;

	@Autowired
	AnypointAPIManagerRepository apimanagerrepository;

	@Value("${api.baseuri}")
	public String apiBaseUri;

	@PostConstruct
	void init() {

		fetchAnypointAPIManager("Development", "2c7ad64e-f216-44ad-bd17-0ca5a005c8c8");
		log.info("Initialized API manager");
	}

	public List<AnypointAPIManagerEntity> fetchAnypointAPIManager(String environmentName, String environmentId) {
		log.info("Getting api manager " + environmentName + "details from Anypoint Platform");

		WebClient client = WebClient.builder().baseUrl(apiBaseUri)
				.defaultHeader("Authorization", "Bearer " + acf.getConnection().getAccessToken()).build();

		Mono<AnypointAPIManagerWrapper> mono = client.get().uri("/apimanager/api/v1/organizations/"
				+ acf.getConnection().getOrganizationId() + "/environments/" + environmentId + "/apis?limit=1").retrieve()
				.bodyToMono(AnypointAPIManagerWrapper.class);

		AnypointAPIManagerWrapper aamw = mono.block();

		log.info(aamw.toString());

		log.info("Retrieved api manager " + environmentName + " app details from Anypoint Platform");

		return null;
	}
}
