package gov.idaho.isp.forensic.time.domain.calendar;

import gov.idaho.isp.forensic.time.domain.Workday;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CalendarEvent {
  private final String title;
  private final LocalDate start;
  private final BigDecimal normal;
  private final BigDecimal overtime;

  public CalendarEvent(Workday workday) {
    this.title = workday.getDisplayName();
    this.start = workday.getWorkdayDate();
    this.normal = workday.getNormalHoursTotal();
    this.overtime = workday.getOvertimeHoursTotal();
  }

  public String getTitle() {
    return title;
  }

  public LocalDate getStart() {
    return start;
  }

  public BigDecimal getNormal() {
    return normal;
  }

  public BigDecimal getOvertime() {
    return overtime;
  }
}
