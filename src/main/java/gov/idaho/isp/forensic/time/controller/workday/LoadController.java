package gov.idaho.isp.forensic.time.controller.workday;

import gov.idaho.isp.forensic.time.controller.advice.WorkdayControllerAdvice.LoadWorkday;
import gov.idaho.isp.forensic.time.domain.User;
import gov.idaho.isp.forensic.time.domain.Workday;
import gov.idaho.isp.forensic.time.message.MessageService;
import gov.idaho.isp.forensic.time.service.WorkweekFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller
@LoadWorkday
public class LoadController extends WorkdayController {
  WorkweekFactory workweekFactory;

  public LoadController(WorkweekFactory workweekFactory, MessageService messageService) {
    super(workweekFactory, messageService);
  }

  @GetMapping("/load")
  public String loadEntry(Workday workday, @RequestAttribute User user, Model m) {
    loadCommonAttributes(workday, m);
    return "edit";
  }
}
