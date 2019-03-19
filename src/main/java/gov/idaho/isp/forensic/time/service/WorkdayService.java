package gov.idaho.isp.forensic.time.service;

import gov.idaho.isp.forensic.time.domain.Workday;
import java.util.Optional;

public interface WorkdayService {
  Workday preprocessWorkday(Workday workday);
  Optional<Workday> copyPrevious(Workday workday);
}
