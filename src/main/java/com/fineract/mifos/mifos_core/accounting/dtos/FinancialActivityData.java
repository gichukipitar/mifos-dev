package com.fineract.mifos.mifos_core.accounting.dtos;

import com.fineract.mifos.mifos_core.accounting.service.GLAccountType;
import lombok.Data;

@Data
public class FinancialActivityData {
    private final Integer id;
    private final String name;
    private final GLAccountType mappedGLAccountType;
}
