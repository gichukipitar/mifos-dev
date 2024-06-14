package com.fineract.mifos.mifos_core.commands.service;

import com.fineract.mifos.mifos_core.commands.dtos.CommandWrapper;
import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import com.fineract.mifos.mifos_core.useradministration.entity.AppUser;
import org.springframework.stereotype.Component;

@Component
public interface CommandProcessingService {
    CommandProcessingResult executeCommand(CommandWrapper wrapper, JsonCommand command, boolean isApprovedByChecker);

    boolean validateRollbackCommand(CommandWrapper commandWrapper, AppUser user);

}
