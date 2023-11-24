package com.e2s.cam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e2s.cam.entity.GroupDetails;
import com.e2s.cam.entity.GroupEntity;
@Repository
public interface GroupDetailsRepository extends JpaRepository<GroupDetails, Long> {
	List<GroupDetails> findAllByUserId(String userId);

	List<GroupDetails> findAllById(Long groupId);

}
