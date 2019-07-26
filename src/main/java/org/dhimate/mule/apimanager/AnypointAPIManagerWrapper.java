package org.dhimate.mule.apimanager;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
@Slf4j
public class AnypointAPIManagerWrapper {
	private int total;
	private List<AnypointAPIManager> anypointAPIManagerAssets;
	private String assetId;
	
	@JsonProperty("assets")
	private void assets(Map<String, Object>[] assets) {
		
		for (Map<String, Object> asset: assets) {
			
			Map<String, Object> audit = (Map<String, Object>) asset.get("audit");
			Map<String, Object> created = (Map<String, Object>) audit.get("created");
			String createdDate =  (String) created.get("date");
			String exchangeAssetName = (String) asset.get("exchangeAssetName");
			assetId = (String)asset.get("assetId");

			log.info("CREATED DATE " + createdDate + " EXCHANGE ASSET NAME " + exchangeAssetName + " ASSET NAME " + assetId) ;
		}
		
	}
}
