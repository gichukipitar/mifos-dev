package com.fineract.mifos.mifos_core.accounting.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PaymentTypeToGLAccountMapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private PaymentTypeData paymentType;
    private GLAccountData fundSourceAccount;
}
