package com.e2s.cam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vw_staff_role_details")
@Getter
@Setter
public class GroupDetails {
	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "role_name")
	private String name;
	@Column(name = "user_id")
	private String userId;

	@Column(name = "staff_id")
	private String staffId;

}
