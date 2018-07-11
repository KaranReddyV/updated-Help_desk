package com.ojas.ticket.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonFormat;

@javax.persistence.Entity
public class Ticket implements Entity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;


	@Column
	private String project;

	@ManyToOne
	@JoinColumn(name = "User_id")
	private User User;

	@Column
	private String descNo;

	/*
	 * @Column
	 * 
	 * @Enumerated(EnumType.STRING) private Department department;
	 */

	@Column
	private String issueType;

	@Column
	private String comment;

	@Column
	private String description;

	@Column
	@Enumerated(EnumType.STRING)
	private TicketPriority priority;

	@Column
	@JsonFormat(pattern = "YYYY-MM-dd")
	private Date createdDate;

	@Column
	@JsonFormat(pattern = "YYYY-MM-dd")
	private Date updatedDate;

	@Column
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dueDate;

	@Column
	@Enumerated(EnumType.STRING)
	private TicketStatus status;

	@Column
	private String note;
	@Lob
	@Column
	private byte[] attachment;

	@Column
	private String fileName;

	@Column
	private String assignTo;

	@Column
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDescNo() {
		return descNo;
	}

	public void setDescNo(String descNo) {
		this.descNo = descNo;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(String assignTo) {
		this.assignTo = assignTo;
	}

	public Long getId() {
		return this.id;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public User getUser() {
		return User;
	}

	public void setUser(User user) {
		User = user;
	}

	/*
	 * public Department getDepartment() { return department; }
	 * 
	 * public void setDepartment(Department department) { this.department =
	 * department; }
	 */

	
	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TicketPriority getPriority() {
		return priority;
	}

	public void setPriority(TicketPriority priority) {
		this.priority = priority;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public TicketStatus getStatus() {
		return status;
	}

	public void setStatus(TicketStatus status) {
		this.status = status;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", project=" + project + ", User=" + User + ", descNo=" + descNo + ", department="
				+ /* department + */ ", issueType=" + issueType + ", comment=" + comment + ", description="
				+ description + ", priority=" + priority + ", createdDate=" + createdDate + ", updatedDate="
				+ updatedDate + ", dueDate=" + dueDate + ", status=" + status + ", note=" + note + ", attachment="
				+ Arrays.toString(attachment) + ", fileName=" + fileName + ", assignTo=" + assignTo + "]";
	}

	

}