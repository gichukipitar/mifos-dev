package com.fineract.mifos.mifos_core.infrastructure.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DataEnricherProcessor {

    private final List<DataEnricher<?>> enhancers;

    @Autowired
    public DataEnricherProcessor(Optional<List<DataEnricher<?>>> enhancers) {
        this.enhancers = enhancers.orElse(new ArrayList<>());
    }

    public <T> T enrich(T source) {
        for (DataEnricher enhancer : enhancers) {
            if (enhancer.isDataTypeSupported(source.getClass())) {
                enhancer.enrich(source);
            }
        }
        return source;
    }
}
