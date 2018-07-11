package com.ojas.ticket.dao.issuetype;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import com.ojas.ticket.dao.JpaDao;
import com.ojas.ticket.entity.Department;
import com.ojas.ticket.entity.IssueType;

public class JpaIssueTypeDao extends JpaDao<IssueType, Long> implements IssueTypeDao{

	public JpaIssueTypeDao() {
		super(IssueType.class);
	}

	@Override
	public List<String> getIssueTypeByDepartment(String department) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<String> query = builder.createQuery(String.class);
		final Root<IssueType> root = query.from(IssueType.class);
		Join<IssueType,Department> join = root.join("department");
		query.select(root.get("issueType")).where(builder.equal(join.get("deptName"), department));
		TypedQuery<String> typedQuery = this.getEntityManager().createQuery(query);
		List<String> issueType = typedQuery.getResultList();
		return issueType;
	}

}
