package com.e2s.cam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.e2s.cam.entity.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long>,JpaSpecificationExecutor<Roles> {
	
	Roles findByGroupId(Long id);

}
