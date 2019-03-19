package gov.idaho.isp.forensic.time.controller.workday;

import gov.idaho.isp.forensic.time.controller.advice.WorkdayControllerAdvice.LoadWorkday;
import gov.idaho.isp.forensic.time.domain.Workday;
import gov.idaho.isp.forensic.time.message.MessageService;
import gov.idaho.isp.forensic.time.service.WorkdayService;
import gov.idaho.isp.forensic.time.service.WorkweekFactory;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@LoadWorkday
public class CopyPreviousController extends WorkdayController {
  private final WorkdayService workdayService;

  public CopyPreviousController(WorkdayService workdayService, WorkweekFactory workweekFactory, MessageService messageService) {
    super(workweekFactory, messageService);
    this.workdayService = workdayService;
  }

  @GetMapping("/copyPrevious")
  public String copyPrevious(Workday workday, Model m) {
    Optional<Workday> copiedFromPrevious = workdayService.copyPrevious(workday);
    loadCommonAttributes(copiedFromPrevious.orElse(workday), m);
    return "edit";
  }
}
