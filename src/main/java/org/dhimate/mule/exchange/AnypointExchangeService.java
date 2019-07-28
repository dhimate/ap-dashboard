package org.dhimate.mule.exchange;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

@Component("AnypointExchangeService")
@DependsOn(value = { "AnypointConnectionFactory", "AnypointOrganizationService" })
@Data
@Slf4j
@ConfigurationProperties
@EnableConfigurationProperties
public class AnypointExchangeService {

	@Autowired
	AnypointConnectionFactory acf;

	@Autowired
	AnypointExchangeRepository repository;

	@Value("${api.baseuri}")
	public String apiBaseUri;

	@PostConstruct
	void init() {
		log.info("Initializing exchange");

		int limit = 20;
		int offset = 0;
		int size = 0;

		do {
			List<AnypointExchangeEntity> anypointExchangeEntity = fetchAnypointExchange(offset, limit);
			repository.saveAll(anypointExchangeEntity);
			size = anypointExchangeEntity.size();
			offset = offset + limit;
		} while (size == limit);
		
		log.info("Initialized exchange");
	}

	public List<AnypointExchangeEntity> fetchAnypointExchange(int offset, int limit) {

		log.debug("Getting exchange details from Anypoint Platform");

		WebClient client = WebClient.builder().baseUrl(apiBaseUri)
				.defaultHeader("Authorization", "Bearer " + acf.getConnection().getAccessToken()).build();

		Mono<List<AnypointExchange>> mono = client
				.get().uri("/exchange/api/v2/assets?organizationId=" + acf.getConnection().getOrganizationId()
						+ "&limit=" + limit + "&offset=" + offset)
				.retrieve().bodyToFlux(AnypointExchange.class).collectList();

		List<AnypointExchange> ape = (List<AnypointExchange>) mono.block();

		log.debug("Retrieved exchange details from Anypoint Platform");

		List<AnypointExchangeEntity> exchange = new ArrayList<AnypointExchangeEntity>();

		for (AnypointExchange item : ape) {
			AnypointExchangeEntity temp = new AnypointExchangeEntity();
			temp.setAssetId(item.getAssetId());
			temp.setVersion(item.getVersion());
			temp.setName(item.getName());
			temp.setType(item.getType());
			temp.setCreatedByUserId(item.getCreatedByUserId());
			temp.setCreatedByName(item.getCreatedByName());
			temp.setCreatedDate(formatDate(item.getCreatedAt()));
			temp.setModifiedDate(formatDate(item.getModifiedAt()));
			exchange.add(temp);

		}

		return exchange;

	}

	public LocalDateTime formatDate(String input) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		LocalDateTime date = LocalDateTime.parse(input, dtf);

		return date;
	}
}
