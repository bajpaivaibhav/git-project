package com.sociopool.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sociopool.registration.dto.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String name);
}
