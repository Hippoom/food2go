package com.github.hippoom.food2go.infrastructure.persistence;

import java.io.Serializable;

import org.springframework.data.repository.Repository;

public interface QueryRepository<T, ID extends Serializable> extends
		Repository<T, ID> {

	T findOne(ID id);

}
