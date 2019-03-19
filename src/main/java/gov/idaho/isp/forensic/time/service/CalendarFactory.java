package gov.idaho.isp.forensic.time.service;

import gov.idaho.isp.forensic.time.domain.Workday;
import gov.idaho.isp.forensic.time.domain.calendar.Calendar;
import java.time.LocalDate;
import java.util.List;

public interface CalendarFactory {
  Calendar getCalendar(LocalDate start, LocalDate end, List<Workday> workdays);
}
