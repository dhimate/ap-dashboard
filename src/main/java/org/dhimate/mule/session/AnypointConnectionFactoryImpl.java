package org.dhimate.mule.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component("AnypointConnectionFactory")
@DependsOn("AnypointConnection")
@Slf4j
@Data
public class AnypointConnectionFactoryImpl implements AnypointConnectionFactory {

	@Autowired
	@Qualifier("AnypointConnection")
	AnypointConnection ac;

	@Override
	public AnypointConnection getConnection() {
		long currentMili = System.currentTimeMillis() / 1000;
		long validTill = ac.getValidFrom() + ac.getValidity();

		if (currentMili > validTill) {
			log.info("Connection expired. Requesting new token");
			ac.refreshToken();
			log.info("Refreshed token " + ac.getAccessToken());
		}
		return this.ac;
	}

}
