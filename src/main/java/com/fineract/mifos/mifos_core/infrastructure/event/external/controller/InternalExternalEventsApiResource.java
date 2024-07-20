package com.fineract.mifos.mifos_core.infrastructure.event.external.controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Profile(FineractProfiles.TEST)
@RestController
@RequestMapping("/v1/internal/externalevents")
@RequiredArgsConstructor
@Slf4j
public class InternalExternalEventsApiResource implements InitializingBean {
    private final InternalExternalEventService internalExternalEventService;
    private final DefaultToApiJsonSerializer<List<ExternalEventDTO>> jsonSerializer;

    @Override
    @SuppressFBWarnings("SLF4J_SIGN_ONLY_FORMAT")
    public void afterPropertiesSet() throws Exception {
        log.warn("------------------------------------------------------------");
        log.warn("                                                            ");
        log.warn("DO NOT USE THIS IN PRODUCTION!");
        log.warn("Internal client services mode is enabled");
        log.warn("DO NOT USE THIS IN PRODUCTION!");
        log.warn("                                                            ");
        log.warn("------------------------------------------------------------");
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON)

    public String getAllExternalEvents(@QueryParam("idempotencyKey") final String idempotencyKey, @QueryParam("type") final String type,
                                       @QueryParam("category") final String category, @QueryParam("aggregateRootId") final Long aggregateRootId) {
        log.debug("getAllExternalEvents called with params idempotencyKey:{}, type:{}, category:{}, aggregateRootId:{}  ", idempotencyKey,
                type, category, aggregateRootId);
        List<ExternalEventDTO> allExternalEvents = internalExternalEventService.getAllExternalEvents(idempotencyKey, type, category,
                aggregateRootId);
        return jsonSerializer.serialize(allExternalEvents);
    }

    @DeleteMapping
    public void deleteAllExternalEvents() {
        log.debug("deleteAllExternalEvents called");
        internalExternalEventService.deleteAllExternalEvents();
    }

}

