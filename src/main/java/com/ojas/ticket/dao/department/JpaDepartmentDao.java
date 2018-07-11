package com.ojas.ticket.dao.department;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;

import com.ojas.ticket.dao.JpaDao;
import com.ojas.ticket.dao.issuetype.IssueTypeDao;
import com.ojas.ticket.entity.Department;
import com.ojas.ticket.entity.Response;

public class JpaDepartmentDao extends JpaDao<Department, Long> implements DepartmentDao {

	public JpaDepartmentDao() {
		super(Department.class);
	}

	@Autowired
	private IssueTypeDao issueTypeDao;

	@Override
	public List<Department> findAll() {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Department> query = builder.createQuery(Department.class);
		// final Root<Department> root = query.from(Department.class);
		query.from(Department.class);
		TypedQuery<Department> typedQuery = this.getEntityManager().createQuery(query);
		List<Department> departmentList = typedQuery.getResultList();
		return departmentList;
	}

	@Override
	public Response getEMailByDepartment(String department) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<String> query = builder.createQuery(String.class);
		final Root<Department> root = query.from(Department.class);
		List<String> issueList = new ArrayList<String>();
		String email = null;
		query.select(root.get("commonEmail")).where(builder.equal(root.get("deptName"), department));
		TypedQuery<String> typedQuery = this.getEntityManager().createQuery(query);
		List<String> commonEmail = typedQuery.getResultList();
		if (!commonEmail.isEmpty()) {
			email = commonEmail.iterator().next();
			issueList = issueTypeDao.getIssueTypeByDepartment(department);
			return new Response(email, issueList);
		} else {
			return new Response(email, issueList);
		}
	}

	@Override
	public List<String> getDepartment() {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<String> query = builder.createQuery(String.class);
		final Root<Department> root = query.from(Department.class);
		query.select(root.get("deptName"));
		TypedQuery<String> typedQuery = this.getEntityManager().createQuery(query);
		List<String> deptList = typedQuery.getResultList();
		return deptList;
	}

	@Override
	public Department findByDepartment(String department) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Department> query = builder.createQuery(Department.class);
		final Root<Department> root = query.from(Department.class);
		query.where(builder.equal(root.get("deptName"), department));
		TypedQuery<Department> typedQuery = this.getEntityManager().createQuery(query);
		List<Department> deptList = typedQuery.getResultList();
		return deptList.iterator().next();
	}

	@Override
	public String getdeptNameByCommonMail(String assignTo) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<String> query = builder.createQuery(String.class);
		final Root<Department> root = query.from(Department.class);
		query.select(root.get("deptName")).where(builder.equal(root.get("commonEmail"), assignTo));
		TypedQuery<String> typedQuery = this.getEntityManager().createQuery(query);
		List<String> deptNameList = typedQuery.getResultList();
		if (deptNameList.isEmpty()) {
			return null;
		} else {
			return deptNameList.iterator().next();
		}
	}

	@Override
	public Department getDeptByemailOrName(String commonEmail, String deptName) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Department> criteriaQuery = builder.createQuery(Department.class);
		final Root<Department> root = criteriaQuery.from(Department.class);
		Predicate p = builder.or(builder.equal(root.get("commonEmail"), commonEmail),
				builder.equal(root.get("deptName"), deptName));
		criteriaQuery.where(p).select(root);
		TypedQuery<Department> typedQuery = getEntityManager().createQuery(criteriaQuery);
		List<Department> deptList = typedQuery.getResultList();
		if (deptList.isEmpty()) {
			return null;
		} else
			return deptList.iterator().next();
	}

}
