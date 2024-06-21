package com.fineract.mifos.mifos_core.infrastructure.cache.repository;

import com.fineract.mifos.mifos_core.infrastructure.cache.entity.PlatformCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformCacheRepository extends JpaRepository <PlatformCache, Long>, JpaSpecificationExecutor<PlatformCache> {
}
