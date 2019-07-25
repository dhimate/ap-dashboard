package org.dhimate.mule.session;

import javax.annotation.PostConstruct;

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

@Data
@Slf4j
@DependsOn("LoginRequest")
@Component("AnypointConnection")
@ConfigurationProperties
@EnableConfigurationProperties
public class AnypointConnection {
	private String accessToken;
	private String organizationId;
	private final int validity = 300;
	private long validFrom = System.currentTimeMillis() / 1000;

	@Autowired
	@Qualifier("LoginRequest")
	private LoginRequest loginRequest;
	
	@Value("${api.baseuri}")
	public String apiBaseUri;

	@PostConstruct
	void init() {
		LoginResponse response = connectToAnypoint();
		this.accessToken = response.access_token;
		log.info("Initialized session");
	}

	public LoginResponse connectToAnypoint() {
		log.info("Logging in to Anypoint Platform");
		
		WebClient client = WebClient.builder().baseUrl(apiBaseUri)
				.defaultHeader("Content-Type", "application/json").build();

		Mono<LoginResponse> mlr = client.post().uri("/accounts/login").body(Mono.just(loginRequest), LoginRequest.class)
				.retrieve().bodyToMono(LoginResponse.class);

		LoginResponse loginResponse = ((Mono<LoginResponse>) mlr).block();
		
		log.info("Connection successful");
		log.info(loginResponse.toString());
		return loginResponse;
	}

	public void refreshToken() {
		LoginResponse response = connectToAnypoint();
		this.setAccessToken(response.access_token);
		this.setValidFrom(System.currentTimeMillis() / 1000);
	}
	
	public void dummyFunction() {
//		RestTemplate restTemplate = new RestTemplate();
		/*
		 * URI uri;
		 * 
		 * try { uri = new URI("https://anypoint.mulesoft.com:443/accounts/login");
		 * RequestEntity<LoginRequest> requestEntity =
		 * RequestEntity.post(uri).body(login); lr =
		 * restTemplate.exchange(requestEntity, LoginResponse.class); } catch
		 * (URISyntaxException e) { e.printStackTrace(); }
		 */

	}
}
