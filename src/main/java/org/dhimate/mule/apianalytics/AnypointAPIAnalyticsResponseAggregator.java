package org.dhimate.mule.apianalytics;

import java.util.List;

import lombok.Data;

@Data
public class AnypointAPIAnalyticsResponseAggregator {
    private List<AnypointAPIAnalyticsResponseAPIId> apiIds;
    private List<AnypointAPIAnalyticsResponseClientId> clientIds;
    
}  