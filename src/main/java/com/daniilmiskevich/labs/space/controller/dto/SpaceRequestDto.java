package com.daniilmiskevich.labs.space.controller.dto;

import com.daniilmiskevich.labs.space.model.Space;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public record SpaceRequestDto(@Name String name) {

    public Space toSpace(Long id) {
        return new Space(id, name);
    }

    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = NameValidator.class)
    public @interface Name {
        public static String DEFAULT_MESSAGE =
            "Name should contain only latin letters, digits, hyphens and underscores.";
        public static String PATTERN_MESSAGE =
            "Name pattern should contain only latin letters, digits, hyphens and underscores;"
                + "additionaly, asterisk can be used to match any sequence of characters.";

        String message() default DEFAULT_MESSAGE;

        boolean doAcceptPatterns() default false;

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    private static class NameValidator implements ConstraintValidator<Name, String> {
        private boolean doAcceptPatterns;

        @Override
        public void initialize(Name annotation) {
            this.doAcceptPatterns = annotation.doAcceptPatterns();
        }

        @Override
        public boolean isValid(String name, ConstraintValidatorContext context) {
            if (name == null) {
                return doAcceptPatterns;
            }
            return (doAcceptPatterns ? name.replace("*", "") : name)
                .matches("^[A-Za-z][A-Za-z-_0-9]*$");
        }
    }

}
