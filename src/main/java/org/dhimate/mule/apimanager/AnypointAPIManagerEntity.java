package org.dhimate.mule.apimanager;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class AnypointAPIManagerEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String organizationId;
	private String environmentId;
	private String environmentName;
    private String exchangeAssetName;
    private long apiId;
	private long apiAutoDiscoveryId;
	private String apiAssetId;
	private LocalDateTime apiCreatedDate;
	private LocalDateTime apiUpdatedDate;
	private LocalDateTime apiLastActiveDate;
	private String apiAssetVersion;
	private String apiProductVersion;
	private long apiActiveContractsCount;

	
}
