package org.dhimate.mule.clientapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * AnypointClientApplicationWrapper
 */
@Component
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnypointClientApplicationWrapper {

	private List<AnypointClientApplication> anypointClientApplication;

	@JsonProperty("client_id")
	private void clientId(Map<String, Object> clientId) {
		if (anypointClientApplication == null) {
			anypointClientApplication = new ArrayList<AnypointClientApplication>();
		}
		clientId.forEach((k, v) -> {
			AnypointClientApplication temp = new AnypointClientApplication();
			temp.setClientApplicationId(k);
			temp.setClientApplicationName((String) v);
			anypointClientApplication.add(temp);
		});

	}

}