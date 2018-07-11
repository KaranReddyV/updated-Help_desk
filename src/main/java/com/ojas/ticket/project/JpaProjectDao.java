/*package com.ojas.ticket.project;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Transactional;

import com.ojas.ticket.dao.JpaDao;
import com.ojas.ticket.entity.Project;


public class JpaProjectDao extends JpaDao<Project, Long> implements ProjectDao
{

	 public JpaProjectDao()
	    {
	        super(Project.class);
	    }
   

	 @Override
	    @Transactional(readOnly = true)
	    public List<Project> findAll()
	    {
	        final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
	        final CriteriaQuery<Project> criteriaQuery = builder.createQuery(Project.class);

	        Root<Project> root = criteriaQuery.from(Project.class);
	        criteriaQuery.orderBy(builder.desc(root.get("addingdate")));

	        TypedQuery<Project> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
	        return typedQuery.getResultList();
	    }
	

  
}
*/