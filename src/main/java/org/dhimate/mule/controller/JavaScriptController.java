package org.dhimate.mule.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.Collectors;

import org.dhimate.mule.apianalytics.AnypointAPIAnalyticsAPIIdEntity;
import org.dhimate.mule.apianalytics.AnypointAPIAnalyticsAPIIdRepository;
import org.dhimate.mule.apianalytics.AnypointAPIAnalyticsClientIdEntity;
import org.dhimate.mule.apianalytics.AnypointAPIAnalyticsClientIdRepository;
import org.dhimate.mule.exchange.AnypointExchangeAssetsByUser;
import org.dhimate.mule.exchange.AnypointExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
// @Slf4j
public class JavaScriptController {

    @Autowired
    AnypointExchangeRepository aexcrepository;

    @Autowired
    AnypointAPIAnalyticsAPIIdRepository apiidanalyticsrepository;

    @Autowired
    AnypointAPIAnalyticsClientIdRepository clientidanalyticsrepository;

    @RequestMapping(value = "js/chart-bar-demo.js", method = RequestMethod.GET)
    public String bar(Model model) {

        List<AnypointExchangeAssetsByUser> assetsByUser = aexcrepository.calculateAssetsByUser();

        List<String> users = new ArrayList<String>();
        List<Long> count = new ArrayList<Long>();

        for (AnypointExchangeAssetsByUser temp : assetsByUser) {
            users.add(temp.getCreatedByName());
            count.add(temp.getCount());
        }

        OptionalLong countWrapper = count.stream().mapToLong(v -> v).max();

        Long maxCount = ((Number) Double.parseDouble(Math.ceil(countWrapper.getAsLong() / 20.0) + "")).longValue() * 20;

        model.addAttribute("exchangeAssetsByUser", users.toArray());
        model.addAttribute("exchangeAssetsCount", count.toArray());
        model.addAttribute("exchangeMaxCount", maxCount);

        return "js/chart-bar-demo.js";
    }

    @RequestMapping(value = "js/chart-bar-api-id-sandbox.js", method = RequestMethod.GET)
    public String sandboxApiId(Model model) {

        List<AnypointAPIAnalyticsAPIIdEntity> callsByAPIId = apiidanalyticsrepository.findAll().stream()
                .filter(p -> p.getEnvironmentName().equals("Sandbox")).collect(Collectors.toList());

        List<String> apiId = new ArrayList<String>();
        List<Long> apiIdCount = new ArrayList<Long>();

        for (AnypointAPIAnalyticsAPIIdEntity temp : callsByAPIId) {
            apiId.add(temp.getApiName());
            apiIdCount.add(((Number) temp.getCount()).longValue());
        }

        OptionalLong maxAPICallsWrapper = apiIdCount.stream().mapToLong(v -> v).max();

        Long maxAPICalls = new Long("0");
        if (maxAPICallsWrapper.isPresent()) {
            maxAPICalls = ((Number) Double.parseDouble(Math.ceil(maxAPICallsWrapper.getAsLong() / 2000.0) + "")).longValue() * 2000;
        }

        model.addAttribute("maxAPICalls", maxAPICalls);
        model.addAttribute("apiIdName", apiId.toArray());
        model.addAttribute("apiIdCount", apiIdCount.toArray());

        return "js/chart-bar-api-id-sandbox.js";
    }

    @RequestMapping(value = "js/chart-bar-client-id-sandbox.js", method = RequestMethod.GET)
    public String sandboxClientId(Model model) {

        List<AnypointAPIAnalyticsClientIdEntity> callsByClientId = clientidanalyticsrepository.findAll().stream()
                .filter(p -> p.getEnvironmentName().equals("Sandbox")).collect(Collectors.toList());

        List<String> clientId = new ArrayList<String>();
        List<Long> clientIdCount = new ArrayList<Long>();

        for (AnypointAPIAnalyticsClientIdEntity temp : callsByClientId) {
            clientId.add(temp.getClientName());
            clientIdCount.add(((Number) temp.getCount()).longValue());
        }

        OptionalLong maxAPICallsWrapper = clientIdCount.stream().mapToLong(v -> v).max();

        Long maxAPICalls = new Long("0");
        if (maxAPICallsWrapper.isPresent()) {
            maxAPICalls = ((Number) Double.parseDouble(Math.ceil(maxAPICallsWrapper.getAsLong() / 2000.0) + "")).longValue() * 2000;
        }

        model.addAttribute("maxAPICalls", maxAPICalls);
        model.addAttribute("clientIdName", clientId.toArray());
        model.addAttribute("clientIdCount", clientIdCount.toArray());

        return "js/chart-bar-client-id-sandbox.js";
    }
}
