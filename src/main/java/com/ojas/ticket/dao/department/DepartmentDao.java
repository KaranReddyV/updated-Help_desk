package com.ojas.ticket.dao.department;

import java.util.List;

import com.ojas.ticket.dao.Dao;
import com.ojas.ticket.entity.Department;
import com.ojas.ticket.entity.Response;

public interface DepartmentDao extends Dao<Department, Long>{
	
	List<Department> findAll();

	Response getEMailByDepartment(String department);

	Department findByDepartment(String department);

	String getdeptNameByCommonMail(String assignTo);

	List<String> getDepartment();

	Department getDeptByemailOrName(String commonEmail, String deptName);


}
