package org.dhimate.mule.apimanager;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Component

public class AnypointAPIManagerWrapper {
	private int total;
	private List<AnypointAPIManager> anypointAPIManagerAssets;


}
