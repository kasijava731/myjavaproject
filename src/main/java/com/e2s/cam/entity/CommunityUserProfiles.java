package com.e2s.cam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="community_user_profiles")
@Getter
@Setter
public class CommunityUserProfiles {
	@Id
	@Column(name="id")
	private Long id;
	@Column(name="user_id")
	private String userId;
	@Column(name="profile_status")
	private String status;
	

}
