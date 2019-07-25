package org.dhimate.mule.cs;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class AnypointCoreServicesUsage {

	private double productionVCoresConsumed;
	private double sandboxVCoresConsumed;
	private double designVCoresConsumed;
	private double staticIpsConsumed;
	private double vpcsConsumed;
	private double vpnsConsumed;
	private double loadBalancersConsumed;

}
