package com.parkingtime.common.annotations.impl;

import com.parkingtime.common.annotations.PasswordValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.passay.*;

import java.util.Arrays;
import java.util.List;

public class PasswordValidatorImpl implements ConstraintValidator<PasswordValidation, String> {

    @Override
    public void initialize(PasswordValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @SneakyThrows
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if(password == null) {
            return false;
        }

        PasswordValidator passwordValidator = new PasswordValidator(
                Arrays.asList(
                        new LengthRule(8, 64),
                        new CharacterRule(EnglishCharacterData.UpperCase, 1),
                        new CharacterRule(EnglishCharacterData.LowerCase, 1),
                        new CharacterRule(EnglishCharacterData.Digit, 1),
                        new CharacterRule(EnglishCharacterData.Special, 1),
                        new WhitespaceRule()
                )
        );
        RuleResult result = passwordValidator.validate(new PasswordData(password));
        if(result.isValid()) {
            return true;
        }
        List<String> messages = passwordValidator.getMessages(result);
        String messageTemplate = String.join(",", messages);
        constraintValidatorContext.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
