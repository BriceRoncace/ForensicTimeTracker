package gov.idaho.isp.forensic.time.message.transformer;

import gov.idaho.isp.forensic.time.domain.Workday;
import gov.idaho.isp.forensic.time.message.ErrorMessageTransformer;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

public class WorkEntryErrorMessageTransformer implements ErrorMessageTransformer {

  @Override
  public boolean isSupported(MessageSourceResolvable message) {
    return Arrays.asList(message.getCodes()).contains("typeMismatch.workday.workEntries.entries.hours.normal")
      || Arrays.asList(message.getCodes()).contains("typeMismatch.workday.workEntries.entries.hours.overtime");
  }

  @Override
  public MessageSourceResolvable transform(MessageSourceResolvable message, Errors errors) {
    Optional<String> code = Arrays.stream(message.getCodes()).filter(s -> s.contains("workEntries[") && s.contains("entries[")).findFirst();

    if (code.isPresent()) {
      Optional<Integer> workEntryIndex = CategoryErrorUtils.getIndex(code.get(), "workEntries\\[([0-9]+)\\]");
      Optional<Integer> entryIndex = CategoryErrorUtils.getIndex(code.get(), "entries\\[([0-9]+)\\]");
      Optional<String> errorCode = CategoryErrorUtils.getErrorCode(code.get());

      if (workEntryIndex.isPresent() && entryIndex.isPresent() && errorCode.isPresent()) {
        Optional<String> categoryName = getCategoryName(errors, workEntryIndex.get(), entryIndex.get());
        String[] errorCodes = new String[] {categoryName.isPresent() ? errorCode.get() : errorCode.get() + ".removed"};
        Object[] arguments = new Object[]{CategoryErrorUtils.formatNumber(workEntryIndex.get() + 1), categoryName.orElse("")};
        return new DefaultMessageSourceResolvable(errorCodes, arguments, errorCodes[0]);
      }
    }

    return message;
  }

  private Optional<String> getCategoryName(Errors errors, Integer workEntryIndex, Integer entryIndex) {
    if (errors instanceof BindingResult) {
      BindingResult br = (BindingResult) errors;
      return getCategoryName((Workday) br.getTarget(), workEntryIndex, entryIndex);
    }
    return Optional.empty();
  }

  private Optional<String> getCategoryName(Workday workday, Integer workEntryIndex, Integer entryIndex) {
    if (workEntryIndex < workday.getWorkEntries().size() && entryIndex < workday.getWorkEntries().get(workEntryIndex).getEntries().size()) {
      return Optional.of(workday.getWorkEntries().get(workEntryIndex).getEntries().get(entryIndex).getCategory().getLabel());
    }
    return Optional.empty();
  }
}
