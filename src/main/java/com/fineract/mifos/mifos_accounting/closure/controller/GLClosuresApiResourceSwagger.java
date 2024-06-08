package com.fineract.mifos.mifos_accounting.closure.controller;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

final class GLClosuresApiResourceSwagger {

    private GLClosuresApiResourceSwagger() {
        // don't allow to instantiate; use only for live API documentation
    }

    /**
     * TODO: describe where this belongs: {@link GLClosuresApiResource } {@link GLClosuresApiResource}
     */
    // Check !!

    @Schema(description = "GetGLClosureResponse")
    public static final class GetGlClosureResponse {

        private GetGlClosureResponse() {
            // dont allow to initiatiate
        }

        @Schema(example = "7")
        public Long id;
        @Schema(example = "1")
        public Long officeId;
        @Schema(example = "Head Office")
        public String officeName;
        @Schema(example = "2013,1,2")
        public LocalDate closingDate;
        @Schema(example = "false")
        public boolean deleted;
        @Schema(example = "2013,1,3")
        public LocalDate createdDate;
        @Schema(example = "2013,1,3")
        public LocalDate lastUpdatedDate;
        @Schema(example = "1")
        public Long createdByUserId;
        @Schema(example = "mifos")
        public String createdByUsername;
        @Schema(example = "1")
        public Long lastUpdatedByUserId;
        @Schema(example = "mifos")
        public String lastUpdatedByUsername;
        @Schema(example = "closed")
        public String comments;

    }

    @Schema(description = "PostGLCLosuresRequest")
    public static final class PostGlClosuresRequest {

        private PostGlClosuresRequest() {
            // don't allow to instantiate; use only for live API documentation
        }

        @Schema(example = "1")
        public Long officeId;
        @Schema(example = "06 December 2012")
        public LocalDate closingDate;
        @Schema(example = "The accountants are heading for a carribean vacation")
        public String comments;
        @Schema(example = "en")
        public String locale;
        @Schema(example = "dd MMMM yyyy")
        public String dateFormat;
    }

    @Schema(description = "PostGlClosuresResponse")
    public static final class PostGlClosuresResponse {

        private PostGlClosuresResponse() {
            // don't allow to instantiate; use only for live API documentation
        }

        @Schema(example = "1")
        public Long officeId;
        @Schema(example = "9")
        public Long resourceId;
    }

    @Schema(description = "PutGlClosuresRequest")
    public static final class PutGlClosuresRequest {

        private PutGlClosuresRequest() {
            // don't allow to instantiate; use only for live API documentation
        }

        @Schema(example = "All transactions verified by Johnny Cash")
        public String comments;
    }

    @Schema(description = "PutGlClosuresResponse")
    public static final class PutGlClosuresResponse {

        private PutGlClosuresResponse() {
            // don't allow to instantiate; use only for live API documentation
        }

        @Schema(example = "1")
        public Long officeId;
        @Schema(example = "9")
        public Long resourceId;
        @Schema(example = "All transactions verified by Johnny Cash")
        public String comments;
    }

    @Schema(description = "DeleteGlClosuresResponse")
    public static final class DeleteGlClosuresResponse {

        private DeleteGlClosuresResponse() {

        }

        @Schema(example = "1")
        public Long officeId;
        @Schema(example = "9")
        public Long resourceId;
    }
}
