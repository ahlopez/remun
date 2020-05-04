package com.transmi.remun.backend.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.BiConsumer;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.transmi.remun.backend.data.entity.Contract;
import com.transmi.remun.backend.data.entity.Contractor;
import com.transmi.remun.backend.data.entity.User;
import com.transmi.remun.backend.data.repositories.ContractRepository;

@Service
public class ContractService implements FilterableCrudService<Contract>
{

  private final ContractRepository contractRepository;

  @Autowired
  public ContractService(ContractRepository contractRepository)
  {
    super();
    this.contractRepository = contractRepository;
  }// ContractCrudService

  @Transactional(rollbackOn = Exception.class)
  public Contract saveContract(User currentUser, Long id, BiConsumer<User, Contract> contractFiller) {
    Contract contract;
    if (id == null)
    {
      contract = new Contract(createUnknownContractor(), currentUser);
    } else
    {
      contract = load(id);
    }

    contractFiller.accept(currentUser, contract);
    return contractRepository.save(contract);
  }// saveContract

  private Contractor createUnknownContractor() {
    Contractor unknown = new Contractor();
    // unknown.setFullName("--Desconocido--");
    return unknown;
  }// createUnknownContractor

  @Transactional(rollbackOn = Exception.class)
  public Contract saveContract(Contract contract) { return contractRepository.save(contract); }

  @Transactional(rollbackOn = Exception.class)
  public Contract addComment(User currentUser, Contract contract, String comment) {
    contract.addHistoryItem(currentUser, comment);
    return contractRepository.save(contract);
  }// addComment

  @Override
  public JpaRepository<Contract, Long> getRepository() { return contractRepository; }

  @Override
  public Page<Contract> findAnyMatching(Optional<String> optionalFilter, Pageable pageable) {

    return optionalFilter.isPresent() && !optionalFilter.get().isEmpty() ?
        contractRepository.findByFase(optionalFilter.get(), pageable) :
        contractRepository.findAll(pageable);

  }// findAnyMatching

  @Override
  public long countAnyMatching(Optional<String> filter) {
    if (filter.isPresent())
    {
      String repositoryFilter = "%" + filter.get() + "%";
      return contractRepository.countByNameContainingIgnoreCase(repositoryFilter);
    }

    return count();

  }// countAnyMatching

  @Override
  @Transactional
  public Contract createNew(User currentUser) {
    Contract contract = new Contract(createUnknownContractor(), currentUser);
    contract.setFromDate(LocalDate.now());
    contract.setToDate(LocalDate.now());
    return contract;
  }// createNew

}// ContractCrudService
