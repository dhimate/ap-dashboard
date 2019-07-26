package org.dhimate.mule.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class AnypointEnvironmentWrapper {

	private int total;
	private List<AnypointEnvironment> anypointEnvironment;

	@JsonProperty("data")
	private void environments(Map<String, Object>[] environments) {

		if (anypointEnvironment == null) {
			anypointEnvironment = new ArrayList<AnypointEnvironment>();
		} else {
			anypointEnvironment.clear();
		}

		for (Map<String, Object> environment : environments) {

			AnypointEnvironment tempEnvironment = new AnypointEnvironment();
			tempEnvironment.setEnvironmentId((String) environment.get("id"));
			tempEnvironment.setName((String) environment.get("name"));
			tempEnvironment.setProduction((boolean) environment.get("isProduction"));
			tempEnvironment.setType((String) environment.get("type"));

			anypointEnvironment.add(tempEnvironment);
		}

	}

}
