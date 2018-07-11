package com.ojas.ticket.Ticket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import com.ojas.ticket.dao.JpaDao;
import com.ojas.ticket.entity.Response;
import com.ojas.ticket.entity.Role;
import com.ojas.ticket.entity.Ticket;
import com.ojas.ticket.entity.TicketStatus;
import com.ojas.ticket.entity.User;
import com.ojas.ticket.exception.TicketsNotFoundException;

public class JpaTicketDao extends JpaDao<Ticket, Long> implements TicketDao {

	public JpaTicketDao() {
		super(Ticket.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Ticket> findAll() {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Ticket> criteriaQuery = builder.createQuery(Ticket.class);
		Root<Ticket> root = criteriaQuery.from(Ticket.class);
		criteriaQuery.orderBy(builder.desc(root.get("id")));
		TypedQuery<Ticket> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

	@Override
	public Response getTicketById(User user, Long ticketId) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Ticket> criteriaQuery = builder.createQuery(Ticket.class);
		Root<Ticket> tickets = criteriaQuery.from(Ticket.class);
		if (user.getRoles().contains(Role.ADMIN)) {
			criteriaQuery.select(tickets).where(
					builder.equal(tickets.get("assignTo"), user.getDepartment().getCommonEmail()),
					builder.equal(tickets.get("id"), ticketId));
		} else {
			Join<Ticket, User> join = tickets.join("User");
			criteriaQuery.select(tickets).where(builder.equal(join.get("id"), user.getId()),
					builder.equal(tickets.get("id"), ticketId));
		}
		TypedQuery<Ticket> typedQuery = getEntityManager().createQuery(criteriaQuery);
		List<Ticket> ticket = typedQuery.getResultList();
		if (!ticket.isEmpty())
			return new Response(ticket, HttpStatus.OK);
		else
			return new Response(HttpStatus.NO_CONTENT);
	}

	@Override
	public Response filterTickets(String column, String filter, User user, Long pageNo, Long pageSize) {
		Response response = this.getFilterPageSizeAndPageNumber(user, pageNo, pageSize, filter, column);
		List<Ticket> filterticketsList = null;
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Ticket> criteriaQuery = builder.createQuery(Ticket.class);
		Root<Ticket> tickets = criteriaQuery.from(Ticket.class);
		Predicate predicate = null;
		if (column.equals("createdDate")) {
			predicate = builder.like(tickets.get("createdDate").as(String.class), "%" + filter + "%");
		} else if (column.equals("priority")) {
			predicate = builder.like(tickets.get("priority").as(String.class), "%" + filter + "%");
		} else if (column.equals("status")) {
			if (!filter.equals("All")) {
				predicate = builder.like(tickets.get("status").as(String.class), "%" + filter + "%");
			} else {
				predicate = null;
			}
		} else if (column.equals("id")) {
			predicate = builder.like(tickets.get("id").as(String.class), "%" + filter + "%");
		} else {
			predicate = builder.like(tickets.get("comment").as(String.class), "%" + filter + "%");
		}
		if (!(predicate == null)) {
			if (user.getRoles().contains(Role.ADMIN)) {
				criteriaQuery.select(tickets)
						.where(builder.equal(tickets.get("assignTo"), user.getDepartment().getCommonEmail()), predicate)
						.orderBy(builder.desc(tickets.get("id")));
			} else {
				Join<Ticket, User> join = tickets.join("User");
				criteriaQuery.select(tickets).where(builder.equal(join.get("id"), user.getId()), predicate)
						.orderBy(builder.desc(tickets.get("id")));
			}
		} else {
			if (user.getRoles().contains(Role.ADMIN)) {
				criteriaQuery.select(tickets)
						.where(builder.equal(tickets.get("assignTo"), user.getDepartment().getCommonEmail()))
						.orderBy(builder.desc(tickets.get("id")));
			} else {
				Join<Ticket, User> join = tickets.join("User");
				criteriaQuery.select(tickets).where(builder.equal(join.get("id"), user.getId()))
						.orderBy(builder.desc(tickets.get("id")));
			}
		}
		TypedQuery<Ticket> typedQuery = getEntityManager().createQuery(criteriaQuery);
		typedQuery.setFirstResult((pageNo.intValue() - 1) * pageSize.intValue());
		typedQuery.setMaxResults(pageSize.intValue());
		filterticketsList = typedQuery.getResultList();
		if (!filterticketsList.isEmpty())
			return new Response(HttpStatus.OK, response.getCount(), filterticketsList, response.getPages());
		else
			return new Response(HttpStatus.NO_CONTENT);
	}

	@Override
	public Response getUpdatedTickets(User user, Long pageNo, Long pageSize) {
		Response response = this.getPageSizeAndPageNumber(user, pageNo, pageSize);
		List<Ticket> updatedTicketList = null;
		final CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Ticket> criteriaQuery = builder.createQuery(Ticket.class);
		Root<Ticket> tickets = criteriaQuery.from(Ticket.class);
		if (user.getRoles().contains(Role.ADMIN)) {
			criteriaQuery.select(tickets).where(
					builder.equal(tickets.get("assignTo"), user.getDepartment().getCommonEmail()),
					builder.lessThanOrEqualTo(tickets.get("updatedDate"), new Date()));
		} else {
			Join<Ticket, User> join = tickets.join("User");
			criteriaQuery.select(tickets).where(builder.equal(join.get("id"), user.getId()),
					builder.lessThanOrEqualTo(tickets.get("updatedDate"), new Date()));
		}
		TypedQuery<Ticket> typedQuery = getEntityManager().createQuery(criteriaQuery);
		typedQuery.setFirstResult((pageNo.intValue() - 1) * pageSize.intValue());
		typedQuery.setMaxResults(pageSize.intValue());
		try {
			updatedTicketList = typedQuery.getResultList();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		if (!updatedTicketList.isEmpty())
			return new Response(HttpStatus.OK, response.getCount(), updatedTicketList, response.getPages());
		else
			return new Response(HttpStatus.NO_CONTENT);
	}

	@Override
	@Transactional(readOnly = true)
	public Response getAdminAssignedIssues(User user, Long pageNo, Long pageSize) {
		List<Ticket> adminTicketList = null;
		final CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		Response response = getPageSizeAndPageNumber(user, pageNo, pageSize);
		final CriteriaQuery<Ticket> criteriaQuery = builder.createQuery(Ticket.class);
		Root<Ticket> tickets = criteriaQuery.from(Ticket.class);
		criteriaQuery.select(tickets)
				.where(builder.equal(tickets.get("assignTo"), user.getDepartment().getCommonEmail()))
				.orderBy(builder.desc(tickets.get("createdDate")));
		TypedQuery<Ticket> typedQuery = getEntityManager().createQuery(criteriaQuery);
		typedQuery.setFirstResult((pageNo.intValue() - 1) * pageSize.intValue());
		typedQuery.setMaxResults(pageSize.intValue());
		adminTicketList = typedQuery.getResultList();
		if (!adminTicketList.isEmpty()) {
			return new Response(HttpStatus.OK, response.getCount(), adminTicketList, response.getPages());
		} else {
			return new Response(HttpStatus.NO_CONTENT);
		}
	}

	// user methods from here

	@Override
	@Transactional(readOnly = true)
	public Response getUserTickets(User user, Long pageNo, Long pageSize) {
		List<Ticket> userTicket = null;
		final CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
		Response response = getPageSizeAndPageNumber(user, pageNo, pageSize);
		final CriteriaQuery<Ticket> criteriaQuery = criteriaBuilder.createQuery(Ticket.class);
		Root<Ticket> tickets = criteriaQuery.from(Ticket.class);
		Join<Ticket, User> joins = tickets.join("User");
		criteriaQuery.select(tickets).where(criteriaBuilder.equal(joins.get("id"), user
				.getId()))/*
							 * .orderBy(
							 * criteriaBuilder.desc(tickets.get("createdDate")),
							 * criteriaBuilder.desc(tickets.get("updatedDate")))
							 */;
		TypedQuery<Ticket> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
		typedQuery.setFirstResult((pageNo.intValue() - 1) * pageSize.intValue());
		typedQuery.setMaxResults(pageSize.intValue());
		userTicket = typedQuery.getResultList();
		if (!userTicket.isEmpty()) {
			return new Response(HttpStatus.OK, response.getCount(), userTicket, response.getPages());
		} else {
			return new Response(HttpStatus.NO_CONTENT);
		}

	}

	private Response getFilterPageSizeAndPageNumber(User user, Long pageNo, Long pageSize, String filter,
			String column) {

		Long noOfRecords = null, noOfPages = null;
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Long> countCriteriaQuery = builder.createQuery(Long.class);
		Root<Ticket> root = countCriteriaQuery.from(Ticket.class);
		Predicate predicate = null;
		try {
			if (column.equals("description")) {
				predicate = builder.like(root.get("description").as(String.class), "%" + filter + "%");
			} else if (column.equals("priority")) {
				predicate = builder.like(root.get("priority").as(String.class), "%" + filter + "%");
			} else if (column.equals("status")) {
				if (!filter.equals("All")) {
					predicate = builder.like(root.get("status").as(String.class), "%" + filter + "%");
				} else {
					predicate = null;
				}
			} else if (column.equals("id")) {
				predicate = builder.like(root.get("id").as(String.class), "%" + filter + "%");
			} else {
				predicate = builder.like(root.get("comment").as(String.class), "%" + filter + "%");
			}
		} catch (NullPointerException e) {
			throw new TicketsNotFoundException();
		}
		if (!(predicate == null)) {
			if (user.getRoles().contains(Role.ADMIN)) {
				countCriteriaQuery.select(builder.count(root))
						.where(builder.equal(root.get("assignTo"), user.getDepartment().getCommonEmail()), predicate)
						.orderBy(builder.desc(root.get("id")));
			} else {
				Join<Ticket, User> join = root.join("User");
				countCriteriaQuery.select(builder.count(root))
						.where(builder.equal(join.get("id"), user.getId()), predicate)
						.orderBy(builder.desc(root.get("id")));
			}

		} else {
			if (user.getRoles().contains(Role.ADMIN)) {
				countCriteriaQuery.select(builder.count(root))
						.where(builder.equal(root.get("assignTo"), user.getDepartment().getCommonEmail()))
						.orderBy(builder.desc(root.get("id")));
			} else {
				Join<Ticket, User> join = root.join("User");
				countCriteriaQuery.select(builder.count(root)).where(builder.equal(join.get("id"), user.getId()))
						.orderBy(builder.desc(root.get("id")));
			}

		}
		noOfRecords = this.getEntityManager().createQuery(countCriteriaQuery).getSingleResult();
		if (null != noOfRecords && pageNo <= noOfRecords) {
			noOfPages = (noOfRecords / pageSize + ((noOfRecords % pageSize) > 0 ? 1 : 0));
			return new Response(HttpStatus.OK, noOfRecords, null, noOfPages);
		} else {
			return new Response(HttpStatus.NO_CONTENT);
		}
	}

	private Response getPageSizeAndPageNumber(User user, Long pageNo, Long pageSize) {
		Long noOfRecords = null, noOfPages = null;
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Long> countCriteriaQuery = builder.createQuery(Long.class);
		Root<Ticket> root = countCriteriaQuery.from(Ticket.class);
		if (user.getRoles().contains(Role.ADMIN)) {
			countCriteriaQuery.select(builder.count(root)).where(
					builder.equal(root.get("assignTo"), user.getDepartment().getCommonEmail()),
					builder.lessThanOrEqualTo(root.get("createdDate"), new Date()));
		} else {
			Join<Ticket, User> join = root.join("User");
			countCriteriaQuery.select(builder.count(root)).where(builder.equal(join.get("id"), user.getId()),
					builder.lessThanOrEqualTo(root.get("createdDate"), new Date()));
		}
		noOfRecords = this.getEntityManager().createQuery(countCriteriaQuery).getSingleResult();
		if (null != noOfRecords && pageNo <= noOfRecords) {
			noOfPages = (noOfRecords / pageSize + ((noOfRecords % pageSize) > 0 ? 1 : 0));
			return new Response(HttpStatus.OK, noOfRecords, null, noOfPages);
		} else {
			return new Response(HttpStatus.NO_CONTENT, noOfRecords, null, noOfPages);
		}
	}

	private Response getUpdatedPageSizeAndPageNumber(User user, Long pageNo, Long pageSize) {
		Long noOfRecords = null, noOfPages = null;
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Long> countCriteriaQuery = builder.createQuery(Long.class);
		Root<Ticket> root = countCriteriaQuery.from(Ticket.class);
		List<TicketStatus> status = new ArrayList<TicketStatus>();
		status.add(TicketStatus.Pending);
		status.add(TicketStatus.WorkInProgress);
		status.add(TicketStatus.Pendingclarification);
		status.add(TicketStatus.Draft);
		status.add(TicketStatus.Resolved);
		Expression<TicketStatus> exp = root.get("status");
		Predicate pr = exp.in(status);
		if (user.getRoles().contains(Role.ADMIN)) {
			countCriteriaQuery.select(builder.count(root)).where(
					builder.equal(root.get("assignTo"), user.getDepartment().getCommonEmail()), pr,
					builder.lessThanOrEqualTo(root.get("createdDate"), new Date()));
		} else {
			Join<Ticket, User> join = root.join("User");
			countCriteriaQuery.select(builder.count(root)).where(builder.equal(join.get("id"), user.getId()), pr,
					builder.lessThanOrEqualTo(root.get("createdDate"), new Date()));
		}
		noOfRecords = this.getEntityManager().createQuery(countCriteriaQuery).getSingleResult();
		if (null != noOfRecords && pageNo <= noOfRecords) {
			noOfPages = (noOfRecords / pageSize + ((noOfRecords % pageSize) > 0 ? 1 : 0));
			return new Response(HttpStatus.OK, noOfRecords, null, noOfPages);
		} else {
			return new Response(HttpStatus.NO_CONTENT, noOfRecords, null, noOfPages);
		}
	}

	@Override
	public Response getUpdatedStatusTicket(User user, Long pageNo, Long pageSize) {

		Response response = this.getUpdatedPageSizeAndPageNumber(user, pageNo, pageSize);
		List<Ticket> updatedTicketList = null;
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Ticket> criteriaQuery = builder.createQuery(Ticket.class);
		Root<Ticket> tickets = criteriaQuery.from(Ticket.class);
		List<TicketStatus> status = new ArrayList<TicketStatus>();
		status.add(TicketStatus.Pending);
		status.add(TicketStatus.WorkInProgress);
		status.add(TicketStatus.Pendingclarification);
		status.add(TicketStatus.Draft);
		status.add(TicketStatus.Resolved);
		Expression<TicketStatus> exp = tickets.get("status");
		Predicate pr = exp.in(status);
		if (user.getRoles().contains(Role.ADMIN)) {
			criteriaQuery.select(tickets)
					.where(builder.equal(tickets.get("assignTo"), user.getDepartment().getCommonEmail()), pr)
					.orderBy(builder.desc(tickets.get("id")));
		} else {
			Join<Ticket, User> join = tickets.join("User");
			criteriaQuery.select(tickets).where(builder.equal(join.get("id"), user.getId()), pr)
					.orderBy(builder.desc(tickets.get("id")));
		}
		TypedQuery<Ticket> typedQuery = getEntityManager().createQuery(criteriaQuery);
		typedQuery.setFirstResult((pageNo.intValue() - 1) * pageSize.intValue());
		typedQuery.setMaxResults(pageSize.intValue());
		updatedTicketList = typedQuery.getResultList();
		if (!updatedTicketList.isEmpty()) {
			return new Response(HttpStatus.OK, response.getCount(), updatedTicketList, response.getPages());
		} else {
			return new Response(HttpStatus.NO_CONTENT);
		}
	}

	@Override
	public Response getAllTicketId(User user) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Ticket> criteriaQuery = builder.createQuery(Ticket.class);
		Root<Ticket> root = criteriaQuery.from(Ticket.class);
		if (user.getRoles().contains(Role.ADMIN)) {
			criteriaQuery.select(root.get("id"))
					.where(builder.equal(root.get("assignTo"), user.getDepartment().getCommonEmail()));
		} else {
			Join<Ticket, User> join = root.join("User");
			criteriaQuery.select(root.get("id")).where(builder.equal(join.get("id"), user.getId()));
		}
		TypedQuery<Ticket> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
		List<Ticket> ticketList = typedQuery.getResultList();
		if (!ticketList.isEmpty()) {
			return new Response(ticketList, HttpStatus.OK);
		} else {
			return new Response(HttpStatus.NO_CONTENT);
		}

	}

	/*
	 * @Override public List<Ticket> getAssignedTicketsToAdmin(String email) {
	 * final CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
	 * final CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
	 * final Root<Ticket> root = query.from(Ticket.class);
	 * query.select(root).where(builder.equal(root.get("assignTo"), email),
	 * builder.notEqual(root.get("status"), TicketStatus.Closed));
	 * TypedQuery<Ticket> typedQuery = getEntityManager().createQuery(query);
	 * List<Ticket> ticketList = typedQuery.getResultList(); return ticketList;
	 * }
	 */

	@Override
	public Response getAssignedTicketsToAdmin(User user, Long pageNumber, Long pageSize) {
		Response response = getPageSizeAndPageNumber(user, pageNumber, pageSize);
		final CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
		final Root<Ticket> root = query.from(Ticket.class);
		query.select(root).where(builder.equal(root.get("assignTo"), user.getEmail()),
				builder.notEqual(root.get("status"), TicketStatus.Closed),
				builder.notEqual(root.get("status"), TicketStatus.Resolved));
		TypedQuery<Ticket> typedQuery = getEntityManager().createQuery(query);
		List<Ticket> ticketList = typedQuery.getResultList();
		if (ticketList.isEmpty()) {
			return new Response(HttpStatus.NO_CONTENT);
		}
		return new Response(HttpStatus.OK, pageNumber, ticketList, pageSize);
	}

}
