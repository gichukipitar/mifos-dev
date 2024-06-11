package com.fineract.mifos.mifos_core.accounting.repository;

import com.fineract.mifos.mifos_core.accounting.entity.GLAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GLAccountRepository extends JpaRepository<GLAccount, Long>, JpaSpecificationExecutor<GLAccount> {

    // no added behaviour
    // adding behaviour to fetch id by glcode for opening balance bulk import
    Optional<GLAccount> findOneByGlCode(String glCode);
}
