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

import com.transmi.remun.backend.data.entity.Contract;
import com.transmi.remun.service.util.ContractStatus;
import com.transmi.remun.service.util.TransmiPhase;

public interface ContractRepository extends JpaRepository<Contract, Long>
{

  @Query("select c from Contract c " +
      "where lower(c.fase) like lower(concat('%', :searchTerm, '%'))" +
      " or   lower(c.code) like lower(concat('%', :searchTerm, '%'))" +
      " or   lower(c.name) like lower(concat('%', :searchTerm, '%'))")

  @EntityGraph(value = Contract.ENTITY_GRAPH_BRIEF, type = EntityGraphType.LOAD)
  Page<Contract> findAll(@Param("searchTerm") String searchTerm, Pageable pageable);

  @EntityGraph(value = Contract.ENTITY_GRAPH_BRIEF, type = EntityGraphType.LOAD)
  Page<Contract> findByFase(String searchQuery, Pageable pageable);

  @EntityGraph(value = Contract.ENTITY_GRAPH_BRIEF, type = EntityGraphType.LOAD)
  Page<Contract> findByCode(String searchQuery, Pageable pageable);

  @Override
  @EntityGraph(value = Contract.ENTITY_GRAPH_BRIEF, type = EntityGraphType.LOAD)
  List<Contract> findAll();

  @Override
  @EntityGraph(value = Contract.ENTITY_GRAPH_BRIEF, type = EntityGraphType.LOAD)
  Page<Contract> findAll(Pageable pageable);

  @Override
  @EntityGraph(value = Contract.ENTITY_GRAPH_FULL, type = EntityGraphType.LOAD)
  Optional<Contract> findById(Long id);

  @Query("select count(c) from Contract c " +
      "where lower(c.fase) like lower(concat('%', :searchTerm, '%')) " +
      "or    lower(c.code) like lower(concat('%', :searchTerm, '%')) " +
      "or    lower(c.name) like lower(concat('%', :searchTerm, '%')) ")
  long countAll(@Param("searchTerm") String searchTerm);

  long countByFase(TransmiPhase fase);

  long countByCode(String code);

  long countByNameContainingIgnoreCase(String searchQuery);

  long countByStatus(ContractStatus status);

}// ContractRepository
