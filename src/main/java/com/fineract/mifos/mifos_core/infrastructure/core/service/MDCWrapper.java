package com.fineract.mifos.mifos_core.infrastructure.core.service;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class MDCWrapper {

    public void put(String key, String val) {
        MDC.put(key, val);
    }

    public void remove(String key) {
        MDC.remove(key);
    }
}
