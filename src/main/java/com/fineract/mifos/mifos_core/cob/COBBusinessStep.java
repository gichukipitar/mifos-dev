package com.fineract.mifos.mifos_core.cob;

import com.fineract.mifos.mifos_core.infrastructure.core.domain.AbstractPersistableCustom;

public interface COBBusinessStep <T extends AbstractPersistableCustom>{
    T execute(T input);

    String getEnumStyledName();

    String getHumanReadableName();
}
