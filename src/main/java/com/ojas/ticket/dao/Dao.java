package com.ojas.ticket.dao;

import java.util.List;

import com.ojas.ticket.entity.Entity;

public interface Dao<T extends Entity, I> {
	List<T> findAll();

	T find(I id);

	T save(T entity);

	void delete(I id);

	void delete(T entity);
	
}
