package com.daniilmiskevich.labs.space.controller.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;
import java.util.stream.Collectors;

import com.daniilmiskevich.labs.space.model.Spark;
import com.daniilmiskevich.labs.space.model.Spectre;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

public record SparkRequestDto(
    @NotBlank(message = "Name should not be blank") String name,
    @JsonProperty("spectre_names")
    Set<@SpectreName(message = "Spectre names should only contain ") String> spectreNames) {

    public Spark toSpark(Long id) {
        return new Spark(
            id,
            name,
            spectreNames != null
                ? spectreNames
                .stream()
                .map(Spectre::new)
                .collect(Collectors.toSet())
                : null);

    }

    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = SpectreNameValidator.class)
    public @interface SpectreName {
        String message();

        boolean doAcceptPatterns() default false;
    }

    private static class SpectreNameValidator implements ConstraintValidator<SpectreName, String> {

        private boolean doAcceptPatterns;

        @Override
        public void initialize(SpectreName annotation) {
            this.doAcceptPatterns = annotation.doAcceptPatterns();
        }

        @Override
        public boolean isValid(String name, ConstraintValidatorContext context) {
            if (name == null) {
                return doAcceptPatterns;
            }
            return name.matches("^[a-z][a-z0-9-_]*$");
        }
    }
}
