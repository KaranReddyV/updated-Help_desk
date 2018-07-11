package com.ojas.ticket.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ojas.ticket.entity.AccessToken;
import com.ojas.ticket.entity.Department;
import com.ojas.ticket.entity.User;

/**
 * @author Philip Washington Sorst <philip@sorst.net>
 */
public interface UserService extends UserDetailsService {
	User findUserByAccessToken(String accessToken);

	AccessToken createAccessToken(User user);

	User findByEmail(String username) throws UsernameNotFoundException;

	User save(User newUser);
    
    List<User> getEMailByDepartment(Department department);
}
