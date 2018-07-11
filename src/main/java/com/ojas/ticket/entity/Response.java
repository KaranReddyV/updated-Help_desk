package com.ojas.ticket.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.ojas.bean.UserBean;

public class Response {

	public Response() {
		super();
	}

	private String status;
	private HttpStatus errorCode;
	private String errorMessage;
	private Long count;
	private Long pages;
	private AccessToken accessToken;
	private List<Role> roles;
	private UserBean bean;
	private Ticket ticket;
	private List<User> userList = new ArrayList<User>();
	private Department department;
	private List<String> issueList = new ArrayList<String>();
	private String email;
	private String deptName;
	private Object result;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getIssueList() {
		return issueList;
	}

	public void setIssueList(List<String> issueList) {
		this.issueList = issueList;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public UserBean getBean() {
		return bean;
	}

	public void setBean(UserBean bean) {
		this.bean = bean;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public HttpStatus getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(HttpStatus errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Response(String status, HttpStatus errorCode, String errorMessage, Object result, Long count, Long pages) {
		super();
		this.status = status;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;

		this.result = result;
		this.count = count;
		this.pages = pages;
	}

	public Response(HttpStatus errorCode, User newUser) {
		this.errorCode = errorCode;
		this.result = newUser;
	}

	public Response(String status) {
		this.status = status;
	}

	public Response(AccessToken accessToken, List<Role> roles) {
		this.accessToken = accessToken;
		this.roles = roles;
	}

	public Response(HttpStatus ok, Long listTotal, List<Ticket> sublist, Long noOfPages) {
		this.errorCode = ok;
		this.count = listTotal;
		this.result = sublist;
		this.pages = noOfPages;
	}

	public Response(HttpStatus noContent, Long noOFRecords, List<Ticket> userList, Long noOfPages, String string) {
		this.errorCode = noContent;
		this.count = noOFRecords;
		this.result = userList;
		this.pages = noOfPages;
		this.status = string;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Long getPages() {
		return pages;
	}

	public void setPages(Long pages) {
		this.pages = pages;
	}

	public Response(UserBean bean, String status) {
		this.bean = bean;
		this.status = status;
	}

	public Response(AccessToken token, String string) {
		this.accessToken = token;
		this.errorMessage = string;
	}

	public Response(Ticket ticket, HttpStatus errorCode) {
		this.ticket = ticket;
		this.errorCode = errorCode;
	}

	public Response(HttpStatus errorCode, List<User> userList) {
		this.userList = userList;
		this.errorCode = errorCode;
	}

	public Response(List<Ticket> ticket, HttpStatus status) {
		this.result = ticket;
		this.errorCode = status;
	}

	public Response(Ticket ticket, HttpStatus errorCode, Department department) {
		this.ticket = ticket;
		this.errorCode = errorCode;
		this.department = department;
	}

	public Response(HttpStatus errorCode) {
		this.errorCode = errorCode;

	}

	public Response(Object result, HttpStatus errorCode) {
		this.result = result;
		this.errorCode = errorCode;

	}

	public Response(String email, List<String> issueList) {
		this.email = email;
		this.issueList = issueList;
	}
	
}
