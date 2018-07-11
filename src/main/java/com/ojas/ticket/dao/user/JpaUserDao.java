package com.ojas.ticket.dao.user;

import java.util.Arrays;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.ojas.ticket.dao.JpaDao;
import com.ojas.ticket.entity.AccessToken;
import com.ojas.ticket.entity.Department;
import com.ojas.ticket.entity.Response;
import com.ojas.ticket.entity.Role;
import com.ojas.ticket.entity.User;
import com.ojas.ticket.exception.UserNotFoundException;

public class JpaUserDao extends JpaDao<User, Long> implements UserDao {

	public JpaUserDao() {
		super(User.class);
	}

	@Override
	@Transactional(readOnly = true)
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.findByName(username);
		if (null == user) {
			throw new UsernameNotFoundException("The user with name " + username + " was not found");
		}

		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public User findByName(String name) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<User> criteriaQuery = builder.createQuery(this.entityClass);
		Root<User> root = criteriaQuery.from(this.entityClass);
		Path<String> namePath = root.get("email");
		criteriaQuery.where(builder.equal(namePath, name));
		TypedQuery<User> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
		List<User> users = typedQuery.getResultList();
		if (users.isEmpty()) {
			return null;
		}
		return users.iterator().next();
	}

	@Override
	public User findByEmailAndPassword(String email) {
		final CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<User> criteriaQuery = builder.createQuery(this.entityClass);

		Root<User> users = criteriaQuery.from(this.entityClass);
		criteriaQuery.select(users);
		criteriaQuery.where(builder.equal(users.get("email"), email));
		TypedQuery<User> typedQuery = getEntityManager().createQuery(criteriaQuery);
		List<User> usersList = typedQuery.getResultList();

		if (usersList.isEmpty()) {
			return null;
		}

		return usersList.iterator().next();

	}

	@Override
	public User findByEmail(String email) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
		Root<User> user = criteriaQuery.from(User.class);
		criteriaQuery.select(user).where(builder.equal(user.get("email"), email));
		TypedQuery<User> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
		List<User> userList = typedQuery.getResultList();
		if (userList.isEmpty())
			return null;
		else
			return userList.iterator().next();
	}

	@Override
	public List<User> getAdmin() {
		CriteriaQuery<User> query = this.getEntityManager().getCriteriaBuilder().createQuery(User.class);
		query.where(query.from(User.class).join("roles").in(Arrays.asList(Role.ADMIN)));
		List<User> result = getEntityManager().createQuery(query).getResultList();
		return result;
	}

	@Override
	public User findByOTP(String otp) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
		Root<User> user = criteriaQuery.from(User.class);
		criteriaQuery.select(user).where(builder.equal(user.get("otp"), otp));
		TypedQuery<User> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
		List<User> userList = typedQuery.getResultList();
		if (userList.isEmpty())
			return null;
		else
			return userList.iterator().next();
	}

	@Override
	public AccessToken getUserByToken(String token) {
		final CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<AccessToken> criteriaQuery = builder.createQuery(AccessToken.class);
		Root<AccessToken> accessToken = criteriaQuery.from(AccessToken.class);
		criteriaQuery.select(accessToken);
		criteriaQuery.where(builder.equal(accessToken.get("token"), token));
		TypedQuery<AccessToken> typedQuery = getEntityManager().createQuery(criteriaQuery);
		List<AccessToken> usersList = typedQuery.getResultList();
		if (usersList.isEmpty()) {
			return null;
		}
		return usersList.iterator().next();
	}

	@Override
	public List<User> getEMailByDepartment(Department department) throws UserNotFoundException {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
		Root<User> user = criteriaQuery.from(User.class);
		Predicate join = user.join("roles").in(Arrays.asList(Role.ADMIN));
		criteriaQuery.select(user).where(builder.equal(user.get("department"), department), join);
		List<User> result = getEntityManager().createQuery(criteriaQuery).getResultList();
		return result;
	}

	@Override
	public List<String> getEmailsWithinDepartment(String deptName) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<String> query = builder.createQuery(String.class);
		final Root<User> root = query.from(User.class);
		Join<User, Department> join = root.join("department");
		query.select(root.get("email")).where(builder.equal(join.get("deptName"), deptName));
		TypedQuery<String> typedQueyList = getEntityManager().createQuery(query);
		List<String> emailList = typedQueyList.getResultList();
		return emailList;
	}

	@Override
	public Response getDepartmentByEMail(String email) {
		final CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
		Root<User> dRoot = criteriaQuery.from(User.class);
		Join<User, Department> join = dRoot.join("department");
		criteriaQuery.select(join.on(builder.equal(dRoot.get("email"), email)).get("deptName"))
				.where(builder.equal(dRoot.get("email"), email));
		List<User> user=getEntityManager().createQuery(criteriaQuery).getResultList();
		return new Response(user, HttpStatus.OK);
	}


	/*@Override
	public javax.ws.rs.core.Response getDepartmentByEMail(String email) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);
		final Root<User> root = criteriaQuery.from(User.class);
		Join<User, Department> join = root.join("department");
		criteriaQuery.select(join.get("deptName")).where(builder.equal(root.get("email"), email));
		TypedQuery<String> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
		List<String> deptNameList = typedQuery.getResultList();
		return javax.ws.rs.core.Response.status(Status.OK).entity(deptNameList).build();
	}*/

	

}