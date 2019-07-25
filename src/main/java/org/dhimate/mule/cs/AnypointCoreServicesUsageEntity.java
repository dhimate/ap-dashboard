package org.dhimate.mule.cs;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class AnypointCoreServicesUsageEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private double productionVCoresConsumed;
	private double sandboxVCoresConsumed;
	private double designVCoresConsumed;
	private double staticIpsConsumed;
	private double vpcsConsumed;
	private double vpnsConsumed;
	private double loadBalancersConsumed;


}
