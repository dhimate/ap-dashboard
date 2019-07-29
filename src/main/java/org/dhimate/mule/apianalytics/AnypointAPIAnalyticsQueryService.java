package org.dhimate.mule.apianalytics;

import java.util.List;

import javax.annotation.PostConstruct;

import org.dhimate.mule.apimanager.AnypointAPIManagerEntity;
import org.dhimate.mule.apimanager.AnypointAPIManagerRepository;
import org.dhimate.mule.clientapplication.AnypointClientApplicationEntity;
import org.dhimate.mule.clientapplication.AnypointClientApplicationRepository;
import org.dhimate.mule.environment.AnypointEnvironmentEntity;
import org.dhimate.mule.environment.AnypointEnvironmentRepository;
import org.dhimate.mule.session.AnypointConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component("AnypointAPIAnalyticsQueryService")
@DependsOn(value = { "AnypointConnectionFactory", 
                    "AnypointOrganizationService", 
                    "AnypointEnvironmentService",
                    "AnypointClientApplicationService", 
                    "AnypointAPIManagerService" })
@Data
@Slf4j
@ConfigurationProperties
@EnableConfigurationProperties
public class AnypointAPIAnalyticsQueryService {

    @Autowired
    AnypointConnectionFactory acf;

    @Autowired
    AnypointEnvironmentRepository environmentRepository;

    @Autowired
    @Qualifier("AnypointAPIAnalyticsQueryRequest")
    AnypointAPIAnalyticsQueryRequest apiAnalyticsQueryRequest;

    @Autowired
    AnypointAPIAnalyticsAPIIdRepository apiidrepository;

    @Autowired
    AnypointAPIAnalyticsClientIdRepository clientidrepository;

    @Autowired
    AnypointClientApplicationRepository clientapplication;

    @Autowired
    AnypointAPIManagerRepository apimanager;

    @Value("${api.baseuri}")
    public String apiBaseUri;

    @PostConstruct
    void init() {
        log.info("Initializing api analytics query");
        for (AnypointEnvironmentEntity e : environmentRepository.findAll()) {

            AnypointAPIAnalyticsQueryResponse aaaq = fetchAnypointClientApplication(e.getName(), e.getEnvironmentId());

            aaaq.getApiIds().forEach((i)-> {
                AnypointAPIAnalyticsAPIIdEntity a = new AnypointAPIAnalyticsAPIIdEntity() ;
                a.setEnvironmentId(e.getEnvironmentId());
                a.setEnvironmentName(e.getName());
                a.setOrganizationId(acf.getConnection().getOrganizationId());
                a.setApiId(i.getApiId());
                a.setCount(i.getCount());
                long apiId = 0;
                if (i.getApiId()!= null) 
                    apiId = Long.parseLong(i.getApiId());
                List<AnypointAPIManagerEntity> apm =apimanager.findByApiId(apiId);
                String apiName = "";
                if (apm != null && apm.size()>0)
                    apiName = apm.get(0).getExchangeAssetName();
                a.setApiName(apiName);
                    
                apiidrepository.save(a);
            });

            aaaq.getClientIds().forEach((i)-> {
                AnypointAPIAnalyticsClientIdEntity a = new AnypointAPIAnalyticsClientIdEntity();
                a.setEnvironmentId(e.getEnvironmentId());
                a.setEnvironmentName(e.getName());
                a.setOrganizationId(acf.getConnection().getOrganizationId());
                a.setClientId(i.getClientId());
                a.setCount(i.getCount());
                List<AnypointClientApplicationEntity> ce =clientapplication.findByClientApplicationId(i.getClientId());
                String clientApplicationName = "";
                if (ce != null && ce.size()>0)
                    clientApplicationName = ce.get(0).getClientApplicationName();
                a.setClientName(clientApplicationName);
                clientidrepository.save(a);
            });
        }
        log.info("Initialized api analytics query");

    }

    public AnypointAPIAnalyticsQueryResponse fetchAnypointClientApplication(String environmentName, String environmentId) {
        log.debug("Getting api analytics query " + environmentName + "details from Anypoint Platform");

        WebClient client = WebClient
                            .builder()
                            .baseUrl(apiBaseUri)
                            .defaultHeader("Authorization", "Bearer " + acf.getConnection().getAccessToken()).build();

        /*
         * Mono<AnypointClientApplicationWrapper> mono = client
         * .get().uri("/analytics/1.0/" + acf.getConnection().getOrganizationId() +
         * "/environments/" + environmentId + "/metadata/legend")
         * .retrieve().bodyToMono(AnypointClientApplicationWrapper.class);
         */
        AnypointAPIAnalyticsQueryResponse aaaq = client
                                                .post()
                                                // .uri("/analytics/1.0/5973c647-af7d-4f34-a1b1-e6877ed5554c/environments/8d53272d-2cc3-4d0f-8c34-bbf267fa6b50/query")
                                                .uri("/analytics/1.0/"+ acf.getConnection().getOrganizationId() +"/environments/"+ environmentId +"/query")
                                                .body(Mono.just(apiAnalyticsQueryRequest), AnypointAPIAnalyticsQueryRequest.class)
                                                .retrieve()
                                                .bodyToMono(AnypointAPIAnalyticsQueryResponse.class)
                                                .block();
        // AnypointAPIAnalyticsQueryResponse acaw = mono.block();

        log.info(aaaq.toString());

        log.debug("Retrieved api client application " + environmentName + " app details from Anypoint Platform");

        return aaaq;
    }
}