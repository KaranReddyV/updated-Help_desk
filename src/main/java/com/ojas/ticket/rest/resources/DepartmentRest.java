package com.ojas.ticket.rest.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.ojas.bean.DepartmentBean;
import com.ojas.ticket.dao.department.DepartmentDao;
import com.ojas.ticket.entity.Department;
import com.ojas.ticket.entity.Response;
import com.ojas.ticket.entity.Role;

@Component
@Path("/department")
public class DepartmentRest {

	@Autowired
	private DepartmentDao departmentDao;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("findall")
	public Response getDepartmentList(){
		List<Department> response=departmentDao.findAll();
		return  new Response(response, HttpStatus.OK);
		
	}

	@GET
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("email_by_dept/{dept}")
	public ResponseEntity<Response> getEMailByDepartment(@PathParam("dept") String deptName) {
		Response response = departmentDao.getEMailByDepartment(deptName);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("departments")
	public List<String> getDepartment(@PathParam("role") Role role) {
		List<String> departmentList = departmentDao.getDepartment();
		return departmentList;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("deptname_by_email")
	public String getDeptNameByEmail(@FormDataParam("commonEmail") String commonEmail) {
		String deptName = departmentDao.getdeptNameByCommonMail(commonEmail);
		return deptName;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("add_department")
	public ResponseEntity<HttpStatus> addDepartment(@RequestBody DepartmentBean bean) {
		Department dept = departmentDao.getDeptByemailOrName(bean.getCommonEmail(), bean.getDeptName());
		if (null == dept) {
			Department department = new Department();
			department.setCommonEmail(bean.getCommonEmail());
			department.setDeptName(bean.getDeptName());
			departmentDao.save(department);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.ALREADY_REPORTED);
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("edit_department/{deptid}")
	public javax.ws.rs.core.Response editDepartment(@PathParam("deptid") Long deptid,
			@RequestBody DepartmentBean bean) {
		Department department = departmentDao.find(deptid);
		if (null != bean.getCommonEmail()) {
			String deptName = departmentDao.getdeptNameByCommonMail(bean.getCommonEmail());
			if (null == deptName)
				department.setCommonEmail(bean.getCommonEmail());
		}
		if (null != bean.getDeptName()) {
			List<String> departmentNameList = departmentDao.getDepartment();
			if (!departmentNameList.contains(bean.getDeptName()))
				department.setDeptName(bean.getDeptName());
		}
		return javax.ws.rs.core.Response.status(Status.OK).entity(departmentDao.save(department)).build();
	}

}
