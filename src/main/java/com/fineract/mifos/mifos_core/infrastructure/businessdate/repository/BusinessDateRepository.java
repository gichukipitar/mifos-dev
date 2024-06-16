package com.fineract.mifos.mifos_core.infrastructure.businessdate.repository;

import com.fineract.mifos.mifos_core.infrastructure.businessdate.entity.BusinessDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessDateRepository extends JpaRepository <BusinessDate, Long>, JpaSpecificationExecutor<BusinessDate> {
    Optional<BusinessDate> findByType(BusinessDateType type);
}
