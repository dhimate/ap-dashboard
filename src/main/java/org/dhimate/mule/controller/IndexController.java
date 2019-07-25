package org.dhimate.mule.controller;

import java.util.List;

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

	@RequestMapping("/")
	public String index(Model model) {
		log.info("Landing on root page");

		List<AnypointUserEntity> users = ausrrepository.findAll();
		List<AnypointExchangeEntity> exchange = aexcrepository.findAll();
		List<AnypointDesignCenterEntity> designCenter = adcrepository.findAll();
		model.addAttribute("users", users);
		model.addAttribute("organizationName", aorgrepository.findAll().get(0).getOrganizationName());
		model.addAttribute("exchange", exchange);
		model.addAttribute("designCenter", designCenter);

		AnypointCoreServicesUsageEntity usage = usagerepository.getOne((long) 1);
		AnypointCoreServicesSubscriptionEntity subscription = subsrepository.getOne((long) 1);
		
		SubscriptionUsage subscriptionUsage = new SubscriptionUsage(usage, subscription);
		
		model.addAttribute("subscriptionUsage", subscriptionUsage);
		
		log.info(subscriptionUsage.toString());

		return "index";
	}

	@Data
	public class SubscriptionUsage {
		private int prodVCore;
		private int sandboxVCore;
		private int designVCore;
		private int staticIp;
		private int vpc;
		private int vpn;
		private int loadBalancer;

		public SubscriptionUsage(AnypointCoreServicesUsageEntity usage,
				AnypointCoreServicesSubscriptionEntity subscription) {
			this.prodVCore = percentage(usage.getProductionVCoresConsumed(),
					subscription.getVCoresProductionAssigned());
			this.sandboxVCore = percentage(usage.getSandboxVCoresConsumed(), subscription.getVCoresSandboxAssigned());
			this.designVCore = percentage(usage.getDesignVCoresConsumed(), subscription.getVCoresDesignAssigned());
			this.staticIp = percentage(usage.getStaticIpsConsumed(), subscription.getStaticIpsAssigned());
			this.vpc = percentage(usage.getVpcsConsumed(), subscription.getVpcsAssigned());
			this.vpn = percentage(usage.getVpnsConsumed(), subscription.getVpnsAssigned());
			this.loadBalancer = percentage(usage.getLoadBalancersConsumed(), subscription.getLoadBalancersAssigned());

		}

		public int percentage(double usage, double subscription) {
			int percent = 0;

			if (subscription != 0) {
				percent = ((Number) ((usage / subscription) * 100)).intValue();
			}

			return percent;

		}
	}

}
