package gov.idaho.isp.forensic.time.controller;

import gov.idaho.isp.forensic.time.domain.Category;
import gov.idaho.isp.forensic.time.domain.County;
import gov.idaho.isp.forensic.time.domain.Discipline;
import gov.idaho.isp.forensic.time.domain.Lab;
import gov.idaho.isp.forensic.time.domain.persistence.WorkdayRepository;
import gov.idaho.isp.forensic.time.domain.persistence.WorkdaySpec;
import gov.idaho.isp.forensic.time.message.MessageService;
import gov.idaho.isp.forensic.time.service.CsvExportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController extends BaseController {
  private final WorkdayRepository workdayRepository;
  private final CsvExportService csvExportService;

  public SearchController(WorkdayRepository workdayRepository, CsvExportService csvExportService, MessageService messageService) {
    super(messageService);
    this.workdayRepository = workdayRepository;
    this.csvExportService = csvExportService;
  }

  @GetMapping("/admin/search")
  public String search(WorkdaySpec spec, Pageable pageable, Model m) {
    if (spec.isSubmitted()) {
      Page page = workdayRepository.findAll(spec, pageable);
      m.addAttribute("page", page);
    }

    m.addAttribute("spec", spec);
    m.addAttribute("labs", Lab.values());
    m.addAttribute("disciplines", Discipline.values());
    m.addAttribute("counties", County.values());
    m.addAttribute("categories", Category.values());
    return "search";
  }

  @GetMapping("/admin/export")
  public HttpEntity<byte[]> export(WorkdaySpec spec, Pageable pageable) {
    return csvExportService.exportWorkdays(workdayRepository.findAll(spec, pageable.getSort()));
  }
}
