package com.daniilmiskevich.labs.space.controller.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.daniilmiskevich.labs.space.model.Spark;
import com.daniilmiskevich.labs.space.model.Spectre;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Schema(description = "Request DTO for creating or updating a spark")
public record SparkRequestDto(
    @Schema(
        description = "Name of the spark",
        example = "This is an example spark name.")
    String name,
    @Schema(
        description = "Set of spectre names associated with the spark",
        example = "[\"foo\", \"bar\", \"foo-bar\"]",
        pattern = "^[a-z][a-z-0-9]*$")
    @JsonProperty("spectre_names") Set<@SpectreName String> spectreNames) {

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
        String DEFAULT_MESSAGE =
            "Spectre name should only contain lowercase latin letters, digits and hyphens.";
        String PATTERN_MESSAGE =
            "Spectre pattern should be a comma-separated list of names,"
                + "which should only contain lowercase latin letters, digits and hyphens.";

        String message() default DEFAULT_MESSAGE;

        boolean doAcceptPatterns() default false;

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
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
            return Arrays.stream(doAcceptPatterns ? name.split(",") : new String[]{name})
                .allMatch(n -> n.matches("^[a-z][a-z0-9-]*$"));
        }
    }
}
