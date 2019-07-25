package org.dhimate.mule.user;

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
public class AnypointUserService {
	@Autowired
	AnypointConnectionFactory acf;

	@Autowired
	AnypointUserRepository repository;

	@Value("${api.baseuri}")
	public String apiBaseUri;

	@PostConstruct
	void init() {
		List<AnypointUser> anypointUser = fetchAnypointUser();

		for (AnypointUser apu : anypointUser) {
			AnypointUserEntity apuDB = new AnypointUserEntity();
			apuDB.setFirstName(apu.getFirstName());
			apuDB.setLastName(apu.getLastName());
			apuDB.setUserName(apu.getUserName());
			apuDB.setLastLogin(apu.getLastLogin());
			apuDB.setEnabled(apu.isEnabled());
			repository.save(apuDB);
		}
		log.info("Initialised user");
	}

	public List<AnypointUser> fetchAnypointUser() {

		log.info("Getting user details from Anypoint Platform");
		WebClient client = WebClient.builder().baseUrl(apiBaseUri)
				.defaultHeader("Authorization", "Bearer " + acf.getConnection().getAccessToken()).build();

		Mono<AnypointUserWrapper> mono = client.get()
				.uri("/accounts/api/organizations/" + acf.getConnection().getOrganizationId() + "/members").retrieve()
				.bodyToMono(AnypointUserWrapper.class);

		AnypointUserWrapper apw = mono.block();

		log.info("Retrieved user details from Anypoint Platform");
		return apw.getAnypointUser();

	}

	
}
