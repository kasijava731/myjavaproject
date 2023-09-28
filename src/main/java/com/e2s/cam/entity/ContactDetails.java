package com.e2s.cam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "alumni_contact_details")
@Getter
@Setter
public class ContactDetails {
	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "type")
	private String type;

	@Column(name = "source")
	private String source;

	@Column(name = "primary")
	private Boolean primary;

	@Column(name = "user_id")
	private String userId;

}
