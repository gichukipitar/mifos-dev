package com.fineract.mifos.mifos_core.infrastructure.core.condition;

import com.fineract.mifos.mifos_core.infrastructure.core.boot.FineractProfiles;

import java.util.List;

public class FineractLiquibaseOnlyApplicationCondition extends ProfileCondition{

    @Override
    protected boolean matches(List<String> activeProfiles) {
        return activeProfiles.contains(FineractProfiles.LIQUIBASE_ONLY);
    }

}
