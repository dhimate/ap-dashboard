package org.dhimate.mule.apianalytics;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class AnypointAPIAnalyticsAPIIdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String organizationId;
	private String environmentId;
    private String environmentName;

    private String apiId;
    private String apiName;
    private int count;

    
}