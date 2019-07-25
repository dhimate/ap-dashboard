package org.dhimate.mule.cs;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class AnypointCoreServicesSubscription {

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

	@SuppressWarnings("unchecked")
	@JsonProperty("user")
	private void createdByDetails(Map<String, Object> user) {

		Map<String, Object> organization = (Map<String, Object>) user.get("organization");
		Map<String, Object> entitlement = (Map<String, Object>) organization.get("entitlements");

		Map<String, Object> vCoresProduction = (Map<String, Object>) entitlement.get("vCoresProduction");
		this.setVCoresProductionAssigned(((Number) vCoresProduction.get("assigned")).doubleValue());
		this.setVCoresProductionReassigned(((Number) vCoresProduction.get("reassigned")).doubleValue());

		Map<String, Object> vCoresSandbox = (Map<String, Object>) entitlement.get("vCoresSandbox");
		this.setVCoresSandboxAssigned(((Number) vCoresSandbox.get("assigned")).doubleValue());
		this.setVCoresSandboxReassigned(((Number) vCoresSandbox.get("reassigned")).doubleValue());

		Map<String, Object> vCoresDesign = (Map<String, Object>) entitlement.get("vCoresDesign");
		this.setVCoresDesignAssigned(((Number) vCoresDesign.get("assigned")).doubleValue());
		this.setVCoresDesignReassigned(((Number) vCoresDesign.get("reassigned")).doubleValue());

		Map<String, Object> staticIps = (Map<String, Object>) entitlement.get("staticIps");
		this.setStaticIpsAssigned((int) staticIps.get("assigned"));
		this.setStaticIpsReassigned((int) staticIps.get("reassigned"));

		Map<String, Object> vpcs = (Map<String, Object>) entitlement.get("vpcs");
		this.setVpcsAssigned((int) vpcs.get("assigned"));
		this.setVpcsReassigned((int) vpcs.get("reassigned"));

		Map<String, Object> vpns = (Map<String, Object>) entitlement.get("vpns");
		this.setVpnsAssigned((int) vpns.get("assigned"));
		this.setVpnsReassigned((int) vpns.get("reassigned"));

		Map<String, Object> loadBalancers = (Map<String, Object>) entitlement.get("loadBalancer");
		this.setLoadBalancersAssigned((int) loadBalancers.get("assigned"));
		this.setLoadBalancersReassigned((int) loadBalancers.get("reassigned"));

	}

}
