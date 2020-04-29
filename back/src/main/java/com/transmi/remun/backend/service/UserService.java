package com.transmi.remun.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transmi.remun.backend.data.entity.User;
import com.transmi.remun.backend.data.repositories.UserRepository;
import com.transmi.remun.service.exception.UserFriendlyDataException;

@Service
public class UserService implements FilterableCrudService<User>
{

  public static final String MODIFY_LOCKED_USER_NOT_PERMITTED = "Usuario ha sido bloqueado y no puede ser modificado ni eliminado";

  private static final String DELETING_SELF_NOT_PERMITTED = "Usted no puede eliminar su propio usuario";

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository)
  { this.userRepository = userRepository; }

  @Override
  public Page<User> findAnyMatching(Optional<String> filter, Pageable pageable) {
    if (filter.isPresent())
    {
      String repositoryFilter = "%" + filter.get() + "%";
      return getRepository()
          .findByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
              repositoryFilter, repositoryFilter, repositoryFilter, repositoryFilter, pageable
          );
    }

    return find(pageable);

  }// findAnyMatching

  @Override
  public long countAnyMatching(Optional<String> filter) {
    if (filter.isPresent())
    {
      String repositoryFilter = "%" + filter.get() + "%";
      return userRepository.countByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
          repositoryFilter, repositoryFilter, repositoryFilter, repositoryFilter
      );
    }

    return count();

  }// countAnyMatching

  @Override
  public UserRepository getRepository() { return userRepository; }

  public Page<User> find(Pageable pageable) { return getRepository().findBy(pageable); }

  @Override
  public User save(User currentUser, User entity) {
    throwIfUserLocked(entity);
    return getRepository().saveAndFlush(entity);
  }// save

  @Override
  @Transactional
  public void delete(User currentUser, User userToDelete) {
    throwIfDeletingSelf(currentUser, userToDelete);
    throwIfUserLocked(userToDelete);
    FilterableCrudService.super.delete(currentUser, userToDelete);
  }

  private void throwIfDeletingSelf(User currentUser, User user) {
    if (currentUser.equals(user))
    {
      throw new UserFriendlyDataException(DELETING_SELF_NOT_PERMITTED);
    }

  }

  private void throwIfUserLocked(User entity) {
    if (entity != null && entity.isLocked())
    {
      throw new UserFriendlyDataException(MODIFY_LOCKED_USER_NOT_PERMITTED);
    }

  }

  @Override
  public User createNew(User currentUser) { return new User(); }

}// UserCrudService
