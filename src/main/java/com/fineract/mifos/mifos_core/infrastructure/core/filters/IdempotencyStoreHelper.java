package com.fineract.mifos.mifos_core.infrastructure.core.filters;

import com.fineract.mifos.mifos_core.commands.repository.CommandSourceRepository;
import com.fineract.mifos.mifos_core.commands.service.CommandSourceService;
import com.fineract.mifos.mifos_core.commands.service.SynchronousCommandProcessingService;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.FineractRequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IdempotencyStoreHelper {

    private final CommandSourceRepository commandSourceRepository;
    private final CommandSourceService commandSourceService;
    private final FineractRequestContextHolder fineractRequestContextHolder;

    public void storeCommandResult(Integer response, String body, Long commandId) {
        commandSourceRepository.findById(commandId).ifPresent(commandSource -> {
            commandSource.setResultStatusCode(response);
            commandSource.setResult(body);
            commandSourceService.saveResultSameTransaction(commandSource);
        });
    }

    public boolean isAllowedContentTypeResponse(HttpServletResponse response) {
        return Optional.ofNullable(response.getContentType()).map(String::toLowerCase).map(ct -> ct.contains("application/json"))
                .orElse(false) || (response.getStatus() > 200 && response.getStatus() < 300);
    }

    public boolean isAllowedContentTypeRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getContentType()).map(String::toLowerCase).map(ct -> ct.contains("application/json"))
                .orElse(false);
    }

    public boolean isStoreIdempotencyKey(HttpServletRequest request) {
        return Optional
                .ofNullable(
                        fineractRequestContextHolder.getAttribute(SynchronousCommandProcessingService.IDEMPOTENCY_KEY_STORE_FLAG, request))
                .filter(Boolean.class::isInstance).map(Boolean.class::cast).orElse(false);
    }

    public Optional<Long> getCommandId(HttpServletRequest request) {
        return Optional
                .ofNullable(fineractRequestContextHolder.getAttribute(SynchronousCommandProcessingService.COMMAND_SOURCE_ID, request))
                .filter(Long.class::isInstance).map(Long.class::cast);
    }
}
