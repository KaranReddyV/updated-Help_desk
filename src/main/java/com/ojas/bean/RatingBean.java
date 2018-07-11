package com.ojas.bean;

import java.io.Serializable;

import com.ojas.ticket.entity.Ticket;

public class RatingBean implements Serializable {

	public RatingBean() {
		super();
	}

	private Long id;

	private String ratingNo;
	private String comment;

	private Ticket ticket;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRatingNo() {
		return ratingNo;
	}

	public void setRatingNo(String ratingNo) {
		this.ratingNo = ratingNo;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	/**
	 * @param id
	 * @param ratingNo
	 * @param ticket
	 */
	public RatingBean(Long id, String ratingNo, Ticket ticket) {
		super();
		this.id = id;
		this.ratingNo = ratingNo;
		this.ticket = ticket;
	}

}
