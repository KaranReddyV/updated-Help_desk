package com.ojas.ticket.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class IssueType implements com.ojas.ticket.entity.Entity{
	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String issueType;

	
	@ManyToOne
	private Department department;

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	@Override
	public String toString() {
		return "IssueType [id=" + id + ", issueType=" + issueType + ", department=" + department + "]";
	}

}
