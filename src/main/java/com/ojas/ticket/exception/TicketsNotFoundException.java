package com.ojas.ticket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Tickets Not Found")
public class TicketsNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TicketsNotFoundException(String message) {
		super(message);

	}

	public TicketsNotFoundException() {
		super();

	}

}
