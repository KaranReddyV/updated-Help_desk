package com.ojas.ticket.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Department implements com.ojas.ticket.entity.Entity {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String deptName;
	@Column
	private String commonEmail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getCommonEmail() {
		return commonEmail;
	}

	public void setCommonEmail(String commonEmail) {
		this.commonEmail = commonEmail;
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", department=" + deptName + ", commonEmail=" + commonEmail + "]";
	}

}
