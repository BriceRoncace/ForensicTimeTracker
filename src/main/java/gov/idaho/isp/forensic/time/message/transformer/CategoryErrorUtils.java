package gov.idaho.isp.forensic.time.message.transformer;

import java.util.Optional;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CategoryErrorUtils {
  public static String formatNumber(Integer integer) {
    String end;
    if (integer == 1) {
      end = "st";
    }
    else if (integer == 2) {
      end = "nd";
    }
    else if (integer == 3) {
      end = "rd";
    }
    else {
      end = "th";
    }
    return integer.toString() + end;
  }

  public static Optional<Integer> getIndex(String code, String patternString) {
    Pattern pattern = Pattern.compile(patternString);
    Matcher matcher = pattern.matcher(code);
    if (matcher.find()) {
      try {
        return Optional.of(Integer.valueOf(matcher.group(1)));
      }
      catch (Exception ex) {
      }
    }
    return Optional.empty();
  }

  public static Optional<String> getErrorCode(String code) {
    Optional<String> type = getType(code);
    Optional<String> end = getEnd(code);

    if (type.isPresent() && end.isPresent()) {
      StringJoiner sj = new StringJoiner(".");
      sj.add("typeMismatch");
      sj.add(type.get());
      sj.add(end.get());
      return Optional.of(sj.toString());
    }
    return Optional.empty();
  }

  private static Optional<String> getType(String code) {
    if (code.contains("workEntries")) {
      return Optional.of("workEntries");
    }
    if (code.contains("courtTestimonies")) {
      return Optional.of("courtTestimonies");
    }
    return Optional.empty();
  }

  private static Optional<String> getEnd(String code) {
    if (code.endsWith("normal")) {
      return Optional.of("normal");
    }
    if (code.endsWith("overtime")) {
      return Optional.of("overtime");
    }
    return Optional.empty();
  }
}
