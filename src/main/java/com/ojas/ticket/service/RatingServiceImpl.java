package com.ojas.ticket.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.ojas.ticket.Ticket.TicketDao;
import com.ojas.ticket.dao.rating.RatingDao;
import com.ojas.ticket.entity.Rating;
import com.ojas.ticket.entity.Ticket;

public class RatingServiceImpl implements RatingService {
	@Autowired
	private RatingDao ratingDao;
	@Autowired
	private TicketDao ticketDao;

	protected RatingServiceImpl() {

	}

	@Override
	public Rating save(Rating rating) {

		return ratingDao.save(rating);
	}
	@Override
	public Rating getRating(Long id) {
		Ticket ticket= ticketDao.find(id);
		return ratingDao.gatTicketRating(ticket);
	}
}
