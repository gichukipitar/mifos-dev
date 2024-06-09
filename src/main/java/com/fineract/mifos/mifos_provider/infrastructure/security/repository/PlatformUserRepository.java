package com.fineract.mifos.mifos_provider.infrastructure.security.repository;

import com.fineract.mifos.mifos_core.infrastructure.security.service.PlatformUser;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformUserRepository {
    PlatformUser findByUsernameAndDeletedAndEnabled(String username, boolean deleted, boolean enabled);
}
