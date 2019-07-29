package org.dhimate.mule.apianalytics;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * AnypointAPIAnalyticsClientIdEntity
 */
@Data
@Entity
public class AnypointAPIAnalyticsClientIdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String organizationId;
	private String environmentId;
    private String environmentName;

    private String clientId;
    private String clientName;
    private int count;
 
}