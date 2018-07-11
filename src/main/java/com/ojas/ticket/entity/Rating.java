package com.ojas.ticket.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Rating implements com.ojas.ticket.entity.Entity {

	@Id
	@GeneratedValue
	private Long id;
	@Column
	private String ratingNo;
/*    @Column
	private String comments;*/

	@OneToOne(cascade = {CascadeType.ALL})

	@JoinColumn(name="Ticket_id")
	private Ticket Ticket;

	public Ticket getTicket() {
		return Ticket;
	}

	public void setTicket(Ticket ticket) {
		this.Ticket = ticket;
	}

	public String getRatingNo() {
		return ratingNo;
	}

	public void setRatingNo(String ratingNo) {
		this.ratingNo = ratingNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

/*	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}*/

}
