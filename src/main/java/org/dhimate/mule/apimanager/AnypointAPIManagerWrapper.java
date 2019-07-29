package org.dhimate.mule.apimanager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class AnypointAPIManagerWrapper {
	private int total;
	private List<AnypointAPIManager> anypointAPIManagerAssets;

	@JsonProperty("assets")
	@SuppressWarnings("unchecked")
	private void assets(Map<String, Object>[] assets) {

		for (Map<String, Object> asset : assets) {

            String exchangeAssetName = (String) asset.get("exchangeAssetName");
            long apiId = ((Number) asset.get("id")).longValue();


			ArrayList<Map<String, Object>> apis = (ArrayList<Map<String, Object>>) asset.get("apis");

			for (Map<String, Object> api : apis) {
				if (anypointAPIManagerAssets == null) {
					anypointAPIManagerAssets = new ArrayList<AnypointAPIManager>();
				} 
				
				AnypointAPIManager tempAnypointAPIManager = new AnypointAPIManager();

				Map<String, Object> audit = (Map<String, Object>) api.get("audit");
				Map<String, Object> created = (Map<String, Object>) audit.get("created");
				Map<String, Object> updated = (Map<String, Object>) audit.get("updated");

                tempAnypointAPIManager.setExchangeAssetName(exchangeAssetName);
                tempAnypointAPIManager.setApiId(apiId);

				
				tempAnypointAPIManager.setApiCreatedDate(formatDate((String) created.get("date")));
				if (updated.get("date")!= null)
					tempAnypointAPIManager.setApiUpdatedDate(formatDate((String) updated.get("date")));
				tempAnypointAPIManager.setApiAutoDiscoveryId(((Number) api.get(("id"))).longValue());
				tempAnypointAPIManager.setApiAssetId((String) api.get("assetId"));
				if(api.get("lastActiveDate")!= null)
					tempAnypointAPIManager.setApiLastActiveDate(formatDate((String) api.get("lastActiveDate")));
				tempAnypointAPIManager.setApiAssetVersion((String) api.get("assetVersion"));
				tempAnypointAPIManager.setApiProductVersion((String) api.get("productVersion"));
				tempAnypointAPIManager
						.setApiActiveContractsCount(((Number) api.get("activeContractsCount")).longValue());
				anypointAPIManagerAssets.add(tempAnypointAPIManager);
			}
		}

	}

	public LocalDateTime formatDate(String input) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		LocalDateTime date = LocalDateTime.parse(input, dtf);

		return date;
	}
}
