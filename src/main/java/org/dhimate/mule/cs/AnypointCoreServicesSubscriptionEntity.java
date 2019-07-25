package org.dhimate.mule.cs;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class AnypointCoreServicesSubscriptionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private double vCoresProductionAssigned;
	private double vCoresProductionReassigned;
	private double vCoresSandboxAssigned;
	private double vCoresSandboxReassigned;
	private double vCoresDesignAssigned;
	private double vCoresDesignReassigned;
	private int staticIpsAssigned;
	private int staticIpsReassigned;
	private int vpcsAssigned;
	private int vpcsReassigned;
	private int vpnsAssigned;
	private int vpnsReassigned;
	private int loadBalancersAssigned;
	private int loadBalancersReassigned;

}
