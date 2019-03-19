package gov.idaho.isp.forensic.time.service;

import gov.idaho.isp.forensic.time.domain.Workday;
import gov.idaho.isp.forensic.time.domain.persistence.CriteriaDate;
import gov.idaho.isp.forensic.time.domain.persistence.WorkdayRepository;
import gov.idaho.isp.forensic.time.domain.persistence.WorkdaySpec;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class WorkdayServiceImpl implements WorkdayService {

  private final WorkdayRepository workdayRepository;

  public WorkdayServiceImpl(WorkdayRepository workdayRepository) {
    this.workdayRepository = workdayRepository;
  }

  @Override
  public Workday preprocessWorkday(Workday workday) {
    workday.getWorkEntries().removeIf(we -> we.isEmpty());
    workday.getCourtTestimonies().removeIf(ct -> ct.isEmpty());
    return workday;
  }

  @Override
  public Optional<Workday> copyPrevious(Workday workday) {
    Optional<Workday> previousWorkday = findMostRecentPrevious(workday);

    return previousWorkday.map(wd -> {
      Workday copiedWorkday = copy(wd);
      copiedWorkday.setWorkdayDate(workday.getWorkdayDate());
      return copiedWorkday;
    });
  }

  private Optional<Workday> findMostRecentPrevious(Workday workday) {
    WorkdaySpec spec = new WorkdaySpec();
    spec.setWorkingDate(new CriteriaDate(workday.getWorkdayDate(), CriteriaDate.SearchType.BEFORE));
    spec.setUsername(workday.getUsername());
    Page<Workday> page = workdayRepository.findAll(spec, PageRequest.of(0, 1, new Sort(Sort.Direction.DESC, "workdayDate")));
    return Optional.ofNullable(page.hasContent() ? page.getContent().get(0) : null);
  }

  private Workday copy(Workday workday) {
    workday.setId(null);
    return workday;
  }
}
