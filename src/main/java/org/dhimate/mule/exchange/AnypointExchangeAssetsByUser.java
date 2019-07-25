package org.dhimate.mule.exchange;

import lombok.Data;

@Data
public class AnypointExchangeAssetsByUser {
	String createdByName;
	Long count;
	public AnypointExchangeAssetsByUser(String createdByName, Long count) {
		this.createdByName = createdByName;
		this.count = count;
	}
	public AnypointExchangeAssetsByUser() {
	}
	
}
