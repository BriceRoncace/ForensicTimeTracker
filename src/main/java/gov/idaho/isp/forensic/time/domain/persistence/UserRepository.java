package gov.idaho.isp.forensic.time.domain.persistence;

import gov.idaho.isp.forensic.time.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsernameIgnoreCase(String username);

  @Override @Query("from User order by lastName asc, firstName asc")
  List<User> findAll();
}
