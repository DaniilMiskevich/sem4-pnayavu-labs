package com.daniilmiskevich.labs.dev.controller;

import com.daniilmiskevich.labs.dev.aspect.VisitsCounterAspect;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/dev/visits_counts")
@Hidden
public class VisitsCounterController {

    private final VisitsCounterAspect aspect;

    public VisitsCounterController(VisitsCounterAspect aspect) {
        this.aspect = aspect;
    }

    @GetMapping(value = "")
    public Map<String, Long> getVisitsCounts(
        @RequestParam(name = "method_name", required = false) String methodName) {
        return methodName == null
            ? aspect.getVisitsCounts()
            : Map.of(methodName, Optional
            .ofNullable(aspect.getVisitsCounts().get(methodName))
            .orElse(0L));
    }

}
