package org.dhimate.mule.apianalytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnypointAPIAnalyticsRequestAggregator {
    private String dimension;
    private String order;

}