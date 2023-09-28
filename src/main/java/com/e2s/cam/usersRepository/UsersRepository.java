package com.e2s.cam.usersRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e2s.cam.userentity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
	Users findByIdAndOrganizationId(Long userId,Long orgId);
}
