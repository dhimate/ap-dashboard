package org.dhimate.mule.cloudhub;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class AnypointCloudhubEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String organizationId;
	private String environmentId;
	private String environmentName;
	private String name;
	private String status;
	private String workerType;
	private double workersWeight;
	private int numWorkers;
	private double totalWorkersWeight;
	private String runtimeVersion;
}
