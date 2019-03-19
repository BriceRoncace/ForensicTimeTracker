package gov.idaho.isp.forensic.time.message;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.Errors;

/**
 * Strategy interface for transforming MessageSourceResolvable messages
 * derived from binding errors using their source MessageSourceResolvable and the
 * triggering BindingResult. Intended to be used by <code>MessageServiceImpl</code>
 * when converting binding error messages.
 */
public interface ErrorMessageTransformer {
  boolean isSupported(MessageSourceResolvable message);
  MessageSourceResolvable transform(MessageSourceResolvable message, Errors errors);
}
