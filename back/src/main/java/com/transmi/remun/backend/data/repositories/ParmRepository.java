package com.transmi.remun.backend.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.transmi.remun.backend.data.entity.Parm;

public interface ParmRepository extends JpaRepository<Parm, Long>
{

  Page<Parm> findBy(Pageable page);

  Page<Parm> findByNameLikeIgnoreCase(String name, Pageable page);

  int countByNameLikeIgnoreCase(String name);

}// CustomerRepository
