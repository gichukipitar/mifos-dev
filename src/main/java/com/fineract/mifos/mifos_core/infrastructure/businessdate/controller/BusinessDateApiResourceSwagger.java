package com.fineract.mifos.mifos_core.infrastructure.businessdate.controller;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public final class BusinessDateApiResourceSwagger {
    private BusinessDateApiResourceSwagger() {

    }
    @Schema(description = "BusinessDateResponse")
    public static final class BusinessDateResponse {

        @Schema(example = "COB date")
        public String description;
        @Schema(example = "COB_DATE")
        public String type;
        @Schema(example = "[2015,02,15]")
        public LocalDate date;

        private BusinessDateResponse() {

        }
    }

    @Schema(description = "BusinessDateRequest")
    public static final class BusinessDateRequest {

        @Schema(example = "yyyy-MM-dd")
        public String dateFormat;
        @Schema(example = "COB_DATE")
        public String type;
        @Schema(example = "2015-02-15")
        public String date;
        @Schema(example = "en")
        public String locale;

        private BusinessDateRequest() {}

    }
}
