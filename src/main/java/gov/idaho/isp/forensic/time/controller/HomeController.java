package gov.idaho.isp.forensic.time.controller;

import gov.idaho.isp.forensic.time.domain.User;
import gov.idaho.isp.forensic.time.domain.Workday;
import gov.idaho.isp.forensic.time.domain.calendar.Calendar;
import gov.idaho.isp.forensic.time.domain.persistence.CriteriaDate;
import gov.idaho.isp.forensic.time.domain.persistence.WorkdayRepository;
import gov.idaho.isp.forensic.time.domain.persistence.WorkdaySpec;
import gov.idaho.isp.forensic.time.message.MessageService;
import gov.idaho.isp.forensic.time.service.CalendarFactory;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController extends BaseController {
  private final WorkdayRepository workdayRepository;
  private final CalendarFactory calendarFactory;

  public HomeController(WorkdayRepository workdayRepository, CalendarFactory calendarFactory, MessageService messageService) {
    super(messageService);
    this.workdayRepository = workdayRepository;
    this.calendarFactory = calendarFactory;
  }

  @GetMapping("/")
  public String getEntries(Model m) {
    m.addAttribute("users", workdayRepository.findAllUsers());
    return "index";
  }

  @GetMapping("/json/workdays")
  @ResponseBody
  public Calendar getWorkdays(@RequestAttribute User user, @RequestParam String username, @RequestParam LocalDate start, @RequestParam LocalDate end) {
    if (!user.isAdmin()) {
      username = user.getUsername();
    }

    WorkdaySpec spec = new WorkdaySpec();
    spec.setUsername(username);
    spec.getWorkingDate().setDate1(start);
    spec.getWorkingDate().setDate2(end);
    spec.getWorkingDate().setSearchType(CriteriaDate.SearchType.BETWEEN);

    List<Workday> workdays = workdayRepository.findAll(spec);
    return calendarFactory.getCalendar(start, end, workdays);
  }
}
