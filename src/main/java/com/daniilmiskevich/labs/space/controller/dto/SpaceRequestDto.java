package com.daniilmiskevich.labs.space.controller.dto;

import com.daniilmiskevich.labs.space.model.Space;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public record SpaceRequestDto(
    @Name(message = "Name should contain only latin letters, digits, hyphens and underscores.")
    String name) {

    public Space toSpace(Long id) {
        return new Space(id, name);
    }

    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = NameValidator.class)
    public @interface Name {
        String message();

        boolean doAcceptPatterns() default false;
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
            return (doAcceptPatterns ? name : name.replace("*", ""))
                .matches("^[A-Za-z][A-Za-z-_0-9]*$");
        }
    }

}
