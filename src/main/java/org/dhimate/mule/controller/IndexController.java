package org.dhimate.mule.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dhimate.mule.apianalytics.AnypointAPIAnalyticsAPIIdRepository;
import org.dhimate.mule.apimanager.AnypointAPIManagerRepository;
import org.dhimate.mule.clientapplication.AnypointClientApplicationRepository;
import org.dhimate.mule.cloudhub.AnypointCloudhubRepository;
import org.dhimate.mule.cs.AnypointCoreServicesSubscriptionEntity;
import org.dhimate.mule.cs.AnypointCoreServicesSubscriptionRepository;
import org.dhimate.mule.cs.AnypointCoreServicesUsageEntity;
import org.dhimate.mule.cs.AnypointCoreServicesUsageRepository;
import org.dhimate.mule.designcenter.AnypointDesignCenterEntity;
import org.dhimate.mule.designcenter.AnypointDesignCenterRepository;
import org.dhimate.mule.exchange.AnypointExchangeEntity;
import org.dhimate.mule.exchange.AnypointExchangeRepository;
import org.dhimate.mule.organization.AnypointOrganizationRepository;
import org.dhimate.mule.user.AnypointUserEntity;
import org.dhimate.mule.user.AnypointUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class IndexController {

	@Autowired
	AnypointOrganizationRepository aorgrepository;

	@Autowired
	AnypointUserRepository ausrrepository;

	@Autowired
	AnypointExchangeRepository aexcrepository;

	@Autowired
	AnypointDesignCenterRepository adcrepository;

	@Autowired
	AnypointCoreServicesSubscriptionRepository subsrepository;

	@Autowired
    AnypointCoreServicesUsageRepository usagerepository;
    
    @Autowired
	AnypointCloudhubRepository cloudhubrepository;
	
	@Autowired
	AnypointAPIManagerRepository apimanagerrepository;
	
	@Autowired
    AnypointClientApplicationRepository clientapplicationrepository;
    
    @Autowired
    AnypointAPIAnalyticsAPIIdRepository apiidanalyticsrepository;

	@RequestMapping("/")
	public String index(Model model) {
		log.info("Landing on root page");

		List<AnypointUserEntity> users = ausrrepository.findAll();
		List<AnypointExchangeEntity> exchange = aexcrepository.findAll();
		List<AnypointDesignCenterEntity> designCenter = adcrepository.findAll();
		model.addAttribute("users", users);
		model.addAttribute("organizationName", aorgrepository.findAll().get(0).getOrganizationName());
		model.addAttribute("totalOrganizations",aorgrepository.findAll().get(0).getTotalSubOrganizations());
		model.addAttribute("exchange", exchange);
        model.addAttribute("designCenter", designCenter);

		AnypointCoreServicesUsageEntity usage = usagerepository.getOne((long) 1);
		AnypointCoreServicesSubscriptionEntity subscription = subsrepository.getOne((long) 1);

		SubscriptionUsage subscriptionUsage = new SubscriptionUsage(usage, subscription);

        model.addAttribute("subscriptionUsage", subscriptionUsage);
        
        int totalApplications =cloudhubrepository.findAll().stream().filter(p->p.getEnvironmentName().equals("Sandbox")).collect(Collectors.toList()).size(); 
        int totalAPIs = apimanagerrepository.findAll().stream().filter(p->p.getEnvironmentName().equals("Sandbox")).collect(Collectors.toList()).size();
        int totalAPIConsumers = clientapplicationrepository.findAll().stream().collect(Collectors.toList()).size();
        model.addAttribute("totalApplications", totalApplications);
        model.addAttribute("totalAPIs", totalAPIs);
        model.addAttribute("totalAPIConsumers", totalAPIConsumers);

        List<Integer> i= apiidanalyticsrepository.findAll().stream().filter(p->p.getEnvironmentName().equals("Sandbox")).map(p->p.getCount()).collect(Collectors.toList());

        Optional<Integer> totalCount = i.stream().collect(Collectors.reducing((p,q)->p+q));
        model.addAttribute("totalAPITransactions", totalCount.get());


//		log.info(subscriptionUsage.toString());

		return "index";
	}

	@Data
	public class SubscriptionUsage {
		private int prodVCore;
		private String prodVCoreClass;
		private int sandboxVCore;
		private String sandboxVCoreClass;
		private int designVCore;
		private int staticIp;
		private String staticIpClass;
		private int vpc;
		private String vpcClass;
		private int vpn;
		private String vpnClass;
		private int loadBalancer;
		private String loadBalancerClass;

		public SubscriptionUsage(AnypointCoreServicesUsageEntity usage,
				AnypointCoreServicesSubscriptionEntity subscription) {
			this.prodVCore = percentage(usage.getProductionVCoresConsumed(),
					subscription.getVCoresProductionAssigned());
			this.prodVCoreClass = indicatorClass(this.prodVCore);
			this.sandboxVCore = percentage(usage.getSandboxVCoresConsumed(), subscription.getVCoresSandboxAssigned());
			this.sandboxVCoreClass = indicatorClass(this.sandboxVCore);
			this.designVCore = percentage(usage.getDesignVCoresConsumed(), subscription.getVCoresDesignAssigned());
			this.staticIp = percentage(usage.getStaticIpsConsumed(), subscription.getStaticIpsAssigned());
			this.staticIpClass = indicatorClass(this.staticIp);
			this.vpc = percentage(usage.getVpcsConsumed(), subscription.getVpcsAssigned());
			this.vpcClass = indicatorClass(this.vpc);
			this.vpn = percentage(usage.getVpnsConsumed(), subscription.getVpnsAssigned());
			this.vpnClass = indicatorClass(this.vpn);
			this.loadBalancer = percentage(usage.getLoadBalancersConsumed(), subscription.getLoadBalancersAssigned());
			this.loadBalancerClass = indicatorClass(this.loadBalancer);

		}

		public int percentage(double usage, double subscription) {
			int percent = 0;

			if (subscription != 0) {
				percent = ((Number) ((usage / subscription) * 100)).intValue();
			}

			return percent;

		}

		public String indicatorClass(int percentage) {
			String indicatorClass = "";

			if (percentage <= 25) {
				indicatorClass = "progress-bar bg-danger";
			} else if (percentage <= 50 && percentage > 25) {
				indicatorClass = "progress-bar bg-warning";
			} else if (percentage <= 75 && percentage > 50) {
				indicatorClass = "progress-bar bg-info";
			} else {
				indicatorClass = "progress-bar bg-success";
			}

			return indicatorClass;

		}
	}

}
