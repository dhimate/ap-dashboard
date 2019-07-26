package org.dhimate.mule.cloudhub;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class AnypointCloudhub {

	@JsonProperty("domain")
	private String name;

	@JsonProperty("status")
	private String status;

	private String workerType;
	private double workersWeight;
	private int numWorkers;

	private double totalWorkersWeight;

	@JsonIgnore
	private String runtimeVersion;

	@SuppressWarnings("unchecked")
	@JsonProperty("workers")
	private void workers(Map<String, Object> workers) {

		this.numWorkers = ((Number) workers.get("amount")).intValue();

		Map<String, Object> type = (Map<String, Object>) workers.get("type");
		this.workerType = (String) type.get("name");
		this.workersWeight = ((Number) type.get("weight")).doubleValue();
		this.totalWorkersWeight = this.workersWeight * this.numWorkers;
	}

	@JsonProperty("muleVersion")
	private void muleVersion(Map<String, Object> muleVersion) {
		this.runtimeVersion = (String) muleVersion.get("version");
	}

}
