package gov.idaho.isp.forensic.time.service;

import gov.idaho.isp.forensic.time.domain.User;
import gov.idaho.isp.forensic.time.domain.persistence.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  public DatabaseUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsernameIgnoreCase(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found with username [" + username + "]");
    }

    return user;
  }
}
