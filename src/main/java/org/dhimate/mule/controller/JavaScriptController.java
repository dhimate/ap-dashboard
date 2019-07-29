package org.dhimate.mule.controller;

import java.util.ArrayList;
import java.util.List;

import org.dhimate.mule.exchange.AnypointExchangeAssetsByUser;
import org.dhimate.mule.exchange.AnypointExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
//@Slf4j
public class JavaScriptController {

	@Autowired
	AnypointExchangeRepository aexcrepository;

	@RequestMapping(value = "js/chart-bar-demo.js", method = RequestMethod.GET)
	public String bar(Model model) {

		List<AnypointExchangeAssetsByUser> assetsByUser = aexcrepository.calculateAssetsByUser();

		List<String> users = new ArrayList<String>();
		List<Long> count = new ArrayList<Long>();

		for (AnypointExchangeAssetsByUser temp : assetsByUser) {
			users.add(temp.getCreatedByName());
			count.add(temp.getCount());
		}

		model.addAttribute("exchangeAssetsByUser", users.toArray());
		model.addAttribute("exchangeAssetsCount", count.toArray());

		return "js/chart-bar-demo.js";
	}
}
