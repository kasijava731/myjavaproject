package com.e2s.cam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e2s.cam.entity.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
	public Staff findByStaffId(String userId);
}
