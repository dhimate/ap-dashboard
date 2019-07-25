package org.dhimate.mule.user;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Component

public class AnypointUserWrapper {

	private int total;
	private List<AnypointUser> anypointUser;

	@JsonProperty("data")
	private void users(Map<String, Object>[] users) {

		if (anypointUser == null) {
			anypointUser = new ArrayList<AnypointUser>();
		} else {
			anypointUser.clear();
		}
		for (Map<String, Object> user : users) {
			AnypointUser tempUser = new AnypointUser();
			tempUser.setFirstName((String) user.get("firstName"));
			tempUser.setLastName((String) user.get("lastName"));
			tempUser.setUserName((String) user.get("username"));
			tempUser.setEnabled((boolean) user.get("enabled"));
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			LocalDateTime date = LocalDateTime.parse((CharSequence) user.get("lastLogin"), dtf);
			tempUser.setLastLogin(date);
			
			anypointUser.add(tempUser);
		}
	}

}