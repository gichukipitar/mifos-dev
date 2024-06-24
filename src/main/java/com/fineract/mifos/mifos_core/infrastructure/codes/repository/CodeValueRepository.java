package com.fineract.mifos.mifos_core.infrastructure.codes.repository;

import com.fineract.mifos.mifos_core.infrastructure.codes.entity.CodeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CodeValueRepository extends JpaRepository<CodeValue, Long>, JpaSpecificationExecutor<CodeValue> {

    CodeValue findByCodeNameAndId(String codeName, Long id);

    CodeValue findByCodeNameAndLabel(String codeName, String label);

}
