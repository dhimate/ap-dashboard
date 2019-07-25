package org.dhimate.mule.user;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component

public class AnypointUser {
	private String userName;
	private String firstName;
	private String lastName;
	private boolean enabled;
	private LocalDateTime lastLogin;	
}
