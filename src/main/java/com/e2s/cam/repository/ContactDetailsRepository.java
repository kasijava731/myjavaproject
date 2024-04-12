	package com.e2s.cam.repository;
	
	import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e2s.cam.entity.ContactDetails;
	@Repository
	public interface ContactDetailsRepository extends JpaRepository<ContactDetails, Long>{
		
		List<ContactDetails> findAllByUserIdAndType(String userId,String type);
	
	}
