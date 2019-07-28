package org.dhimate.mule.user;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class AnypointUserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String organizationId;
	private String userName;
	private String firstName;
	private String lastName;
	private boolean enabled;
	private LocalDateTime lastLogin;	
}
