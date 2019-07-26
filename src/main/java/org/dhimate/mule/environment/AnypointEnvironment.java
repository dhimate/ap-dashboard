package org.dhimate.mule.environment;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class AnypointEnvironment {
	
	private String environmentId;
	private String name;
	private boolean isProduction;
	private String type;
	

}
