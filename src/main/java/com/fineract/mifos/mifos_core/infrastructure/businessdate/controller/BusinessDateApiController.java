package com.fineract.mifos.mifos_core.infrastructure.businessdate.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/businessdate")
@RequiredArgsConstructor
@Tag(name = "Business Date Management", description = "Business date management enables you to set up, fetch and adjust organisation business dates")

public class BusinessDateApiController {
}
