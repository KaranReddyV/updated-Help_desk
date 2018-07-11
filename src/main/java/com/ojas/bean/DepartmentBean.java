package com.ojas.bean;

import java.io.Serializable;

public class DepartmentBean implements Serializable {

	private Long id;
	private String deptName;
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

}
