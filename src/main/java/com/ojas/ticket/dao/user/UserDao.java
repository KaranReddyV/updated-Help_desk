package com.ojas.ticket.dao.user;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ojas.ticket.dao.Dao;
import com.ojas.ticket.entity.AccessToken;
import com.ojas.ticket.entity.Department;
import com.ojas.ticket.entity.Response;
import com.ojas.ticket.entity.User;

public interface UserDao extends Dao<User, Long> {

	User loadUserByUsername(String username) throws UsernameNotFoundException;

	User findByName(String name);

	User findByEmailAndPassword(String email);

	User findByEmail(String email);

	List<User> getAdmin();

	User findByOTP(String otp);

	AccessToken getUserByToken(String token);

	List<User> getEMailByDepartment(Department department);

	List<String> getEmailsWithinDepartment(String deptName);


	Response getDepartmentByEMail(String email);


	

}