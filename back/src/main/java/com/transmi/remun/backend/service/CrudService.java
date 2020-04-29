package com.transmi.remun.backend.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transmi.remun.backend.data.entity.AbstractEntity;
import com.transmi.remun.backend.data.entity.User;

public interface CrudService<T extends AbstractEntity>
{

  JpaRepository<T, Long> getRepository();

  default T save(User currentUser, T entity) { return getRepository().saveAndFlush(entity); }

  default void delete(User currentUser, T entity) {
    if (entity == null)
    {
      throw new EntityNotFoundException();
    }

    getRepository().delete(entity);
  }// delete

  default void delete(User currentUser, Long id) { delete(currentUser, load(id)); }

  default long count() { return getRepository().count(); }

  default T load(Long id) {
    T entity = getRepository().findById(id).orElse(null);
    if (entity == null)
    {
      throw new EntityNotFoundException();
    }

    return entity;
  }// load

  T createNew(User currentUser);

}// CrudService
