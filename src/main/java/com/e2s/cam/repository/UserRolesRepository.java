package com.e2s.cam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e2s.cam.entity.UserRoles;

import jakarta.transaction.Transactional;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Long>{
	
	UserRoles findByUserIdAndRoleId(String userId,Long roleId);
	List<UserRoles> findAllByRoleId(Long roleId);
	@Transactional
	 void deleteByUserId(String userId); 



}
