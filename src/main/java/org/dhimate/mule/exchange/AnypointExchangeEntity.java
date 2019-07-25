package org.dhimate.mule.exchange;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class AnypointExchangeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String assetId;
	private String version;
	private String name;
	private String type;
	private String createdByUserId;
	private String createdByName;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

}