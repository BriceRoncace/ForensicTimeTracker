package gov.idaho.isp.forensic.time.domain.persistence;

import gov.idaho.isp.forensic.time.domain.User;
import gov.idaho.isp.forensic.time.domain.Workday;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface WorkdayRepository extends JpaRepository<Workday, Long>, JpaSpecificationExecutor<Workday> {
  @Query("SELECT distinct new gov.idaho.isp.forensic.time.domain.User(firstName, middleName, lastName, username) FROM Workday order by lastName")
  List<User> findAllUsers();
  Workday findByWorkdayDateAndUsername(LocalDate workdayDate, String username);
}
