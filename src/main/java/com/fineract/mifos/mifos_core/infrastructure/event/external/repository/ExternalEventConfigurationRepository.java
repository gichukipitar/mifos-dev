package com.fineract.mifos.mifos_core.infrastructure.event.external.repository;

import com.fineract.mifos.mifos_core.infrastructure.event.external.entity.ExternalEventConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalEventConfigurationRepository
        extends JpaRepository<ExternalEventConfiguration, String>, CustomExternalEventConfigurationRepository {}
