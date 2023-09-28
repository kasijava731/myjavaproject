package com.e2s.cam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e2s.cam.entity.CommunityUserProfiles;

@Repository
public interface CommunityUserProfilesRepository extends JpaRepository<CommunityUserProfiles, Long>{
 
	CommunityUserProfiles findByUserId(String userId);
}
