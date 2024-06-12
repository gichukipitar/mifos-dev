package com.fineract.mifos.mifos_core.batch.controller;

import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

public class BatchApiResourceSwagger {
    private BatchApiResourceSwagger() {

    }

    @Schema(description = "PostBatchesRequest")
    public static final class PostBatchesRequest {

        private PostBatchesRequest() {

        }

        public static final class PostBodyRequestSwagger {

            private PostBodyRequestSwagger() {

            }

            @Schema(example = "1")
            public Long officeId;
            @Schema(example = "\"Petra\"")
            public String firstname;
            @Schema(example = "\"Yton\"")
            public String lastname;
            @Schema(example = "\"ex_externalId1\"")
            public String externalId;
            @Schema(example = "\"dd MMMM yyyy\"")
            public String dateFormat;
            @Schema(example = "\"en\"")
            public String locale;
            @Schema(example = "true")
            public boolean active;
            @Schema(example = "\"04 March 2009\"")
            public String activationDate;
            @Schema(example = "\"04 March 2009\"")
            public String submittedOnDate;

        }

        @Schema(example = "1")
        public Long requestId;
        @Schema(example = "clients")
        public String relativeUrl;
        @Schema(example = "POST")
        public String method;
        public Set<Header> headers;
        @Schema(example = "1")
        public Long reference;
        public PostBodyRequestSwagger body;
    }
}

