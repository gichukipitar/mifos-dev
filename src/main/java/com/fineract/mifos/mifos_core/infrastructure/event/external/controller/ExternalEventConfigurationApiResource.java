package com.fineract.mifos.mifos_core.infrastructure.event.external.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/externalevents/configuration")
@Tag(name = "External event configuration", description = "External event configuration enables user to enable/disable event posting to downstream message channel")

public class ExternalEventConfigurationApiResource {
}
