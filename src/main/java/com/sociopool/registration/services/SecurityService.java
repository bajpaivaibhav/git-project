package com.sociopool.registration.services;

public interface SecurityService {

		String findLoggedInUserName();
		void autoLogin(String username, String passsword);	

}
