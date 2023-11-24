package com.e2s.cam.usersRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.e2s.cam.userentity.Users;

import jakarta.transaction.Transactional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
	Users findByIdAndOrganizationId(Long userId,Long orgId);
	 @Modifying
	    @Transactional
	    @Query("UPDATE Users e SET e.status= :status WHERE e.id=:id and e.organizationId = :orgId ")
	    void updateName(@Param("orgId") Long orgId, @Param("id") Long id, @Param("status") String status);
}
