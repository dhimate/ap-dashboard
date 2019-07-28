package org.dhimate.mule.clientapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class AnypointClientApplication {

    private String clientApplicationId;
    private String clientApplicationName;
    
}