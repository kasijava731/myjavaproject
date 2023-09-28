package com.e2s.cam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vw_staff_details_cam")
@Getter
@Setter
public class UserEntity {
	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "user_name")
	private String userName;

	@Column(name = "name")
	private String displayName;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "created_at")
	private Long createdAt;
	
	@Column(name = "updated_at")
	private Long updatedAt;
	
	@Column(name = "profile_url")
	private String profilePicUrl;
	
	@Column(name = "user_type")
	private String userType;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "staff_id")
	private String staffId;

	
}
