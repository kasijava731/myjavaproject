package com.e2s.cam.entity;

import jakarta.persistence.Table;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Roles {
    @Id 
    @Column(name = "id")
	private Long groupId;
	@Column(name = "name")
	private String groupName;
	@Column(name = "created_at")
	private Long createdAt;
	@Column(name = "updated_at")
	private Long updatedAt;

}
