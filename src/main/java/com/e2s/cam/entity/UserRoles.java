package com.e2s.cam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "user_roles")
public class UserRoles {
	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "role_id")
	private Long roleId;
	@Column(name = "status")
	private String status;
	@Column(name = "created_at")
	private Long createdAt;
	@Column(name = "created_by")
	private Long createdBy;
	@Column(name = "updated_by")
	private Long updateBy;
	@Column(name = "updated_at")
	private Long updatedAt;
	@Column(name = "department_id")
	private Long departmentId;

}
