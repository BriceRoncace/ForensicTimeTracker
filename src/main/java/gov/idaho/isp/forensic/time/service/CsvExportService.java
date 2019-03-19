package gov.idaho.isp.forensic.time.service;

import gov.idaho.isp.forensic.time.domain.Workday;
import java.util.List;
import org.springframework.http.HttpEntity;

public interface CsvExportService {
  HttpEntity<byte[]> exportWorkdays(List<Workday> workdays);
}
