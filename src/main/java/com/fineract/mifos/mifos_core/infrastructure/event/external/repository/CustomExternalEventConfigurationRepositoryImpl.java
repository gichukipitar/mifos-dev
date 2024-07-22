package com.fineract.mifos.mifos_core.infrastructure.event.external.repository;

import com.fineract.mifos.mifos_core.infrastructure.event.external.entity.ExternalEventConfiguration;
import com.fineract.mifos.mifos_core.infrastructure.event.external.exception.ExternalEventConfigurationNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomExternalEventConfigurationRepositoryImpl implements CustomExternalEventConfigurationRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public ExternalEventConfiguration findExternalEventConfigurationByTypeWithNotFoundDetection(String externalEventType) {
        final ExternalEventConfiguration configuration = entityManager.find(ExternalEventConfiguration.class, externalEventType);
        if (configuration == null) {
            throw new ExternalEventConfigurationNotFoundException(externalEventType);
        }
        return configuration;
    }
}
