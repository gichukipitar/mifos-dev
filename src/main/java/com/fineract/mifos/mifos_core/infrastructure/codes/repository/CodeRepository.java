package com.fineract.mifos.mifos_core.infrastructure.codes.repository;

import com.fineract.mifos.mifos_core.infrastructure.codes.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CodeRepository extends JpaRepository<Code, Long>, JpaSpecificationExecutor<Code> {

    Code findOneByName(String name);

}
