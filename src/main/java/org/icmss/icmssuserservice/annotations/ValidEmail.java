package org.icmss.icmssuserservice.annotations;



import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidEmailImpl.class)
public @interface ValidEmail {

    String message() default "email-invalid format;";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
