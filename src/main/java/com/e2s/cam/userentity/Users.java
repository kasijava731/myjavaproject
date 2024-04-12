package com.e2s.cam.userentity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "authentication" ,name="users")
@Getter
@Setter
public class Users {
	@Id
	@Column(name="id")
	private Long id;
	@Column(name="status")
	private String status;
	@Column(name = "organization_id")
	private Long organizationId;

}
