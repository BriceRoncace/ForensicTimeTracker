package gov.idaho.isp.forensic.time.domain.calendar;

import gov.idaho.isp.forensic.time.domain.Hours;
import java.util.ArrayList;
import java.util.List;

public class Calendar {
  List<Hours> weekTotals;
  Hours calendarTotals;
  List<CalendarEvent> events = new ArrayList<>();

  public List<Hours> getWeekTotals() {
    return weekTotals;
  }

  public void setWeekTotals(List<Hours> weekTotals) {
    this.weekTotals = weekTotals;
  }

  public Hours getCalendarTotals() {
    return calendarTotals;
  }

  public void setCalendarTotals(Hours calendarTotals) {
    this.calendarTotals = calendarTotals;
  }

  public List<CalendarEvent> getEvents() {
    return events;
  }

  public void setEvents(List<CalendarEvent> events) {
    this.events = events;
  }
}
