package gov.idaho.isp.forensic.time.controller;

import gov.idaho.isp.forensic.time.message.MessageService;
import java.util.List;
import org.springframework.validation.BindingResult;

public abstract class BaseController {
  private final MessageService messageService;

  public BaseController(MessageService messageService) {
    this.messageService = messageService;
  }

  protected List<String> getErrors(BindingResult bindingResult) {
    return messageService.convertErrors(bindingResult);
  }

  protected String getText(String text, String... args) {
    return messageService.getMessageText(text, args);
  }
}
