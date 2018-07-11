package com.ojas.ticket.rest.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.ojas.ticket.dao.department.DepartmentDao;
import com.ojas.ticket.dao.issuetype.IssueTypeDao;
import com.ojas.ticket.entity.IssueType;

@Component
@Path("issue")
public class IssueTypeRest {

	@Autowired
	private IssueTypeDao issueTypeDao;

	@Autowired
	private DepartmentDao departmentDao;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("issuetype_by_dept/{dept}")
	public ResponseEntity<List<String>> getIssueTypeByDepartment(@PathParam("dept") String department) {
		List<String> issueTypeList = issueTypeDao.getIssueTypeByDepartment(department);
		if (issueTypeList.isEmpty()) {
			return new ResponseEntity<List<String>>(HttpStatus.NO_CONTENT);
		} else
			return new ResponseEntity<List<String>>(issueTypeList, HttpStatus.OK);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("add_issue/{deptid}")
	public ResponseEntity<HttpStatus> addIssue(@RequestBody IssueType issueType, @PathParam("deptid") Long deptid) {
		IssueType issue = new IssueType();
		issue.setIssueType(issueType.getIssueType());
		issue.setDepartment(departmentDao.find(deptid));
		issueTypeDao.save(issue);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("edit_issuetype")
	public ResponseEntity<HttpStatus> updateIssue(@RequestBody IssueType issueType) {
		IssueType issue = issueTypeDao.find(issueType.getId());
		issue.setIssueType(issueType.getIssueType());
		issueTypeDao.save(issue);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}

}
