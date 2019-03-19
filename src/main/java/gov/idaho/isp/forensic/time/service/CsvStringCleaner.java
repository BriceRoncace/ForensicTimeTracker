package gov.idaho.isp.forensic.time.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class CsvStringCleaner {
  public final String NEW_LINE = "\n";
  public final String COMMA = ",";
  public final String EMPTY_STR = "";
  public final String QUOTE = "\"";
  public final String DOUBLE_QUOTE = "\"\"";

  private final Pattern SINGLEQUOTE = Pattern.compile(QUOTE);

  private final DateTimeFormatter formatter;

  public CsvStringCleaner() {
    formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
  }

  public CsvStringCleaner(String datePattern) {
    formatter = DateTimeFormatter.ofPattern(datePattern);
  }

  public String clean(String str) {
    str = escapeQuotes(str);
    str = surroundWithQuotes(str);
    return str;
  }

  public String clean(boolean bool) {
    return clean(String.valueOf(bool));
  }

  public String clean(Boolean bool) {
    return bool != null ? clean(bool.booleanValue()) : EMPTY_STR;
  }

  public String clean(Long l) {
    return l != null ? clean(String.valueOf(l)) : EMPTY_STR;
  }

  public String clean(Integer i) {
    return i != null ? clean(String.valueOf(i)) : EMPTY_STR;
  }

  public String clean(LocalDate localDate) {
    return localDate != null ? clean(formatter.format(localDate)) : EMPTY_STR;
  }

  public String clean(LocalDateTime localDateTime) {
    return localDateTime != null ? clean(formatter.format(localDateTime)) : EMPTY_STR;
  }

  public String clean(BigDecimal bigDecimal) {
    return bigDecimal != null ? clean(bigDecimal.toString()) : EMPTY_STR;
  }

  private String escapeQuotes(String str) {
    if (isEmpty(str)) {
      return EMPTY_STR;
    }
    return SINGLEQUOTE.matcher(str).replaceAll(DOUBLE_QUOTE).trim();
  }

  private String surroundWithQuotes(String str) {
    if (isEmpty(str)) {
      return EMPTY_STR;
    }
    return QUOTE + str +  QUOTE;
  }

  private boolean isEmpty(String str) {
    return StringUtils.stripToEmpty(str).isEmpty();
  }
}
