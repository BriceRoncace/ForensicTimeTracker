package gov.idaho.isp.forensic.time.service;

import gov.idaho.isp.forensic.time.domain.Workday;
import gov.idaho.isp.forensic.time.domain.persistence.WorkdayRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class WorkweekFactoryImpl implements WorkweekFactory {
  private final WorkdayRepository workdayRepository;

  public WorkweekFactoryImpl(WorkdayRepository workdayRepository) {
    this.workdayRepository = workdayRepository;
  }

  @Override
  public Workweek buildWorkweek(Workday workday) {
    Workweek workweek = new Workweek();
    LocalDate date = workday.getWorkdayDate();
    if (!date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
      date = date.with(DayOfWeek.SUNDAY).minusDays(7);
    }

    for (int i = 0; i < 7; i++) {
      Workday day = workdayRepository.findByWorkdayDateAndUsername(date, workday.getUsername());
      workweek.getWeek().add(day != null ? day : (workday.getWorkdayDate().equals(date) ? workday : new Workday(date)));
      date = date.plusDays(1);
    }
    return workweek;
  }
}
