package org.dhimate.mule.apianalytics;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
        LocalDateTime firstDayOfTheMonth = LocalDateTime.now().withDayOfMonth(1).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        LocalDateTime lastDayOfPreviousMonth = firstDayOfTheMonth.minusDays(1);
        LocalDateTime firstDayOfPreviousMonth = lastDayOfPreviousMonth.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(000000000);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String days = ChronoUnit.DAYS.between(firstDayOfPreviousMonth, lastDayOfPreviousMonth) + 1 + "d";

        this.type = "enriched-http-event";
        this.duration = days;
        this.aggregators = new ArrayList<AnypointAPIAnalyticsRequestAggregator>();
        aggregators.add(new AnypointAPIAnalyticsRequestAggregator("api_id","descending"));
        aggregators.add(new AnypointAPIAnalyticsRequestAggregator("client_id","descending"));
        this.start_time = firstDayOfPreviousMonth.format(formatter) ;
        this.include_policy_violation = true;
    }
    
}