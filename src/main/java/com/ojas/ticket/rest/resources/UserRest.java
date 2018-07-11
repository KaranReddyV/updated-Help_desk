package com.ojas.ticket.rest.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ojas.bean.UserBean;
import com.ojas.ticket.dao.department.DepartmentDao;
import com.ojas.ticket.dao.user.UserDao;
import com.ojas.ticket.entity.Department;
import com.ojas.ticket.entity.Response;
import com.ojas.ticket.entity.Role;
import com.ojas.ticket.entity.User;
import com.ojas.ticket.service.UserService;
import com.ojas.ticket.transfer.SendMail;
import com.ojas.ticket.transfer.UserRes;

@Component
@Path("/users")
public class UserRest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserDao userDao;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SendMail sendMail;

	@Autowired
	private DepartmentDao departmentDao;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String list() throws IOException {
		this.logger.info("list()");
		List<User> allEntries = this.userDao.findAll();
		List<UserRes> UserResList = new ArrayList<>();
		UserRes userRes = new UserRes();
		Iterator<User> itr = allEntries.iterator();
		while (itr.hasNext()) {
			User user = itr.next();
			userRes.setId(user.getId());
			userRes.setFirstName(user.getFirstName());
			userRes.setLastName(user.getLastName());
			userRes.setEmail(user.getEmail());
			userRes.setMobileNo(user.getMobNo());
			userRes.setEnable(user.isEnabled());
			userRes.setRoles(user.getRoles());
			UserResList.add(userRes);
		}

		return mapper.writeValueAsString(UserResList);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("get_email")
	public Response getOTP(@RequestBody UserBean bean) throws MessagingException {
		User user = userDao.findByEmail(bean.getEmail());
		if (bean.getEmail().equals(user.getEmail())) {
			int randomPin = (int) (Math.random() * 9000) + 1000;
			String otp = String.valueOf(randomPin);
			String subject = "OTP for Ticket";
			sendMail.sendMail(user.getEmail(), subject, otp);
			user.setOtp(otp);
			userDao.save(user);
			return new Response(bean, "SUCCESS");
		} else
			return new Response("ERROR");
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("forgot_password")
	public Response setPassword(@RequestBody UserBean bean) {
		User user = userDao.findByOTP(bean.getOtp());
		if (null != user) {
			if (bean.getPassword().equals(bean.getConfirmPassword()) && bean.getOtp().equals(user.getOtp())) {
				user.setPassword(passwordEncoder.encode(bean.getPassword()));
				userDao.save(user);
				return new Response("SUCCESS");
			} else {
				return new Response("ERROR");
			}
		} else {
			return new Response("Invalid OTP");
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("roles")
	public ResponseEntity<List<Role>> getRoles() {
		return new ResponseEntity<List<Role>>(Arrays.asList(Role.values()), HttpStatus.OK);
	}

	/*
	 * @GET
	 * 
	 * @Produces(MediaType.APPLICATION_JSON)
	 * 
	 * @Path("department/{role}") public List<Department>
	 * getDepartmentsByRole(@PathParam("role") Role role) { List<Department>
	 * deptList = Department.getdepartmentsByRole(role); if (deptList.isEmpty()
	 * || deptList == null) return null; else return deptList; }
	 */
	/*
	 * @GET
	 * 
	 * @Produces(MediaType.APPLICATION_JSON)
	 * 
	 * @Path("dept/{role}") public List<Department>
	 * getDepartmentsBasedOnRole(@PathParam("role") Role role) { return
	 * Department.getdepartmentsByRole(role); }
	 */
	/*
	 * @GET
	 * 
	 * @Produces(MediaType.APPLICATION_JSON)
	 * 
	 * @Path("assign_to/{dept}") public Response
	 * getEmailByDepartment(@PathParam("dept") String dept) { Department
	 * department =Department.valueOf(dept); return
	 * Department.getEmailByDepartment(department);
	 * 
	 * }
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("register")
	public Response registerUser(@RequestBody UserBean bean) {
		User user = userDao.findByEmail(bean.getEmail());
		if (null == user || user.getRoles().contains(bean.getRole())) {

			User newUser = new User();

			newUser.addRole(bean.getRole());
			newUser.setFirstName(bean.getFirstName());
			newUser.setLastName(bean.getLastName());
			newUser.setEmpId(bean.getEmpId());
			newUser.setEmail(bean.getEmail());
			newUser.setMobNo(bean.getMobNo());
			if (bean.getRole().equals(Role.ADMIN.toString())) {
				Department department = departmentDao.findByDepartment(bean.getDeptName());
				newUser.setDepartment(department);
			}

			if (bean.getPassword().equals(bean.getConfirmPassword()))
				newUser.setPassword(passwordEncoder.encode(bean.getPassword()));
			userDao.save(newUser);
			return new Response(HttpStatus.OK, newUser);
		} else {
			return new Response("ERROR");
		}
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("admin")
	public List<User> getAdmin() {

		return userDao.getAdmin();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("emails_within_department/{deptname}")
	public ResponseEntity<List<String>> getEmailsWithinDepartment(@PathParam("deptname") String deptName) {
		List<String> departNameList = userDao.getEmailsWithinDepartment(deptName);
		return new ResponseEntity<List<String>>(departNameList, HttpStatus.OK);
	}

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("deptbymail/{email}")
	public Response getDepartmentByEMail(@PathParam("email") String email) {
		return userDao.getDepartmentByEMail(email);
	}

	/*
	 * @GET
	 * 
	 * @Consumes(MediaType.MULTIPART_FORM_DATA)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON)
	 * 
	 * @Path("department/{dept}") public ResponseEntity<String>
	 * getEMailByDepartment(@PathParam("dept") String department) { String email
	 * = departmentDao.getEMailByDepartment(department); return new
	 * ResponseEntity<String>(email, HttpStatus.OK); }
	 */
	

}
