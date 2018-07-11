/*package com.ojas.ticket.rest.resources;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ojas.ticket.entity.Project;
import com.ojas.ticket.project.ProjectDao;

@Component
@Path("/projects")
public class ProjectResource 
{
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
	 
	    @Autowired
	    private ProjectDao projectDao;
	    
	    @Autowired
	    private ObjectMapper mapper;
	    
	    
	    
	    @GET
	    @Produces(MediaType.APPLICATION_JSON)
	    public String list() throws IOException
	    {
	        this.logger.info("list()");

	        List<Project> allEntries = this.projectDao.findAll();

	        return mapper.writeValueAsString(allEntries);
	    }
	    
	    
	    @GET
	    @Produces(MediaType.APPLICATION_JSON)
	    @Path("{id}")
	    public Project read(@PathParam("id") Long id)
	    {
	        this.logger.info("read(id)");

	        Project project = this.projectDao.find(id);
	        if (project == null) {
	            throw new WebApplicationException(Response.Status.NOT_FOUND);
	        }

	        return project;
	    }
	    
	    
	 
	    @POST
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	   
	    public Project create(Project project)
	    {
	        this.logger.info("create(): " + project);
	        
	        return this.projectDao.save(project);

	        
	    }
	    
	    @POST
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	    @Path("{id}")
	    public Project update(@PathParam("id") Long id, Project project)
	    {
	        this.logger.info("update(): " + project);

	        return this.projectDao.save(project);
	    }

	    @DELETE
	    @Produces(MediaType.APPLICATION_JSON)
	    @Path("{id}")
	    public void delete(@PathParam("id") Long id)
	    {
	        this.logger.info("delete(id)");

	        this.projectDao.delete(id);
	    }

}
*/