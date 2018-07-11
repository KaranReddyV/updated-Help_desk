package com.ojas.ticket.transfer;

import com.ojas.ticket.entity.Ticket;

public class DetailsTable {

	public String displayTheIssueDetails(Ticket ticket) {


		StringBuffer issue = new StringBuffer();
		issue.append(
				"<html><head><style>table {font-family: arial, sans-serif;border-collapse: collapse;width: 100%;}");
		issue.append("table th{font-size:14px;color:#e57a1f}");
		issue.append("table td{font-size:13px;color:#11585f}");
		issue.append("td, th {border: 1px solid #dddddd;text-align: left;padding: 8px;}");
		issue.append("tr:nth-child(even) {background-color: #dddddd;}");
		issue.append("</style></head><body><div>");
		issue.append("<table style='background-color:#cbdadf'><tr><th>Issue Type</th><th>comment</th>");
		issue.append("<th>Description</th><th>Priority</th><th>Ticket Status</th>");
		issue.append("<th>Note</th><th>Name</th><th>Mob. no.</th></tr>");
		issue.append("<tr><td>" + ticket.getIssueType() + "</td><td>" + ticket.getComment() + "</td>");
		issue.append("<td>" + ticket.getDescription() + "</td><td>" + ticket.getPriority() + "</td>");
		issue.append("<td>" + ticket.getStatus() + "</td><td>" + ticket.getNote() + "</td>");
		issue.append("<td>" + ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName() + "</td>");
		issue.append("<td>" + ticket.getUser().getMobNo() + "</td></tr>");
		issue.append("</table></div></body></html>");
		return issue.toString();
	}
}