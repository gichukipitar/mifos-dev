package com.fineract.mifos.mifos_core.infrastructure.cache.entity;


import com.fineract.mifos.mifos_core.infrastructure.cache.dto.CacheType;
import com.fineract.mifos.mifos_core.infrastructure.core.entity.AbstractPersistableCustom;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "c_cache")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PlatformCache extends AbstractPersistableCustom {

    @Column(name = "cache_type_enum")
    private Integer cacheType;

    public boolean isNoCachedEnabled() {
        return CacheType.fromInt(this.cacheType).isNoCache();
    }

    public boolean isEhcacheEnabled() {
        return CacheType.fromInt(this.cacheType).isEhcache();
    }

    public boolean isDistributedCacheEnabled() {
        return CacheType.fromInt(this.cacheType).isDistributedCache();
    }

}
