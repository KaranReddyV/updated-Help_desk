package com.ojas.ticket.entity;

import java.io.Serializable;

public enum TicketPriority implements Serializable {
	High("High"), Medium("Medium"), Low("Low");

	private String priority;

	TicketPriority(String priority) {
		this.setPriority(priority);
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

}
