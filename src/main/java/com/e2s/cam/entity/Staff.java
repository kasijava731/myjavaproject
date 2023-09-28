package com.e2s.cam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "staffs")
@Getter
@Setter
public class Staff {
	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "status")
	private String status;
	
	@Column(name = "staff_user_id")
	private String staffId;

}
