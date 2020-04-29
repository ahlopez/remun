package com.transmi.remun.backend.service.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.transmi.remun.backend.data.entity.User;
import com.transmi.remun.backend.data.repositories.UserRepository;

/**
 * Implements the {@link UserDetailsServiceImpl}.
 *
 * This implementation searches for {@link User} entities by the e-mail address
 * supplied in the login screen.
 */
@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService
{

  private final UserRepository userRepository;

  @Autowired
  public UserDetailsServiceImpl(UserRepository userRepository)
  {
    this.userRepository = userRepository;

  }// UserDetailsServiceImpl

  /**
   *
   * Recovers the {@link User} from the database using the e-mail address supplied
   * in the login screen. If the user is found, returns a
   * {@link org.springframework.security.core.userdetails.User}.
   *
   * @param username User's e-mail address
   *
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmailIgnoreCase(username);
    if (null == user)
      throw new UsernameNotFoundException("No user present with username: " + username);

    return new org.springframework.security.core.userdetails.User(
        user.getEmail(), user.getPasswordHash(),
        Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
    );

  }// loadUserByUsername

}// UserDetailsServiceImpl