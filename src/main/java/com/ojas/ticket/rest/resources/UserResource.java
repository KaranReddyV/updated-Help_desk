package com.ojas.ticket.rest.resources;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.ojas.ticket.entity.AccessToken;
import com.ojas.ticket.entity.User;
import com.ojas.ticket.service.UserService;
import com.ojas.ticket.transfer.UserTransfer;

@Component
@Path("/user")
public class UserResource {
	@Autowired
	private UserService userService;

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserTransfer getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (!(principal instanceof UserDetails)) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		UserDetails userDetails = (UserDetails) principal;

		return new UserTransfer(userDetails.getUsername(), this.createRoleMap(userDetails));
	}

	@Path("authenticate")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public com.ojas.ticket.entity.Response authenticate(@FormParam("email") String email,
			@FormParam("password") String password) {
		AccessToken token = null;
		try {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
					password);
			Authentication authentication = this.authManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			Object principal = authentication.getPrincipal();
			if (!(principal instanceof User)) {
				throw new WebApplicationException(Response.Status.UNAUTHORIZED);
			} else {
				token = this.userService.createAccessToken((User) principal);
				return new com.ojas.ticket.entity.Response(token, "SUCCESS");
			}
		} catch (Exception e) {
			return new com.ojas.ticket.entity.Response("Un-autherized");
		}
	}

	private Map<String, Boolean> createRoleMap(UserDetails userDetails) {
		Map<String, Boolean> roles = new HashMap<>();
		for (GrantedAuthority authority : userDetails.getAuthorities()) {
			roles.put(authority.getAuthority(), Boolean.TRUE);
		}

		return roles;
	}
}
