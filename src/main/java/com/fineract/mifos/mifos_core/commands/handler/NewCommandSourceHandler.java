package com.fineract.mifos.mifos_core.commands.handler;

import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;

public interface NewCommandSourceHandler {

    CommandProcessingResult processCommand(JsonCommand command);
}
