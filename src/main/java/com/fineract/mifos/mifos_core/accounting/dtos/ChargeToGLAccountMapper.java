package com.fineract.mifos.mifos_core.accounting.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@RequiredArgsConstructor
@Data
@Accessors(chain = true)
public class ChargeToGLAccountMapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private ChargeData charge;
    private GLAccountData incomeAccount;
}
