package org.dhimate.mule.controller;

import java.util.List;

import org.dhimate.mule.designcenter.AnypointDesignCenterEntity;
import org.dhimate.mule.designcenter.AnypointDesignCenterRepository;
import org.dhimate.mule.exchange.AnypointExchangeEntity;
import org.dhimate.mule.exchange.AnypointExchangeRepository;
import org.dhimate.mule.organization.AnypointOrganizationEntity;
import org.dhimate.mule.organization.AnypointOrganizationRepository;
import org.dhimate.mule.session.AnypointConnectionFactory;
import org.dhimate.mule.user.AnypointUserEntity;
import org.dhimate.mule.user.AnypointUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/api")
public class APIController {
	/*
	 * @Autowired AnypointOrganizationService aos;
	 * 
	 * @Autowired AnypointUserService aus;
	 */

	@Autowired
	AnypointOrganizationRepository aorgrepository;

	@Autowired
	AnypointUserRepository ausrrepository;

	@Autowired
	AnypointConnectionFactory ac;

	@Autowired
	AnypointExchangeRepository aexcrepository;

	@Autowired
	AnypointDesignCenterRepository adcrepository;


	@GetMapping("/users")
	@ResponseBody
	List<AnypointUserEntity> users() {

		return ausrrepository.findAll();
	}

	@GetMapping("/organizations")
	@ResponseBody
	List<AnypointOrganizationEntity> organizations() {

		return aorgrepository.findAll();
	}

	@GetMapping("/exchange")
	@ResponseBody
	List<AnypointExchangeEntity> exchange() {

		return aexcrepository.findAll();
	}

	@GetMapping("/design-center")
	@ResponseBody
	List<AnypointDesignCenterEntity> designCenter() {

		return adcrepository.findAll();
	}

}