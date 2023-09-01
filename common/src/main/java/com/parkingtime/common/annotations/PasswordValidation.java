package com.parkingtime.common.annotations;

import com.parkingtime.common.annotations.impl.PasswordValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordValidatorImpl.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface PasswordValidation {
    String message() default "Password does not meet minimum requirements to be accepted.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
