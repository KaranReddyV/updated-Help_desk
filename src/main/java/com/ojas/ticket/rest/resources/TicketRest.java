package com.ojas.ticket.rest.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ojas.bean.TicketBean;
import com.ojas.ticket.Ticket.TicketDao;
import com.ojas.ticket.dao.department.DepartmentDao;
import com.ojas.ticket.dao.user.UserDao;
import com.ojas.ticket.entity.Department;
import com.ojas.ticket.entity.Response;
import com.ojas.ticket.entity.Ticket;
import com.ojas.ticket.entity.TicketPriority;
import com.ojas.ticket.entity.TicketStatus;
import com.ojas.ticket.entity.User;
import com.ojas.ticket.exception.TicketsNotFoundException;
import com.ojas.ticket.transfer.DetailsTable;
import com.ojas.ticket.transfer.SendMail;

@Component
@Path("/tickets")
public class TicketRest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TicketDao ticketDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private SendMail sendMail;

	@Autowired
	private DepartmentDao departmentDao;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}/{pageno}/{pagesize}")
	public @ResponseBody Response getAdminAssinedIssues(@PathParam("id") Long id, @PathParam("pageno") Long pageNo,
			@PathParam("pagesize") Long pageSize) throws TicketsNotFoundException {
		Response response = null;
		User user = userDao.find(id);
		try {
			response = ticketDao.getAdminAssignedIssues(user, pageNo, pageSize);
			if (response.getErrorCode().equals(HttpStatus.NO_CONTENT)) {
				return response;
			} else {
				List<Ticket> ticketList = (List<Ticket>) response.getResult();
				List<Ticket> openTickets = new ArrayList<Ticket>();
				List<Ticket> inProgressTickets = new ArrayList<Ticket>();
				List<Ticket> otherTickets = new ArrayList<Ticket>();
				List<Ticket> resultList = new ArrayList<Ticket>();
				ticketList.forEach(ticket -> {
					if (ticket.getStatus().equals(TicketStatus.Opened)) {
						openTickets.add(ticket);
					} else if (ticket.getStatus().equals(TicketStatus.WorkInProgress)) {
						inProgressTickets.add(ticket);
					} else {
						otherTickets.add(ticket);
					}
				});
				resultList.addAll(openTickets);
				resultList.addAll(inProgressTickets);
				resultList.addAll(otherTickets);
				response.setResult(resultList);
				response.setDeptName(departmentDao.getdeptNameByCommonMail(user.getDepartment().getCommonEmail()));
			}
		} catch (TicketsNotFoundException e) {
			e.printStackTrace();
		}
		return response;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("ticket_status")
	public ResponseEntity<List<String>> getTicketStatus() {
		return new ResponseEntity<List<String>>(TicketStatus.getStatus(), HttpStatus.OK);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("updated_tickets/{id}/{pageno}/{pagesize}")
	public @ResponseBody Response getRecentlyUpdatedTickets(@PathParam("id") Long id, @PathParam("pageno") Long pageNo,
			@PathParam("pagesize") Long pageSize) throws Exception {
		User user = userDao.find(id);
		Response response = this.ticketDao.getUpdatedTickets(user, pageNo, pageSize);
		return response;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("updatedtickets_by_status/{id}/{pageno}/{pagesize}")
	public @ResponseBody Response getUpdateTicketStatusTickets(@PathParam("id") Long id,
			@PathParam("pageno") Long pageNo, @PathParam("pagesize") Long pageSize) {

		User user = userDao.find(id);
		Response response = this.ticketDao.getUpdatedStatusTicket(user, pageNo, pageSize);
		return response;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("ticketId/{id}/{ticketId}")
	public @ResponseBody Response getTicketById(@PathParam("id") Long id, @PathParam("ticketId") Long ticketId)
			throws TicketsNotFoundException {
		Response response = null;
		User user = userDao.find(id);
		try {
			response = ticketDao.getTicketById(user, ticketId);
		} catch (TicketsNotFoundException e) {
			e.printStackTrace();
		}
		return response;
	}

	@PUT
	@Path("/reopen")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response reOpenTicket(@FormDataParam("id") Long id, @FormDataParam("comment") String comment) {
		Ticket ticket = ticketDao.find(id);
		try {
			ticket.setStatus(TicketStatus.ReOpened);
			ticket.setComment(comment);
			DetailsTable detailsTable = new DetailsTable();
			/*
			 * sendMail.sendMail(ticket.getUser().getEmail(), "ReOpened ticket",
			 * detailsTable.displayTheIssueDetails(ticket));
			 * sendMail.sendMail(ticket.getAssignTo(), "ReOpened ticket",
			 * detailsTable.displayTheIssueDetails
			 * 
			 * (ticket));
			 */
		} catch (TicketsNotFoundException e) {
			return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		ticketDao.save(ticket);
		return new Response(HttpStatus.OK);

	}

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("create/{id}/{email}")
	public Response create(@PathParam("id") Long id, @PathParam("email") String assignTo,
			@FormDataParam("project") String project, @FormDataParam("issueType") String issueType,
			@FormDataParam("comment") String comment, @FormDataParam("description") String description,
			@FormDataParam("priority") TicketPriority priority, @FormDataParam("descNo") String descNo,
			@FormDataParam("note") String note, @FormDataParam("attachment") InputStream attachment,
			@FormDataParam("attachment") FormDataContentDisposition fileName) throws IOException, MessagingException {
		Ticket ticket = new Ticket();

		try {
			ticket.setProject(project);
			// ticket.setDepartment(department);
			ticket.setIssueType(issueType);
			ticket.setComment(comment);
			ticket.setDescription(description);
			ticket.setPriority(priority);
			ticket.setDescNo(descNo);
			ticket.setStatus(TicketStatus.Opened);
			ticket.setNote(note);
			ticket.setUser(userDao.find(id));
			ticket.setAssignTo(assignTo);
			if (null != attachment) {
				ticket.setAttachment(IOUtils.toByteArray(attachment));
				ticket.setFileName(fileName.getFileName());
			}
			this.logger.info("create(): " + ticket);
			String subject = "hello";
			// String msg = "hai";
			DetailsTable detailstable = new DetailsTable();
			// sendMail.sendMail(ticket.getAssignTo(), subject,
			// detailstable.displayTheIssueDetails(ticket));
			ticket.setCreatedDate(new Date());
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return new Response(ticketDao.save(ticket), HttpStatus.OK);
	}

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("draft/{id}/{email}")
	public Response createDraft(@PathParam("id") Long id, @PathParam("email") String assignTo,
			@FormDataParam("project") String project, @FormDataParam("issueType") String issueType,
			@FormDataParam("comment") String comment, @FormDataParam("description") String description,
			@FormDataParam("priority") TicketPriority priority, @FormDataParam("descNo") String descNo,
			@FormDataParam("note") String note, @FormDataParam("attachment") InputStream attachment,
			@FormDataParam("attachment") FormDataContentDisposition fileName) throws IOException, MessagingException {
		Ticket ticket = new Ticket();
		try {
			ticket.setProject(project);
			ticket.setIssueType(issueType);
			ticket.setComment(comment);
			ticket.setDescription(description);
			ticket.setPriority(priority);
			ticket.setDescNo(descNo);
			ticket.setStatus(TicketStatus.Draft);
			ticket.setNote(note);
			ticket.setUser(userDao.find(id));
			ticket.setAssignTo(assignTo);
			if (null != attachment) {
				ticket.setAttachment(IOUtils.toByteArray(attachment));
				ticket.setFileName(fileName.getFileName());
			}
			this.logger.info("Draft(): " + ticket);
			ticket.setCreatedDate(new Date());
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return new Response(ticketDao.save(ticket), HttpStatus.OK);
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/update")
	public Ticket updateTicket(@FormDataParam("id") Long id, @FormDataParam("project") String project,
			@FormDataParam("department") Department department, @FormDataParam("issueType") String issueType,
			@FormDataParam("comment") String comment, @FormDataParam("description") String description,
			@FormDataParam("descNo") String descNo, @FormDataParam("mobNo") String mobNo,
			@FormDataParam("createdDate") String createdDate, @FormDataParam("priority") TicketPriority priority,
			@FormDataParam("status") TicketStatus status, @FormDataParam("attachment") InputStream attachment,
			@FormDataParam("attachment") FormDataContentDisposition fileName, @FormDataParam("remark") String remark,
			@FormDataParam("assignTo") String assignTo) throws IOException, MessagingException {
		this.logger.info("update(): " + id);
		Ticket ticket = ticketDao.find(id);
		try {
			ticket.setProject(project);
			ticket.setIssueType(issueType);
			ticket.setComment(comment);
			ticket.setDescription(description);
			ticket.setPriority(priority);
			if (status.equals(TicketStatus.Draft)) {
				ticket.setStatus(TicketStatus.Opened);
			} else {
				ticket.setStatus(status);
			}
			ticket.setDescNo(descNo);
			ticket.setUpdatedDate(new Date());
			// ticket.setAssignTo(assignTo);
			if (!attachment.toString().isEmpty() && null != fileName.getFileName()) {
				ticket.setAttachment(IOUtils.toByteArray(attachment));
				ticket.setFileName(fileName.getFileName());
			}
			this.logger.info("create(): " + ticket);
			String subject = "hello";
			// String msg = "hai";
			DetailsTable detailstable = new DetailsTable();
			// sendMail.sendMail(ticket.getUser().getEmail(), subject,
			// detailstable.displayTheIssueDetails(ticket));
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return this.ticketDao.save(ticket);
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/query")
	public Response createQuery(@RequestBody TicketBean bean) {
		Ticket ticket = ticketDao.find(bean.getId());
		ticket.setComment(bean.getComment());
		ticket.setStatus(TicketStatus.Pendingclarification);
		ticketDao.save(ticket);
		return new Response(HttpStatus.OK);
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/response")
	public Ticket queryResponse(@RequestBody TicketBean bean) {
		Ticket ticket = ticketDao.find(bean.getId());
		ticket.setDescription(bean.getDescription());
		ticketDao.save(ticket);
		return ticketDao.save(ticket);
	}

	@PUT
	@Path("/reassign/{department}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response reassignTicket(@FormDataParam("id") Long id, @FormDataParam("remark") String remark,
			@PathParam("department") String department) throws MessagingException {
		Ticket ticket = ticketDao.find(id);
		try {
			ticket.setRemark(remark);
			Response response = departmentDao.getEMailByDepartment(department);
			String email = response.getEmail();
			ticket.setAssignTo(email);
			ticket.setStatus(TicketStatus.Opened);
			// ticket.setAssignTo(bean.getAssignTo());
			String subject = "Reassigned ticket";
			DetailsTable detailstable = new DetailsTable();
			/*
			 * sendMail.sendMail(ticket.getUser().getEmail(), subject,
			 * detailstable.displayTheIssueDetails(ticket));
			 * sendMail.sendMail(assignTo, subject,
			 * detailstable.displayTheIssueDetails(ticket));
			 */
		} catch (TicketsNotFoundException e) {
			e.printStackTrace();
		}
		ticketDao.save(ticket);
		return new Response(HttpStatus.OK);
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("assign_within_department/{id}/{assignTo}")
	public Response reassignWithinDepartment(@PathParam("id") Long ticketId, @PathParam("assignTo") String assignTo) {
		Ticket ticket = ticketDao.find(ticketId);
		ticket.setAssignTo(assignTo);
		ticket.setStatus(TicketStatus.Assigned);
		ticket.setUpdatedDate(new Date());
		ticketDao.save(ticket);
		return new Response(HttpStatus.OK);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("get_assigned_tickets/{userid}/{page_number}/{page_size}")
	public ResponseEntity<Response> getAssignedTicketsToAdmin(@PathParam("userid") Long userId,
			@PathParam("page_number") Long pageNumber, @PathParam("page_size") Long pageSize) {
		User user = userDao.find(userId);
		Response response = ticketDao.getAssignedTicketsToAdmin(user, pageNumber, pageSize);
		if (response.getErrorCode().equals(HttpStatus.NO_CONTENT)) {
			return new ResponseEntity<Response>(response, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public void delete(@PathParam("id") Long id) {
		this.logger.info("delete(id)");

		this.ticketDao.delete(id);
	}

	// user tickets from here

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("user/{id}/{pageno}/{pagesize}")
	public @ResponseBody Response getUserTickets(@PathParam("id") Long id, @PathParam("pageno") Long pageNo,
			@PathParam("pagesize") Long pageSize) {
		this.logger.info("getUserTickets() To display tickets belongs to user");
		Response response = ticketDao.getUserTickets(userDao.find(id), pageNo, pageSize);
		if (response.getErrorCode().equals(HttpStatus.NO_CONTENT)) {
			return response;
		} else {
			List<Ticket> ticketList = (List<Ticket>) response.getResult();
			response.setResult(ticketList);
			ticketList.forEach(ticket -> {
				String deptName = departmentDao.getdeptNameByCommonMail(ticket.getAssignTo());
				if (null == deptName) {
					Response dept = userDao.getDepartmentByEMail(ticket.getAssignTo());
					List<String> deptList =  (List<String>) dept.getResult();

					ticket.setAssignTo(deptList.get(0));
				} else
					ticket.setAssignTo(deptName);
			});
			response.setResult(ticketList);
			return response;
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/attachment/{ticket_id}")
	public Ticket getAttachmentView(@PathParam("ticket_id") Long id) {

		Ticket ticket = this.ticketDao.find(id);
		String deptName = departmentDao.getdeptNameByCommonMail(ticket.getAssignTo());
		ticket.setAssignTo(deptName);
		return ticket;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/dept")
	public ResponseEntity<List<Department>> getDepartment() {
		List<Department> department = departmentDao.findAll();

		/*
		 * Department[] deptArr = Department.values(); List<Department> deptList
		 * = Arrays.asList(deptArr); return deptList;
		 */
		return new ResponseEntity<List<Department>>(department, HttpStatus.OK);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)

	@Path("columnFilter/{column}/{filter}/{id}/{pageno}/{pagesize}")
	public @ResponseBody Response getFilterTickets(@PathParam("column") String column,
			@PathParam("filter") String filter, @PathParam("id") Long id, @PathParam("pageno") Long pageNo,
			@PathParam("pagesize") Long pageSize) throws Exception {

		User user = userDao.find(id);
		Response response = this.ticketDao.filterTickets(column, filter, user, pageNo, pageSize);

		return response;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getallticketid/{id}")
	public Response getAllTicketId(@PathParam("id") Long id) {
		User user = userDao.find(id);
		return ticketDao.getAllTicketId(user);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get_all_tickets")
	public ResponseEntity<List<Ticket>> getAllTickets() {
		List<Ticket> ticketList = ticketDao.findAll();
		List<Ticket> openTickets = new ArrayList<Ticket>();
		List<Ticket> inProgressTickets = new ArrayList<Ticket>();
		List<Ticket> otherTickets = new ArrayList<Ticket>();
		List<Ticket> resultList = new ArrayList<Ticket>();
		ticketList.forEach(ticket -> {
			if (ticket.getStatus().equals(TicketStatus.Opened)) {
				openTickets.add(ticket);
			} else if (ticket.getStatus().equals(TicketStatus.WorkInProgress)) {
				inProgressTickets.add(ticket);
			} else {
				otherTickets.add(ticket);
			}
		});
		resultList.addAll(openTickets);
		resultList.addAll(inProgressTickets);
		resultList.addAll(otherTickets);
		return new ResponseEntity<List<Ticket>>(resultList, HttpStatus.OK);
	}

}
