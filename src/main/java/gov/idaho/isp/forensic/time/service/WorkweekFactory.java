package gov.idaho.isp.forensic.time.service;

import gov.idaho.isp.forensic.time.domain.Workday;

public interface WorkweekFactory {
  Workweek buildWorkweek(Workday workday);
}
