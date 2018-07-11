package com.ojas.ticket.dao.rating;

import com.ojas.ticket.dao.Dao;
import com.ojas.ticket.entity.Rating;
import com.ojas.ticket.entity.Ticket;

public interface RatingDao extends Dao<Rating, Long> {

	Rating gatTicketRating(Ticket ticket);
}
