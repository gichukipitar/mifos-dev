package com.fineract.mifos.mifos_core.infrastructure.businessdate.handler;

import com.fineract.mifos.mifos_core.commands.annotation.CommandType;
import com.fineract.mifos.mifos_core.commands.handler.NewCommandSourceHandler;
import com.fineract.mifos.mifos_core.infrastructure.businessdate.service.BusinessDateWritePlatformService;
import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@CommandType(entity = "BUSINESS_DATE", action = "UPDATE")
public class BusinessDateUpdateHandler implements NewCommandSourceHandler {
    private final BusinessDateWritePlatformService businessDateWritePlatformService;

    @Transactional
    @Override
    public CommandProcessingResult processCommand(@NotNull final JsonCommand command) {
        return businessDateWritePlatformService.updateBusinessDate(command);
    }
}
