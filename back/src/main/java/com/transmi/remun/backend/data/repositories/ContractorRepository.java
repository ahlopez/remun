package com.transmi.remun.backend.data.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.transmi.remun.backend.data.entity.Contractor;

public interface ContractorRepository extends JpaRepository<Contractor, Long>
{

  @Query("select c from Contractor c " +
      "where lower(c.code) like lower(concat('%', :searchTerm, '%')) " +
      "or lower(c.fullName) like lower(concat('%', :searchTerm, '%'))")
  Page<Contractor> findAll(@Param("searchTerm") String searchTerm, Pageable pageable);

  @EntityGraph(type = EntityGraphType.LOAD)
  Page<Contractor> findByCodeContainingIgnoreCase(String searchQuery, Pageable pageable);

  @EntityGraph(type = EntityGraphType.LOAD)
  Page<Contractor> findByFullNameContainingIgnoreCase(String searchQuery, Pageable pageable);

  Contractor findByCode(String code);

  @Override
  @EntityGraph(type = EntityGraphType.LOAD)
  List<Contractor> findAll();

  @Override
  @EntityGraph(type = EntityGraphType.LOAD)
  Page<Contractor> findAll(Pageable pageable);

  @Override
  @EntityGraph(type = EntityGraphType.LOAD)
  Optional<Contractor> findById(Long id);

  long countByCodeContainingIgnoreCase(String searchQuery);

  long countByFullNameContainingIgnoreCase(String searchQuery);

}// ContractorRepository
