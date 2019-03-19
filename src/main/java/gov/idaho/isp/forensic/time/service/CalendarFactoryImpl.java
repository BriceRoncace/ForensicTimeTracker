package gov.idaho.isp.forensic.time.service;

import gov.idaho.isp.forensic.time.domain.Hours;
import gov.idaho.isp.forensic.time.domain.Workday;
import gov.idaho.isp.forensic.time.domain.calendar.Calendar;
import gov.idaho.isp.forensic.time.domain.calendar.CalendarEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CalendarFactoryImpl implements CalendarFactory {
  @Override
  public Calendar getCalendar(LocalDate start, LocalDate end, List<Workday> workdays) {
    Calendar calendar = new Calendar();
    calendar.setWeekTotals(getWeekTotals(start, end, workdays));
    calendar.setCalendarTotals(getCalendarTotals(workdays));
    calendar.setEvents(workdays.stream().map(wd -> new CalendarEvent(wd)).collect(Collectors.toList()));
    return calendar;
  }

  private List<Hours> getWeekTotals(LocalDate start, LocalDate end, List<Workday> workdays) {
    List<Hours> totals = new ArrayList<>();

    for (int i = 0; i < ChronoUnit.WEEKS.between(start, end); i++) {
      List<Workday> weekdays = getWeekdays(start.plusDays(i * 7), workdays);

      Hours weekTotal = new Hours();
      weekTotal.setNormal(weekdays.stream().map(w -> w.getNormalHoursTotal()).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
      weekTotal.setOvertime(weekdays.stream().map(w -> w.getOvertimeHoursTotal()).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
      totals.add(weekTotal);
    }

    return totals;
  }

  private List<Workday> getWeekdays(LocalDate startDate, List<Workday> workdays) {
    List<Workday> weekDays = new ArrayList<>();
    for (int i = 0; i < 7; i++) {
      for (Workday w : workdays) {
        if (startDate.plusDays(i).equals(w.getWorkdayDate())) {
          weekDays.add(w);
        }
      }
    }
    return weekDays;
  }

  private Hours getCalendarTotals(List<Workday> workdays) {
    Hours totals = new Hours();
    totals.setNormal(workdays.stream().map(w -> w.getNormalHoursTotal()).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
    totals.setOvertime(workdays.stream().map(w -> w.getOvertimeHoursTotal()).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
    return totals;
  }
}
