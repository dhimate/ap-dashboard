package org.dhimate.mule.session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Component("LoginRequest")
@EnableConfigurationProperties
@ConfigurationProperties
public class LoginRequest {
	@Value("${api.user.name}")
	private String username;

	@Value("${api.user.password}")
	private String password;
}
