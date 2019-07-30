package org.dhimate.mule.clientapplication;

import java.util.List;

import javax.annotation.PostConstruct;

//import org.dhimate.mule.apimanager.AnypointAPIManagerRepository;
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

/**
 * AnypointClientApplicationService
 */
@Component("AnypointClientApplicationService")
@DependsOn(value = { "AnypointConnectionFactory", "AnypointOrganizationService", "AnypointEnvironmentService" })
@Data
@Slf4j
@ConfigurationProperties
@EnableConfigurationProperties
public class AnypointClientApplicationService {

	@Autowired
	AnypointConnectionFactory acf;

	@Autowired
	AnypointEnvironmentRepository environmentRepository;

	@Autowired
	AnypointClientApplicationRepository clientapplicationrepository;

	@Value("${api.baseuri}")
	public String apiBaseUri;

	@PostConstruct
	void init() {
		log.info("Initializing api client application");

		environmentRepository.findAll().get(0);
		String environmentId = environmentRepository.findAll().get(0).getEnvironmentId();
		String environmentName = environmentRepository.findAll().get(0).getName();
		
		AnypointClientApplicationWrapper acaw = fetchAnypointClientApplication(environmentName, environmentId);
		List<AnypointClientApplication> aca = acaw.getAnypointClientApplication();
		for (AnypointClientApplication tempAnypointClientApplication : aca) {
			AnypointClientApplicationEntity temp = new AnypointClientApplicationEntity();
			temp.setOrganizationId(acf.getConnection().getOrganizationId());
			temp.setClientApplicationId(tempAnypointClientApplication.getClientApplicationId());
			temp.setClientApplicationName(tempAnypointClientApplication.getClientApplicationName());
			clientapplicationrepository.save(temp);
        }

        log.info("Initialized api client application");
	}

	public AnypointClientApplicationWrapper fetchAnypointClientApplication(String environmentName,
			String environmentId) {
		log.debug("Getting api client application " + environmentName + "details from Anypoint Platform");

		WebClient client = WebClient.builder().baseUrl(apiBaseUri)
				.defaultHeader("Authorization", "Bearer " + acf.getConnection().getAccessToken()).build();

		Mono<AnypointClientApplicationWrapper> mono = client
				.get().uri("/analytics/1.0/" + acf.getConnection().getOrganizationId() + "/environments/"
						+ environmentId + "/metadata/legend")
				.retrieve().bodyToMono(AnypointClientApplicationWrapper.class);

		AnypointClientApplicationWrapper acaw = mono.block();

		log.debug("Retrieved api client application " + environmentName + " app details from Anypoint Platform");

		return acaw;
	}

}