package org.dhimate.mule.controller;

import java.util.List;

import org.dhimate.mule.designcenter.AnypointDesignCenterEntity;
import org.dhimate.mule.designcenter.AnypointDesignCenterRepository;
import org.dhimate.mule.organization.AnypointOrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DesignCenterController {

	@Autowired
	AnypointOrganizationRepository aorgrepository;

	@Autowired
	AnypointDesignCenterRepository adcrepository;

	@RequestMapping("/designCenter")
	public String index(Model model) {
		log.info("Landing on design center page");

		List<AnypointDesignCenterEntity> designCenter = adcrepository.findAll();
		model.addAttribute("organizationName", aorgrepository.findAll().get(0).getOrganizationName());

		model.addAttribute("designCenter", designCenter);

		return "designCenter";
	}
}
