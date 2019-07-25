package org.dhimate.mule.organization;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class AnypointOrganizationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String organizationId;
	private String organizationName;

	public AnypointOrganizationEntity() {

	}

	public AnypointOrganizationEntity(String organizationId, String organizationName) {
		this.organizationId = organizationId;
		this.organizationName = organizationName;
	}

}
