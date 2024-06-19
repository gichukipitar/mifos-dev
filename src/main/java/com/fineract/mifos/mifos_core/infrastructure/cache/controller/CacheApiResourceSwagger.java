package com.fineract.mifos.mifos_core.infrastructure.cache.controller;

import com.fineract.mifos.mifos_core.infrastructure.core.dto.EnumOptionData;
import io.swagger.v3.oas.annotations.media.Schema;

public class CacheApiResourceSwagger {
    private CacheApiResourceSwagger() {

    }
    @Schema(description = "GetCachesResponse")
    public static final class GetCachesResponse {

        private GetCachesResponse() {

        }

        public EnumOptionData cacheType;
        public boolean enabled;
    }

    @Schema(description = "PutCachesRequest")
    public static final class PutCachesRequest {

        private PutCachesRequest() {

        }

        @Schema(example = "2")
        public Long cacheType;

    }

    @Schema(description = "PutCachesResponse")
    public static final class PutCachesResponse {

        private PutCachesResponse() {

        }

        public static final class PutCachechangesSwagger {

            private PutCachechangesSwagger() {

            }

            @Schema(example = "2")
            public Long cacheType;

        }

        public PutCachechangesSwagger cacheType;

    }
}
