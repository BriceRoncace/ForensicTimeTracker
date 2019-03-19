package gov.idaho.isp.forensic.time.controller.workday;

import gov.idaho.isp.forensic.time.controller.BaseController;
import gov.idaho.isp.forensic.time.domain.Category;
import gov.idaho.isp.forensic.time.domain.County;
import gov.idaho.isp.forensic.time.domain.Discipline;
import gov.idaho.isp.forensic.time.domain.Lab;
import gov.idaho.isp.forensic.time.domain.Workday;
import gov.idaho.isp.forensic.time.message.MessageService;
import gov.idaho.isp.forensic.time.service.WorkweekFactory;
import java.util.Arrays;
import org.springframework.ui.Model;

public abstract class WorkdayController extends BaseController {
  private final WorkweekFactory workweekFactory;

  public WorkdayController(WorkweekFactory workweekFactory, MessageService messageService) {
    super(messageService);
    this.workweekFactory = workweekFactory;
  }

  protected void loadCommonAttributes(Workday workday, Model m) {
    m.addAttribute("workday", workday);
    m.addAttribute("labs", Lab.values());
    m.addAttribute("disciplines", Discipline.values());
    m.addAttribute("workEntryCategories", Category.workEntryValues());
    m.addAttribute("courtEntryCategories", Category.courtEntryValues());
    m.addAttribute("counties", Arrays.asList(County.values()));
    m.addAttribute("workweek", workweekFactory.buildWorkweek(workday));
  }
}
