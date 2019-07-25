package org.dhimate.mule.organization;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class AnypointOrganization {

	private String organizationId;
	private String organizationName;

	@JsonProperty("organization")
	private void organizationDetails(Map<String, Object> organization) {
		this.organizationName = (String) organization.get("name");
	}

}
