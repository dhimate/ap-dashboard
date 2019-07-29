package org.dhimate.mule.apianalytics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnypointAPIAnalyticsQueryResponse {
    private boolean success;
    private boolean error;
    private List<AnypointAPIAnalyticsResponseAPIId> apiIds = new ArrayList<AnypointAPIAnalyticsResponseAPIId>();
    private List<AnypointAPIAnalyticsResponseClientId> clientIds = new ArrayList<AnypointAPIAnalyticsResponseClientId>();

//    AnypointAPIAnalyticsResponseAggregator anypointAnalyticsResponseAggregator = new AnypointAPIAnalyticsResponseAggregator();
    @JsonProperty("response")
    @SuppressWarnings("unchecked")
    public void response(List<Map<String, Object>> response) {

        response.forEach((item) -> {
            if (item.containsKey("api_id")) {
                List<Map<String,Object>> apiIdResponse = (List<Map<String,Object>>) item.get("api_id");
                List<AnypointAPIAnalyticsResponseAPIId> apiIds = new ArrayList<AnypointAPIAnalyticsResponseAPIId>();
                apiIdResponse.forEach((apiIdItem) -> {
                    String tempAPIId = (String) apiIdItem.keySet().iterator().next();
                    Map<String, Object> tempAPICountWrapper = (Map<String, Object>) apiIdItem.get(tempAPIId);
                    int tempAPICount = (int) tempAPICountWrapper.get("count");
                    apiIds.add(new AnypointAPIAnalyticsResponseAPIId(tempAPIId, tempAPICount));
                });
                //anypointAnalyticsResponseAggregator.setApiIds(apiIds);
                this.setApiIds(apiIds);
            } else if (item.containsKey("client_id")) {
                List<Map<String,Object>> clientIdResponse = (List<Map<String,Object>>) item.get("client_id");
                List<AnypointAPIAnalyticsResponseClientId> clientIds = new ArrayList<AnypointAPIAnalyticsResponseClientId>();
                clientIdResponse.forEach((clientIdItem) -> {
                    String tempClientId = (String) clientIdItem.keySet().iterator().next();
                    Map<String, Object> tempClientCountWrapper = (Map<String, Object>) clientIdItem.get(tempClientId);
                    int tempClientCount = (int) tempClientCountWrapper.get("count");
                    clientIds.add(new AnypointAPIAnalyticsResponseClientId(tempClientId, tempClientCount));
                });
                //anypointAnalyticsResponseAggregator.setClientIds(clientIds);
                this.setClientIds(clientIds);
            }

        });
    }
}