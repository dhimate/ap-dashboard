package org.dhimate.mule.apimanager;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class AnypointAPIManager {

	private String exchangeAssetName;
	private long apiAutoDiscoveryId;
	private String apiAssetId;
	private LocalDateTime apiCreatedDate;
	private LocalDateTime apiUpdatedDate;
	private LocalDateTime apiLastActiveDate;
	private String apiAssetVersion;
	private String apiProductVersion;
	private long apiActiveContractsCount;
}
