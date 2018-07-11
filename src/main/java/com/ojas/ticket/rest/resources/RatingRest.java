package com.ojas.ticket.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.ojas.bean.RatingBean;
import com.ojas.ticket.Ticket.TicketDao;
import com.ojas.ticket.entity.Rating;
import com.ojas.ticket.entity.Ticket;
import com.ojas.ticket.exception.TicketsNotFoundException;
import com.ojas.ticket.service.RatingService;

@Component
@Path("/rating")
public class RatingRest {

	@Autowired
	private RatingService ratingService;

	@Autowired
	private TicketDao ticketDao;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/save/{id}")
	public Rating saveRating(@RequestBody RatingBean ratingBean, @PathParam("id") Long id) {
		Rating rating = null;
		try {
			rating = new Rating();
			rating.setTicket(ticketDao.find(id));
			/* rating.setTicket(ticketDao.find(id)); */
			if (null != id) {
				rating = new Rating();
				Ticket ticket = ticketDao.find(id);
				ticket.setComment(ticket.getComment() + "\n" + ratingBean.getComment());
				ticketDao.save(ticket);
				rating.setTicket(ticket);
				rating.setRatingNo(ratingBean.getRatingNo());
			}
			rating.setRatingNo(ratingBean.getRatingNo());
		} catch (TicketsNotFoundException e) {
			e.printStackTrace();
		}

		return ratingService.save(rating);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Rating getRating(@PathParam("id") Long id) {
		Rating rating = null;

		if (null != id) {
			rating = ratingService.getRating(id);
		}

		return rating;

	}

}
