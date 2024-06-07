package com.fineract.mifos.core.infrastructure.security.service;

public interface PlatformPasswordEncoder {
    String encode(PlatformUser appUser);
}
