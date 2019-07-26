package org.dhimate.mule.cloudhub;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.dhimate.mule.environment.AnypointEnvironmentEntity;
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

@Component("AnypointCloudhubService")
@DependsOn(value = { "AnypointConnectionFactory", "AnypointOrganizationService", "AnypointEnvironmentService" })
@Data
@Slf4j
@ConfigurationProperties
@EnableConfigurationProperties

public class AnypointCloudhubService {
	@Autowired
	AnypointConnectionFactory acf;

	@Autowired
	AnypointEnvironmentRepository environmentRepository;

	@Autowired
	AnypointCloudhubRepository cloudhubRepository;

	@Value("${api.baseuri}")
	public String apiBaseUri;

	@PostConstruct
	void init() {

		List<AnypointEnvironmentEntity> environmentList = environmentRepository.findAll();

		for (AnypointEnvironmentEntity e : environmentList) {
			List<AnypointCloudhubEntity> chel = fetchAnypointCloudhub(e.getName(), e.getEnvironmentId());
			if (chel.size() > 0) {
				cloudhubRepository.saveAll(chel);
			}
		}

		log.info("Initialized cloudhub");

	}

	public List<AnypointCloudhubEntity> fetchAnypointCloudhub(String environmentName, String environmentId) {

		log.info("Getting cloudhub " + environmentName + " app details from Anypoint Platform");

		WebClient client = WebClient.builder().baseUrl(apiBaseUri)
				.defaultHeader("Authorization", "Bearer " + acf.getConnection().getAccessToken()).build();

		Mono<List<AnypointCloudhub>> mono = client.get().uri("/cloudhub/api/v2/applications")
				.header("X-ANYPNT-ENV-ID", environmentId).retrieve().bodyToFlux(AnypointCloudhub.class).collectList();

		List<AnypointCloudhub> apc = (List<AnypointCloudhub>) mono.block();

		log.info("Retrieved cloudhub " + environmentName + " app details from Anypoint Platform");

		List<AnypointCloudhubEntity> chel = new ArrayList<AnypointCloudhubEntity>();

		for (AnypointCloudhub i : apc) {

			AnypointCloudhubEntity tempCHEntity = new AnypointCloudhubEntity();
			
			tempCHEntity.setOrganizationId(acf.getConnection().getOrganizationId());
			tempCHEntity.setEnvironmentId(environmentId);
			tempCHEntity.setEnvironmentName(environmentName);
			tempCHEntity.setName(i.getName());
			tempCHEntity.setStatus(i.getStatus());
			tempCHEntity.setWorkerType(i.getWorkerType());
			tempCHEntity.setWorkersWeight(i.getWorkersWeight());
			tempCHEntity.setNumWorkers(i.getNumWorkers());
			tempCHEntity.setTotalWorkersWeight(i.getTotalWorkersWeight());
			tempCHEntity.setRuntimeVersion(i.getRuntimeVersion());
			
			chel.add(tempCHEntity);
		}

		return chel;

	}

}
