package com.ojas.ticket.dao.issuetype;

import java.util.List;

import com.ojas.ticket.dao.Dao;
import com.ojas.ticket.entity.IssueType;

public interface IssueTypeDao extends Dao<IssueType, Long>{

	List<String> getIssueTypeByDepartment(String department);

}
