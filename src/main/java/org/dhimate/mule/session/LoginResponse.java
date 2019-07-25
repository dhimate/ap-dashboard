package org.dhimate.mule.session;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
	public String access_token;
	public String token_type;

}
