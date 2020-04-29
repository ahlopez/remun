package com.transmi.remun.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.transmi.remun.backend.data.entity.Parm;
import com.transmi.remun.backend.data.entity.User;
import com.transmi.remun.backend.data.repositories.ParmRepository;
import com.transmi.remun.service.exception.UserFriendlyDataException;

@Service
public class ParmService implements FilterableCrudService<Parm>
{

  private final ParmRepository parmRepository;

  @Autowired
  public ParmService(ParmRepository parmRepository)
  { this.parmRepository = parmRepository; }

  @Override
  public Page<Parm> findAnyMatching(Optional<String> filter, Pageable pageable) {
    if (filter.isPresent())
    {
      String repositoryFilter = "%" + filter.get() + "%";
      return parmRepository.findByNameLikeIgnoreCase(repositoryFilter, pageable);
    }

    return find(pageable);

  }// findAnyMatching

  @Override
  public long countAnyMatching(Optional<String> filter) {
    if (filter.isPresent())
    {
      String repositoryFilter = "%" + filter.get() + "%";
      return parmRepository.countByNameLikeIgnoreCase(repositoryFilter);
    }

    return count();

  }// countAnyMatching

  public Page<Parm> find(Pageable pageable) { return parmRepository.findBy(pageable); }

  @Override
  public JpaRepository<Parm, Long> getRepository() { return parmRepository; }

  @Override
  public Parm createNew(User currentUser) { return new Parm(); }

  @Override
  public Parm save(User currentUser, Parm entity) {
    try
    {
      return FilterableCrudService.super.save(currentUser, entity);
    } catch (DataIntegrityViolationException e)
    {
      throw new UserFriendlyDataException(
          "El contrato ya tiene un parámetro con el mismo nombre. Por favor use ese parámetro o cambie el nombre de este nuevo parámetro."
      );
    }

  }// save

}// ParmCrudService
