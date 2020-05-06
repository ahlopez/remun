package com.transmi.remun.backend.service;

import java.util.Optional;
import java.util.function.BiConsumer;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.transmi.remun.backend.data.entity.Contractor;
import com.transmi.remun.backend.data.entity.User;
import com.transmi.remun.backend.data.repositories.ContractorRepository;

@Service
public class ContractorService implements FilterableCrudService<Contractor>
{

  private static Contractor globalContractor;

  private final ContractorRepository contractorRepository;

  @Autowired
  public ContractorService(ContractorRepository contractorRepository)
  {
    super();
    this.contractorRepository = contractorRepository;
    globalContractor          = contractorRepository.findByCode("GLOBAL");
  }// ContractorCrudService

  @Transactional(rollbackOn = Exception.class)
  public Contractor saveContractor(User currentUser, Long id, BiConsumer<User, Contractor> contractorFiller) {
    Contractor contractor;
    if (id == null)
    {
      contractor = createUnknownContractor();
    } else
    {
      contractor = load(id);
    }
    contractorFiller.accept(currentUser, contractor);
    return contractorRepository.save(contractor);
  }// saveContractor

  private Contractor createUnknownContractor() { return new Contractor(); }

  @Transactional(rollbackOn = Exception.class)
  public Contractor saveContractor(Contractor contractor) { return contractorRepository.save(contractor); }

  @Override
  public JpaRepository<Contractor, Long> getRepository() { return contractorRepository; }

  @Override
  public Page<Contractor> findAnyMatching(Optional<String> optionalFilter, Pageable pageable) {

    return optionalFilter.isPresent() && !optionalFilter.get().isEmpty() ?
        contractorRepository.findAll(optionalFilter.get(), pageable) :
        contractorRepository.findAll(pageable);

  }// findAnyMatching

  @Override
  public long countAnyMatching(Optional<String> filter) {
    if (filter.isPresent())
    {
      String repositoryFilter = "%" + filter.get() + "%";
      return contractorRepository.countByFullNameContainingIgnoreCase(repositoryFilter);
    }

    return count();

  }// countAnyMatching

  @Override
  @Transactional
  public Contractor createNew(User currentUser) { return createUnknownContractor(); }// createNew

  public static Contractor getGlobalContractor() { return globalContractor; }

}// ContractorService
