package gov.idaho.isp.forensic.time.service;

import gov.idaho.isp.forensic.time.domain.Workday;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Workweek {

  private List<Workday> week = new ArrayList<>();

  public BigDecimal getWeekNormalTimeTotal() {
    return week.stream().map(d -> d.getNormalHoursTotal()).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public BigDecimal getWeekOvertimeTotal() {
    return week.stream().map(d -> d.getOvertimeHoursTotal()).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public BigDecimal getWeekTimeTotal() {
    return getWeekNormalTimeTotal().add(getWeekOvertimeTotal());
  }

  public Workday getDay(DayOfWeek dayOfWeek) {
    return week.stream().filter(d -> d.getDayOfWeek() == dayOfWeek).findFirst().orElse(null);
  }

  public List<Workday> getWeek() {
    return week;
  }

  public void setWeek(List<Workday> week) {
    this.week = week;
  }

  @Override
  public String toString() {
    return "Workweek{" + "week=" + week + '}';
  }
}
