package com.fineract.mifos.mifos_core.commands.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdempotencyKeyGenerator {
    public String create() {
        return UUID.randomUUID().toString();
    }
}
