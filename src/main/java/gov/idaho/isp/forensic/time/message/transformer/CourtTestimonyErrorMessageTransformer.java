package gov.idaho.isp.forensic.time.message.transformer;

import gov.idaho.isp.forensic.time.domain.Workday;
import gov.idaho.isp.forensic.time.message.ErrorMessageTransformer;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

public class CourtTestimonyErrorMessageTransformer implements ErrorMessageTransformer {

  @Override
  public boolean isSupported(MessageSourceResolvable message) {
    return Arrays.asList(message.getCodes()).contains("typeMismatch.workday.courtTestimonies.entries.hours.normal")
      || Arrays.asList(message.getCodes()).contains("typeMismatch.workday.courtTestimonies.entries.hours.overtime");
  }

  @Override
  public MessageSourceResolvable transform(MessageSourceResolvable message, Errors errors) {
    Optional<String> code = Arrays.stream(message.getCodes()).filter(s -> s.contains("courtTestimonies[") && s.contains("entries[")).findFirst();

    if (code.isPresent()) {
      Optional<Integer> courtIndex = CategoryErrorUtils.getIndex(code.get(), "courtTestimonies\\[([0-9]+)\\]");
      Optional<Integer> entryIndex = CategoryErrorUtils.getIndex(code.get(), "entries\\[([0-9]+)\\]");
      Optional<String> errorCode = CategoryErrorUtils.getErrorCode(code.get());

      if (courtIndex.isPresent() && entryIndex.isPresent() && errorCode.isPresent()) {
        Optional<String> categoryName = getCategoryName(errors, courtIndex.get(), entryIndex.get());
        String[] errorCodes = new String[] {categoryName.isPresent() ? errorCode.get() : errorCode.get() + ".removed"};
        Object[] arguments = new Object[]{CategoryErrorUtils.formatNumber(courtIndex.get() + 1), categoryName.orElse("")};
        return new DefaultMessageSourceResolvable(errorCodes, arguments, errorCodes[0]);
      }
    }

    return message;
  }

  private Optional<String> getCategoryName(Errors errors, Integer courtIndex, Integer entryIndex) {
    if (errors instanceof BindingResult) {
      BindingResult br = (BindingResult) errors;
      return getCategoryName((Workday) br.getTarget(), courtIndex, entryIndex);
    }
    return Optional.empty();
  }

  private Optional<String> getCategoryName(Workday workday, Integer courtIndex, Integer entryIndex) {
    if (courtIndex < workday.getCourtTestimonies().size() && entryIndex < workday.getCourtTestimonies().get(courtIndex).getEntries().size()) {
      return Optional.of(workday.getCourtTestimonies().get(courtIndex).getEntries().get(entryIndex).getCategory().getLabel());
    }
    return Optional.empty();
  }
}
