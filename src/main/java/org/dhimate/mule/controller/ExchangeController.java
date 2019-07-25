package org.dhimate.mule.controller;

import java.util.List;

import org.dhimate.mule.exchange.AnypointExchangeEntity;
import org.dhimate.mule.exchange.AnypointExchangeRepository;
import org.dhimate.mule.organization.AnypointOrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ExchangeController {

	@Autowired
	AnypointOrganizationRepository aorgrepository;



	@Autowired
	AnypointExchangeRepository aexcrepository;

	@RequestMapping("/exchange")
	public String index(Model model) {
		log.info("Landing on design center page");

		List<AnypointExchangeEntity> exchange = aexcrepository.findAll();
		
		log.info(exchange.toString());
		
		model.addAttribute("organizationName", aorgrepository.findAll().get(0).getOrganizationName());

		model.addAttribute("exchange", exchange);

		return "exchange";
	}
}
