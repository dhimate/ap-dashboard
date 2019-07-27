package org.dhimate.mule.controller;

import java.util.List;

import org.dhimate.mule.apimanager.AnypointAPIManagerEntity;
import org.dhimate.mule.apimanager.AnypointAPIManagerRepository;
import org.dhimate.mule.cloudhub.AnypointCloudhubEntity;
import org.dhimate.mule.cloudhub.AnypointCloudhubRepository;
import org.dhimate.mule.cs.AnypointCoreServicesSubscriptionEntity;
import org.dhimate.mule.cs.AnypointCoreServicesSubscriptionRepository;
import org.dhimate.mule.cs.AnypointCoreServicesUsageEntity;
import org.dhimate.mule.cs.AnypointCoreServicesUsageRepository;
import org.dhimate.mule.designcenter.AnypointDesignCenterEntity;
import org.dhimate.mule.designcenter.AnypointDesignCenterRepository;
import org.dhimate.mule.environment.AnypointEnvironmentEntity;
import org.dhimate.mule.environment.AnypointEnvironmentRepository;
import org.dhimate.mule.exchange.AnypointExchangeEntity;
import org.dhimate.mule.exchange.AnypointExchangeRepository;
import org.dhimate.mule.organization.AnypointOrganizationEntity;
import org.dhimate.mule.organization.AnypointOrganizationRepository;
import org.dhimate.mule.user.AnypointUserEntity;
import org.dhimate.mule.user.AnypointUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class APIController {

	@Autowired
	AnypointOrganizationRepository orgrepository;

	@Autowired
	AnypointUserRepository userrepository;

	@Autowired
	AnypointExchangeRepository exchrepository;

	@Autowired
	AnypointDesignCenterRepository dcrepository;

	@Autowired
	AnypointCoreServicesSubscriptionRepository subsrepository;

	@Autowired
	AnypointCoreServicesUsageRepository usagerepository;

	@Autowired
	AnypointEnvironmentRepository environmentrepository;

	@Autowired
	AnypointCloudhubRepository cloudhubrepository;
	
	@Autowired
	AnypointAPIManagerRepository apimanagerrepository;

	@GetMapping("/users")
	@ResponseBody
	List<AnypointUserEntity> users() {

		return userrepository.findAll();
	}

	@GetMapping("/organizations")
	@ResponseBody
	List<AnypointOrganizationEntity> organizations() {

		return orgrepository.findAll();
	}

	@GetMapping("/exchange")
	@ResponseBody
	List<AnypointExchangeEntity> exchange() {

		return exchrepository.findAll();
	}

	@GetMapping("/design-center")
	@ResponseBody
	List<AnypointDesignCenterEntity> designCenter() {

		return dcrepository.findAll();
	}

	@GetMapping("/subscription")
	@ResponseBody
	List<AnypointCoreServicesSubscriptionEntity> subscription() {

		return subsrepository.findAll();
	}

	@GetMapping("/usage")
	@ResponseBody
	List<AnypointCoreServicesUsageEntity> usage() {

		return usagerepository.findAll();
	}

	@GetMapping("/environment")
	@ResponseBody
	List<AnypointEnvironmentEntity> environment() {

		return environmentrepository.findAll();
	}

	@GetMapping("/cloudhub")
	@ResponseBody
	List<AnypointCloudhubEntity> cloudhub() {
		return cloudhubrepository.findAll();
	}
	
	@GetMapping("/apimanager")
	@ResponseBody
	List<AnypointAPIManagerEntity> apimanager() {
		return apimanagerrepository.findAll();
	}
}