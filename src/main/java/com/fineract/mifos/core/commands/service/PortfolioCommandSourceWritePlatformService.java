package com.fineract.mifos.core.commands.service;

import com.fineract.mifos.core.commands.domain.CommandWrapper;
import com.fineract.mifos.core.infrastructure.core.data.CommandProcessingResult;

public interface PortfolioCommandSourceWritePlatformService {

    CommandProcessingResult logCommandSource(CommandWrapper commandRequest);

    CommandProcessingResult approveEntry(Long id);

    Long rejectEntry(Long id);

    Long deleteEntry(Long makerCheckerId);
}
