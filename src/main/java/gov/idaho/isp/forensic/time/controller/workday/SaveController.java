package gov.idaho.isp.forensic.time.controller.workday;

import gov.idaho.isp.forensic.time.controller.advice.WorkdayControllerAdvice.LoadWorkday;
import gov.idaho.isp.forensic.time.domain.Workday;
import gov.idaho.isp.forensic.time.domain.persistence.WorkdayRepository;
import gov.idaho.isp.forensic.time.message.MessageService;
import gov.idaho.isp.forensic.time.service.WorkdayService;
import gov.idaho.isp.forensic.time.service.WorkweekFactory;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@LoadWorkday
public class SaveController extends WorkdayController {
  private final WorkdayRepository workdayRepository;
  private final WorkdayService workdayService;
  private final Validator validator;

  public SaveController(WorkdayRepository workdayRepository, WorkdayService workdayService, Validator validator, WorkweekFactory workWeekFactory, MessageService messageService) {
    super(workWeekFactory, messageService);
    this.workdayRepository = workdayRepository;
    this.workdayService = workdayService;
    this.validator = validator;
  }

  @PostMapping("/save")
  public String saveEntry(Workday workday, BindingResult br, Optional<String> returnQueryString, Model m, RedirectAttributes ra) {
    workday = workdayService.preprocessWorkday(workday);
    validator.validate(workday, br);

    if (br.hasErrors()) {
      m.addAttribute("errors", getErrors(br));
      loadCommonAttributes(workday, m);
      return "edit";
    }

    workdayRepository.save(workday);
    ra.addFlashAttribute("messages", getText("var.saved", "Workday " + workday.getWorkdayDate().format(DateTimeFormatter.ofPattern("M/d")) + " for " + workday.getDisplayName()));

    if (returnQueryString.isPresent()) {
      return "redirect:/admin/search" + returnQueryString.get();
    }
    return "redirect:/";
  }
}
