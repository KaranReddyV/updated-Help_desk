package com.ojas.ticket.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.ojas.bean.UserBean;
import com.ojas.ticket.dao.accesstoken.AccessTokenDao;
import com.ojas.ticket.dao.user.JpaUserDao;
import com.ojas.ticket.dao.user.UserDao;
import com.ojas.ticket.entity.AccessToken;
import com.ojas.ticket.entity.Department;
import com.ojas.ticket.entity.User;
import com.ojas.ticket.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * @author Philip Washington Sorst <philip@sorst.net>
 */
public class DaoUserService implements UserService {
	private UserDao userDao;

	private AccessTokenDao accessTokenDao;

	private JpaUserDao jpaUserDao;

	protected DaoUserService() {
		/* Reflection instantiation */
	}

	public DaoUserService(UserDao userDao, AccessTokenDao accessTokenDao) {
		this.userDao = userDao;
		this.accessTokenDao = accessTokenDao;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.userDao.loadUserByUsername(username);
	}

	@Override
	@Transactional
	public User findUserByAccessToken(String accessTokenString) {
		AccessToken accessToken = this.accessTokenDao.findByToken(accessTokenString);

		if (null == accessToken) {
			return null;
		}

		if (accessToken.isExpired()) {
			this.accessTokenDao.delete(accessToken);
			return null;
		}

		return accessToken.getUser();
	}

	@Override
	@Transactional
	public AccessToken createAccessToken(User user) {
		AccessToken accessToken = new AccessToken(user, UUID.randomUUID().toString());
		return this.accessTokenDao.save(accessToken);
	}

	@Override
	public User findByEmail(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return jpaUserDao.findByEmail(username);
	}

	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		return user;
	}


	@Override
	public List<User> getEMailByDepartment(Department department) {
		List<User> user=null;
		 try {
			user = this.userDao.getEMailByDepartment(department);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		return user;
	}
}
