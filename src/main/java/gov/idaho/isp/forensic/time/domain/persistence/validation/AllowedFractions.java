package gov.idaho.isp.forensic.time.domain.persistence.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = AllowedFractionsValidator.class)
@Target( { ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedFractions {
  String message() default "{invalidFraction}";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  String[] value();
}
