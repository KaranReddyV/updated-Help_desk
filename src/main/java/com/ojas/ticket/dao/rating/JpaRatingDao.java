package com.ojas.ticket.dao.rating;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Transactional;

import com.ojas.ticket.dao.JpaDao;
import com.ojas.ticket.entity.Rating;
import com.ojas.ticket.entity.Ticket;
import com.ojas.ticket.entity.User;

public class JpaRatingDao extends JpaDao<Rating, Long> implements RatingDao {

	public JpaRatingDao() {
		super(Rating.class);
		
	}

	@Override
	@Transactional
	public Rating gatTicketRating(Ticket ticket) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Rating> criteriaQuery = builder.createQuery(this.entityClass);
		Root<Rating> root = criteriaQuery.from(this.entityClass);
		Join<Rating, Ticket> join = root.join("Ticket");
		criteriaQuery.where(builder.equal(join.get("id"), ticket.getId()));
		TypedQuery<Rating> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
		List<Rating> rating = typedQuery.getResultList();
		if (rating.isEmpty()) {
			return null;
		}
		return rating.iterator().next();
		
	}

}
