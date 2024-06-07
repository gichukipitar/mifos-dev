package com.fineract.mifos.accounting.closure.controller;

import com.fineract.mifos.accounting.closure.dto.GLClosureData;
import com.fineract.mifos.core.commands.service.PortfolioCommandSourceWritePlatformService;
import com.fineract.mifos.core.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import com.fineract.mifos.core.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import com.fineract.mifos.core.infrastructure.security.service.PlatformSecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/v1/glclosures")
@Tag(name = "Accounting Closure", description = "An accounting closure indicates that no more journal entries may be logged (or reversed) in the system, either manually or via the portfolio with an entry date prior to the defined closure date\n"
        + "\n" + "Field Descriptions\n" + "closingDate\n" + "The date for which the accounting closure is defined\n" + "officeId\n"
        + "The identifer of the branch for which accounting has been closed\n" + "comments\n"
        + "Description associated with an Accounting closure")
@RequiredArgsConstructor
public class GLClosuresApiResource {
    private static final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<>(
            Arrays.asList("id", "officeId", "officeName", "closingDate", "deleted", "createdDate", "lastUpdatedDate", "createdByUserId",
                    "createdByUsername", "lastUpdatedByUserId", "lastUpdatedByUsername"));

    private static final String RESOURCE_NAME_FOR_PERMISSION = "GLCLOSURE";

    private final PlatformSecurityContext context;
    private final GLClosureReadPlatformService glClosureReadPlatformService;
    private final DefaultToApiJsonSerializer<GLClosureData> apiJsonSerializerService;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
    private final OfficeReadPlatformService officeReadPlatformService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List Accounting closures", description = "Example Requests:\n" + "\n" + "glclosures")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = GLClosuresApiResourceSwagger.GetGlClosureResponse.class))))
    })

    public ResponseEntity<String> retreiveClosure(@PathVariable("glClosureId") @Parameter(description = "glClosureId") final Long glClosureId,
                                                  @Context final UriInfo uriInfo) {

        this.context.authenticatedUser().validateHasReadPermission(RESOURCE_NAME_FOR_PERMISSION);

        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());

        final GLClosureData glClosureData = this.glClosureReadPlatformService.retrieveGLClosureById(glClosureId);
        if (settings.isTemplate()) {
            glClosureData.setAllowedOffices(this.officeReadPlatformService.retrieveAllOfficesForDropdown());
        }

        final String responseBody = this.apiJsonSerializerService.serialize(settings, glClosureData, RESPONSE_DATA_PARAMETERS);
        return ResponseEntity.ok(responseBody);
    }

}
