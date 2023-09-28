package com.e2s.cam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e2s.cam.entity.GroupEntity;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
	
	List<GroupEntity> findAllByUserId(String userId);
	List<GroupEntity> findAllById(Long groupId);


}
