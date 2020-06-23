package com.sociopool.registration.services;

import com.sociopool.registration.dto.User;

public interface UserService {

	void save(User user);

	User findByUsername(String username);
}
