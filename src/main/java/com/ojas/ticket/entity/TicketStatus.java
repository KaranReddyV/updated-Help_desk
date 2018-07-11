package com.ojas.ticket.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TicketStatus {
	Pending("Pending"), Opened("Opened"), WorkInProgress("Work In Progress"), Resolved(
			"Resolved"), Pendingclarification("Pending clarification"), Closed(
					"Closed"), update("Update"), ReOpened("Reopened"), Draft("Draft"), Assigned("Assigned");

	private String name;

	TicketStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static List<String> getStatus() {
		List<TicketStatus> statusList = Arrays.asList(TicketStatus.values());
		List<String> nameList = new ArrayList<String>();
		statusList.forEach(status -> {
			nameList.add(status.name);
		});
		return nameList;
	}

}
