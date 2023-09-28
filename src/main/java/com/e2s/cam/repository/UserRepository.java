package com.e2s.cam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.e2s.cam.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>,JpaSpecificationExecutor<UserEntity> {
//	UserEntity findByUserId(String userId);
	UserEntity findByStaffId(String userId);

	Page<UserEntity> findAll(Pageable pageable);
//	Page<UserEntity> findAllByUserId(String userName,Pageable pageable);
	Page<UserEntity> findAllByStaffId(String userName,Pageable pageable);

}
