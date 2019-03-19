package gov.idaho.isp.forensic.time.message;

import java.util.List;
import org.springframework.validation.Errors;

public interface MessageService {
  List<String> convertErrors(Errors errors);
  String getMessageText(String messageName, String... args);
}