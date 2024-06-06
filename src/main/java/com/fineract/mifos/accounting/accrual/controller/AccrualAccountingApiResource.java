package com.fineract.mifos.accounting.accrual.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/runaccruals")
@RequiredArgsConstructor
@Component
@Tag(name = "Periodic Accrual Accounting", description = "Periodic Accrual is to accrue the loan income till the specific date or till batch job scheduled time.\n")

public class AccrualAccountingApiResource {

}
