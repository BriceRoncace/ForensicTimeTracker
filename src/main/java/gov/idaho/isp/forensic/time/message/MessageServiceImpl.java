package gov.idaho.isp.forensic.time.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.Errors;

public class MessageServiceImpl implements MessageService {
  private final MessageSource messageSource;
  private final List<ErrorMessageTransformer> errorMessageTransformers = new ArrayList<>();

  public MessageServiceImpl() {
    this.messageSource = new NullMessageSource();
  }

  public MessageServiceImpl(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public List<String> convertErrors(Errors errors) {
    List<String> errorMsgs = new ArrayList<>();
    errorMsgs.addAll(getFieldBindingFailures(errors));
    errorMsgs.addAll(getFieldErrors(errors));
    errorMsgs.addAll(getGlobalErrors(errors));
    return errorMsgs;
  }

  private List<String> getFieldBindingFailures(Errors errors) {
    return errors.getFieldErrors()
      .stream()
      .filter(fe -> fe.isBindingFailure())
      .map(fe -> messageSource.getMessage(transform(fe, errors), Locale.getDefault()))
      .collect(Collectors.toList());
  }

  private List<String> getFieldErrors(Errors errors) {
    return errors.getFieldErrors()
      .stream()
      .filter(fe -> !fe.isBindingFailure())
      .map(fe -> messageSource.getMessage(transform(fe, errors), Locale.getDefault()))
      .collect(Collectors.toList());
  }

  private List<String> getGlobalErrors(Errors errors) {
    return errors.getGlobalErrors()
      .stream()
      .map(ge -> messageSource.getMessage(transform(ge, errors), Locale.getDefault()))
      .collect(Collectors.toList());
  }

  private MessageSourceResolvable transform(MessageSourceResolvable msg, Errors errors) {
    return errorMessageTransformers.stream()
      .filter(t -> t.isSupported(msg))
      .map(t -> transform(t, msg, errors))
      .findAny().orElse(msg);
  }

  private MessageSourceResolvable transform(ErrorMessageTransformer transformer, MessageSourceResolvable msg, Errors errors) {
    MessageSourceResolvable transformedMsg = transformer.transform(msg, errors);
    if (transformedMsg == null) {
      throw new NullPointerException("Transformed MessageSourceResolvable cannot be null.  Faulty transformer: " + transformer);
    }
    return transformedMsg;
  }

  @Override
  public String getMessageText(String messageName, String... args) {
    return messageSource.getMessage(messageName, args, messageName, Locale.getDefault());
  }

  public void addErrorMessageTransformer(ErrorMessageTransformer transformer) {
    errorMessageTransformers.add(transformer);
  }

  public MessageSource getMessageSource() {
    return messageSource;
  }

  private static class NullMessageSource implements MessageSource {
    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
      return defaultMessage;
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
      return code;
    }

    @Override
    public String getMessage(MessageSourceResolvable msr, Locale locale) throws NoSuchMessageException {
      return msr.getDefaultMessage();
    }
  }
}