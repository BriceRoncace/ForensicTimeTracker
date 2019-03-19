package gov.idaho.isp.forensic.time.domain.persistence.validation;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AllowedFractionsValidator implements ConstraintValidator<AllowedFractions, BigDecimal> {
  private List<String> validFractions;

  @Override
  public void initialize(AllowedFractions constraintAnnotation) {
    validFractions = Arrays.stream(constraintAnnotation.value()).map(f -> f.replace(".", "")).collect(Collectors.toList());
  }

  @Override
  public boolean isValid(BigDecimal bd, ConstraintValidatorContext cvc) {
    return bd == null || validFractions.contains(getFractionalValue(bd));
  }

  private static String getFractionalValue(BigDecimal bd) {
    String val = bd.stripTrailingZeros().toString();
    if (val.contains(".")) {
      return val.substring(val.indexOf(".")+1);
    }
    return "0";
  }
}