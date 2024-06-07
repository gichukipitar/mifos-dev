package com.fineract.mifos.accounting.accrual.controller;

import io.swagger.v3.oas.annotations.media.Schema;

final class AccrualAccountingApiResourceSwagger {

    private AccrualAccountingApiResourceSwagger() {
        // don't allow to instantiate; use only for live API documentation
    }

    @Schema(description = "runaccrualsRequest")
    public static final class PostRunaccrualsRequest {

        private PostRunaccrualsRequest() {
            // don't allow to instantiate; use only for live API documentation
        }

        @Schema(example = "en")
        public String locale;
        @Schema(example = "dd MMMM yyyy")
        public String dateFormat;
        @Schema(example = "04 June 2014", description = "which specifies periodic accruals should happen till the given Date", required = true)
        public String tillDate;
    }

}
