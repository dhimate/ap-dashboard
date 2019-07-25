package org.dhimate.mule.exchange;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class AnypointExchange {

	private String assetId;
	private String version;
	private String name;
	private String type;
	private String createdByUserId;
	private String createdByName;
	private String createdAt;
	private String modifiedAt;

	@JsonProperty("createdBy")
	private void createdByDetails(Map<String, Object> createdBy) {
		this.createdByUserId = (String) createdBy.get("userName");
		this.createdByName = (String) createdBy.get("firstName") + " " + (String) createdBy.get("lastName");
	}

}
