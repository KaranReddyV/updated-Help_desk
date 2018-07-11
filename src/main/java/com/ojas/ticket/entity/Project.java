/*package com.ojas.ticket.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@javax.persistence.Entity
public class Project implements Entity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String projectName;

	@Column
	private Date addingdate;

	@Column
	private Date complitionDate;

	@Column
	private String description;

	public Project() {
		this.addingdate = new Date();
	}

	public Long getId() {

		return this.id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Date getAddingdate() {
		return addingdate;
	}

	public void setAddingdate(Date addingdate) {
		this.addingdate = addingdate;
	}

	public Date getComplitionDate() {
		return complitionDate;
	}

	public void setComplitionDate(Date complitionDate) {
		this.complitionDate = complitionDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", projectName=" + projectName + ", addingdate=" + addingdate + ", complitionDate="
				+ complitionDate + ", description=" + description + "]";
	}

}
*/