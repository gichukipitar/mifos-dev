package com.fineract.mifos.mifos_accounting.accrual.service;

import com.fineract.mifos.mifos_core.commands.annotation.CommandType;
import com.fineract.mifos.mifos_core.commands.handler.NewCommandSourceHandler;
import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@CommandType(entity = "PERIODICACCRUALACCOUNTING", action = "EXECUTE")
public class ExecutePeriodicAccrualCommandHandler implements NewCommandSourceHandler {

    private final AccrualAccountingWritePlatformService writePlatformService;

    @Transactional
    @Override
    public CommandProcessingResult processCommand(final JsonCommand command) {
        return this.writePlatformService.executeLoansPeriodicAccrual(command);
    }
}
