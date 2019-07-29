package org.dhimate.mule.apianalytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnypointAPIAnalyticsResponseAPIId {
    private String apiId;
    private int count;
    
}