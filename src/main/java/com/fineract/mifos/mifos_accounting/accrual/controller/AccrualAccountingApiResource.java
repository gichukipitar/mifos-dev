package com.fineract.mifos.mifos_accounting.accrual.controller;

import com.fineract.mifos.mifos_core.commands.domain.CommandWrapper;
import com.fineract.mifos.mifos_core.commands.service.CommandWrapperBuilder;
import com.fineract.mifos.mifos_core.commands.service.PortfolioCommandSourceWritePlatformService;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/runaccruals")
@RequiredArgsConstructor
@Component
@Tag(name = "Periodic Accrual Accounting", description = "Periodic Accrual is to accrue the loan income till the specific date or till batch job scheduled time.\n")

public class AccrualAccountingApiResource {
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
    private final DefaultToApiJsonSerializer<String> apiJsonSerializerService;

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Executes Periodic Accrual Accounting", description = "Mandatory Fields: tillDate")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "OK") })

    public String executePeriodicAccrualAccounting(@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = AccrualAccountingApiResourceSwagger.PostRunaccrualsRequest.class))) String jsonRequestBody) {

        CommandWrapper commandRequest = new CommandWrapperBuilder().excuteAccrualAccounting().withJson(jsonRequestBody).build();
        CommandProcessingResult result = commandsSourceWritePlatformService.logCommandSource(commandRequest);

        return apiJsonSerializerService.serialize(result);
    }
}

