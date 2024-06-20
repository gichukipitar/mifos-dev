package com.fineract.mifos.mifos_core.infrastructure.cache.dto;

import com.fineract.mifos.mifos_core.infrastructure.core.dto.EnumOptionData;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public final class CacheData {

    @SuppressWarnings("unused")
    private EnumOptionData cacheType;
    @SuppressWarnings("unused")
    private boolean enabled;

    public static CacheData instance(final EnumOptionData cacheType, final boolean enabled) {
        return new CacheData().setCacheType(cacheType).setEnabled(enabled);
    }
}
