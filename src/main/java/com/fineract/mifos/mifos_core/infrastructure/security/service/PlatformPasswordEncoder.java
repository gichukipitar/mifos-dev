package com.fineract.mifos.mifos_core.infrastructure.security.service;

public interface PlatformPasswordEncoder {
    String encode(PlatformUser appUser);
}
