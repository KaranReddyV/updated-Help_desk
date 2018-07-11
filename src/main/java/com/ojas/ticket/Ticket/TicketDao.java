package com.ojas.ticket.Ticket;

import java.util.List;

import com.ojas.ticket.dao.Dao;
import com.ojas.ticket.entity.Response;
import com.ojas.ticket.entity.Ticket;
import com.ojas.ticket.entity.User;

public interface TicketDao extends Dao<Ticket, Long> {

	Response filterTickets(String column, String filter, User user, Long pageNo, Long pageSize);

	Response getUpdatedTickets(User user, Long pageNo, Long pageSize);

	Response getTicketById(User user, Long ticketId);

	Response getAdminAssignedIssues(User user, Long pageNo, Long pageSize);

	Response getUserTickets(User user, Long pageNo, Long pageSize);

	Response getUpdatedStatusTicket(User user, Long pageNo, Long pageSize);

	Response getAllTicketId(User user);

	Response getAssignedTicketsToAdmin(User user, Long pageNumber, Long pageSize);

}
