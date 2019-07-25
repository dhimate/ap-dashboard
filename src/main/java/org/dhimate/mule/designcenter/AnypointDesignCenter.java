package org.dhimate.mule.designcenter;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class AnypointDesignCenter {

	private String id;
	private long createdDate;
	private long lastUpdateDate;
	private String name;
	private String type;

}
