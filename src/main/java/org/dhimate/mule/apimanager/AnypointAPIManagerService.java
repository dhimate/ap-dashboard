package org.dhimate.mule.apimanager;

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
		log.info("Initializing API manager");

		List<AnypointEnvironmentEntity> environmentList = environmentRepository.findAll();

		for (AnypointEnvironmentEntity e : environmentList) {

			int limit = 20;
			int offset = 0;
			int currentTotal = 0;
			int assetsTotal = 0;

			do {
				List<AnypointAPIManagerEntity> aame = new ArrayList<AnypointAPIManagerEntity>();
				AnypointAPIManagerWrapper aamw = fetchAnypointAPIManager(e.getName(), e.getEnvironmentId(), offset,
						limit);
				assetsTotal = aamw.getTotal();

				if (assetsTotal > 0) {
					currentTotal = currentTotal + aamw.getAnypointAPIManagerAssets().size();

					for (AnypointAPIManager i : aamw.getAnypointAPIManagerAssets()) {

						AnypointAPIManagerEntity temp = new AnypointAPIManagerEntity();
						temp.setOrganizationId(acf.getConnection().getOrganizationId());
						temp.setEnvironmentId(e.getEnvironmentId());
						temp.setEnvironmentName(e.getName());
						temp.setExchangeAssetName(i.getExchangeAssetName());
						temp.setApiAutoDiscoveryId(i.getApiAutoDiscoveryId());
						temp.setApiAssetId(i.getApiAssetId());
						temp.setApiCreatedDate(i.getApiCreatedDate());
						temp.setApiUpdatedDate(i.getApiUpdatedDate());
						temp.setApiLastActiveDate(i.getApiLastActiveDate());
						temp.setApiAssetVersion(i.getApiAssetVersion());
						temp.setApiProductVersion(i.getApiProductVersion());
						temp.setApiActiveContractsCount(i.getApiActiveContractsCount());
						aame.add(temp);
					}

					if (aame.size() > 0) {
						apimanagerrepository.saveAll(aame);
					}
					offset = offset + limit;
				}
			} while (currentTotal < assetsTotal);
		}

		log.info("Initialized API manager");
	}

	public AnypointAPIManagerWrapper fetchAnypointAPIManager(String environmentName, String environmentId, int offset,
			int limit) {
		log.debug("Getting api manager " + environmentName + "details from Anypoint Platform");

		WebClient client = WebClient.builder().baseUrl(apiBaseUri)
				.defaultHeader("Authorization", "Bearer " + acf.getConnection().getAccessToken()).build();

		Mono<AnypointAPIManagerWrapper> mono = client.get()
				.uri("/apimanager/api/v1/organizations/" + acf.getConnection().getOrganizationId() + "/environments/"
						+ environmentId + "/apis?offset=" + offset + "&limit=" + limit)
				.retrieve().bodyToMono(AnypointAPIManagerWrapper.class);

		AnypointAPIManagerWrapper aamw = mono.block();

		log.debug("Retrieved api manager " + environmentName + " app details from Anypoint Platform");

		return aamw;
	}
}
