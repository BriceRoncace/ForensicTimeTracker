package gov.idaho.isp.forensic.time.controller.advice;

import gov.idaho.isp.forensic.time.controller.advice.WorkdayControllerAdvice.LoadWorkday;
import gov.idaho.isp.forensic.time.domain.User;
import gov.idaho.isp.forensic.time.domain.Workday;
import gov.idaho.isp.forensic.time.domain.persistence.CriteriaDate;
import gov.idaho.isp.forensic.time.domain.persistence.CriteriaDate.SearchType;
import gov.idaho.isp.forensic.time.domain.persistence.UserRepository;
import gov.idaho.isp.forensic.time.domain.persistence.WorkdayRepository;
import gov.idaho.isp.forensic.time.domain.persistence.WorkdaySpec;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@ControllerAdvice(annotations = LoadWorkday.class)
public class WorkdayControllerAdvice {

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface LoadWorkday {
  }

  private final WorkdayRepository workdayRepository;
  private final UserRepository userRepository;

  public WorkdayControllerAdvice(WorkdayRepository workdayRepository, UserRepository userRepository) {
    this.workdayRepository = workdayRepository;
    this.userRepository = userRepository;
  }

  @ModelAttribute
  public Workday prepareWorkday(HttpServletRequest request, @RequestAttribute User user, @RequestParam String username, @RequestParam LocalDate workdayDate) {
    if (user.isAdmin() || user.getUsername().equals(username)) {
      WorkdaySpec spec = new WorkdaySpec();
      spec.setWorkingDate(new CriteriaDate(workdayDate, SearchType.ON));
      spec.setUsername(username);
      return getExistingOrNewWorkday(spec);
    }
    throw new AccessDeniedException("Access Denied");
  }

  private Workday getExistingOrNewWorkday(WorkdaySpec spec) {
    List<Workday> workdays = workdayRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "workdayDate"));
    return workdays.isEmpty() ? newWorkdayForUser(spec.getUsername()) : workdays.get(0);
  }

  private Workday newWorkdayForUser(String username) {
    User person = userRepository.findByUsernameIgnoreCase(username);
    if (person == null) {
      throw new RuntimeException("Could not find user " + username);
    }
    return new Workday(person);
  }
}
