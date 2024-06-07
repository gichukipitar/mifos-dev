package com.fineract.mifos.accounting.accrual.service;

import com.fineract.mifos.core.commands.annotation.CommandType;
import com.fineract.mifos.core.commands.handler.NewCommandSourceHandler;
import com.fineract.mifos.core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.core.infrastructure.core.data.CommandProcessingResult;
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
