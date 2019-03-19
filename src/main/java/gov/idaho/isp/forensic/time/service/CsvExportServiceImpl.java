package gov.idaho.isp.forensic.time.service;

import gov.idaho.isp.forensic.time.domain.Category;
import gov.idaho.isp.forensic.time.domain.CourtTestimony;
import gov.idaho.isp.forensic.time.domain.Discipline;
import gov.idaho.isp.forensic.time.domain.Entry;
import gov.idaho.isp.forensic.time.domain.Hours;
import gov.idaho.isp.forensic.time.domain.WorkEntry;
import gov.idaho.isp.forensic.time.domain.Workday;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class CsvExportServiceImpl implements CsvExportService {
  private final CsvStringCleaner cleaner = new CsvStringCleaner();

  @Override
  public HttpEntity<byte[]> exportWorkdays(List<Workday> workdays) {
    List<String> fileLines = new ArrayList<>(Arrays.asList(getHeader()));
    fileLines.addAll(workdays.stream().flatMap(wd -> getWorkdayLines(wd).stream()).collect(Collectors.toList()));

    String fileName = "workday-export.csv";
    byte[] fileContent = String.join(cleaner.NEW_LINE, fileLines).getBytes();
    MediaType mediaType = new MediaType("text", "csv", Charset.forName("utf-8"));

    return toHttpEntity(fileName, fileContent, mediaType);
  }

  private List<String> getWorkdayLines(Workday workday) {
    List<String> lines = new ArrayList<>();

    Arrays.stream(Discipline.values()).forEach(d -> {
      List<WorkEntry> workEntries = workday.getWorkEntries().stream().filter(we -> d.equals(we.getDiscipline())).collect(Collectors.toList());
      List<CourtTestimony> courtTestimonies = workday.getCourtTestimonies().stream().filter(ct -> d.equals(ct.getDiscipline())).collect(Collectors.toList());

      if (!workEntries.isEmpty() || !courtTestimonies.isEmpty()) {
        do {
          Optional<CourtTestimony> courtTestimony = courtTestimonies.stream().findFirst();
          Optional<WorkEntry> workEntry = workEntries.stream().findFirst();

          lines.add(getLine(workday, d, workEntry, courtTestimony));

          workEntry.ifPresent(we -> workEntries.remove(we));
          courtTestimony.ifPresent(ct -> courtTestimonies.remove(ct));
        } while (!workEntries.isEmpty() || !courtTestimonies.isEmpty());
      }
    });

    return lines;
  }

  private String getLine(Workday workday, Discipline discipline, Optional<WorkEntry> workEntry, Optional<CourtTestimony> courtTestimony) {
    StringJoiner line = new StringJoiner(cleaner.COMMA);
    line.add(cleaner.clean(workday.getFirstName()));
    line.add(cleaner.clean(workday.getMiddleName()));
    line.add(cleaner.clean(workday.getLastName()));
    line.add(cleaner.clean(workday.getUsername()));
    line.add(cleaner.clean(workday.getWorkdayDate()));
    line.add(cleaner.clean(workday.getLab().getLabel()));
    line.add(cleaner.clean(discipline.getLabel()));

    Category.workEntryValues().forEach(c -> {
      Optional<Entry> oe = workEntry.map(we -> we.getEntries().stream().filter(e -> c.equals(e.getCategory())).findFirst()).orElseGet(() -> Optional.empty());
      line.add(cleaner.clean(getTime(Optional.ofNullable(oe.map(e -> e.getHours()).orElse(null)))));
      line.add(cleaner.clean(getOvertime(Optional.ofNullable(oe.map(e -> e.getHours()).orElse(null)))));
    });

    line.add(cleaner.clean(courtTestimony.map(ct -> ct.getCaseNumbersJoined()).orElse(null)));
    line.add(cleaner.clean(courtTestimony.map(ct -> ct.getCounty() != null ? ct.getCounty().getLabel() : null).orElse(null)));

    Category.courtEntryValues().forEach(c -> {
      Optional<Entry> oe = courtTestimony.map(ct -> ct.getEntries().stream().filter(e -> c.equals(e.getCategory())).findFirst()).orElseGet(() -> Optional.empty());
      line.add(cleaner.clean(getTime(Optional.ofNullable(oe.map(e -> e.getHours()).orElse(null)))));
      line.add(cleaner.clean(getOvertime(Optional.ofNullable(oe.map(e -> e.getHours()).orElse(null)))));
    });

    return line.toString();
  }

  private BigDecimal getTime(Optional<Hours> optionalHours) {
    return optionalHours.map(h -> h.getNormal()).orElseGet(() -> BigDecimal.ZERO);
  }

  private BigDecimal getOvertime(Optional<Hours> optionalHours) {
    return optionalHours.map(h -> h.getOvertime()).orElseGet(() -> BigDecimal.ZERO);
  }

  private String getHeader() {
    StringJoiner header = new StringJoiner(cleaner.COMMA);
    header.add(cleaner.clean("First Name"));
    header.add(cleaner.clean("Middle Name"));
    header.add(cleaner.clean("Last Name"));
    header.add(cleaner.clean("Username"));
    header.add(cleaner.clean("Date"));
    header.add(cleaner.clean("Lab"));
    header.add(cleaner.clean("Discipline"));
    Category.workEntryValues().forEach(c -> {
      header.add(cleaner.clean(c.getLabel() + " Time"));
      header.add(cleaner.clean(c.getLabel() + " Overtime"));
    });
    header.add(cleaner.clean("Case Number"));
    header.add(cleaner.clean("County"));
    Category.courtEntryValues().forEach(c -> {
      header.add(cleaner.clean(c.getLabel() + " Time"));
      header.add(cleaner.clean(c.getLabel() + " Overtime"));
    });
    return header.toString();
  }

  private HttpEntity<byte[]> toHttpEntity(String fileName, byte[] content, MediaType mediaType) {
    HttpHeaders header = new HttpHeaders();
    header.setContentType(mediaType);
    header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
    header.setContentLength(content == null ? 0 : content.length);
    return new HttpEntity<>(content, header);
  }
}
