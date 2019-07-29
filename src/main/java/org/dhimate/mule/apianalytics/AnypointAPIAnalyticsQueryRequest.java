package org.dhimate.mule.apianalytics;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Component("AnypointAPIAnalyticsQueryRequest")
public class AnypointAPIAnalyticsQueryRequest {
    private String type;
    private String duration;
    private List<AnypointAPIAnalyticsRequestAggregator> aggregators;
    private String start_time;
    private boolean include_policy_violation;

    public AnypointAPIAnalyticsQueryRequest() {
        this.type = "enriched-http-event";
        this.duration = "20d";
        this.aggregators = new ArrayList<AnypointAPIAnalyticsRequestAggregator>();
        aggregators.add(new AnypointAPIAnalyticsRequestAggregator("api_id","descending"));
        aggregators.add(new AnypointAPIAnalyticsRequestAggregator("client_id","descending"));
        this.start_time = "2019-07-01T00:00:00.000Z";   
        this.include_policy_violation = true;
    }
    
}