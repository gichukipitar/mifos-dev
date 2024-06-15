package com.fineract.mifos.mifos_core.infrastructure.accountnumberformat.dto;

import com.fineract.mifos.mifos_core.infrastructure.core.dto.EnumOptionData;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
public class AccountNumberFormatData implements Serializable {

    private final Long id;
    private final EnumOptionData accountType;
    private final EnumOptionData prefixType;

    // template options
    private List<EnumOptionData> accountTypeOptions;
    private Map<String, List<EnumOptionData>> prefixTypeOptions;

    private String prefixCharacter;

    public AccountNumberFormatData(final Long id, final EnumOptionData accountType, final EnumOptionData prefixType,
                                   final String prefixCharacter) {
        this(id, accountType, prefixType, null, null, prefixCharacter);
    }

    public AccountNumberFormatData(final List<EnumOptionData> accountTypeOptions, Map<String, List<EnumOptionData>> prefixTypeOptions) {
        this(null, null, null, accountTypeOptions, prefixTypeOptions, null);
    }

    public void templateOnTop(List<EnumOptionData> accountTypeOptions, Map<String, List<EnumOptionData>> prefixTypeOptions) {
        this.accountTypeOptions = accountTypeOptions;
        this.prefixTypeOptions = prefixTypeOptions;
    }

    private AccountNumberFormatData(final Long id, final EnumOptionData accountType, final EnumOptionData prefixType,
                                    final List<EnumOptionData> accountTypeOptions, Map<String, List<EnumOptionData>> prefixTypeOptions,
                                    final String prefixCharacter) {
        this.id = id;
        this.accountType = accountType;
        this.prefixType = prefixType;
        this.accountTypeOptions = accountTypeOptions;
        this.prefixTypeOptions = prefixTypeOptions;
        this.prefixCharacter = prefixCharacter;
    }

}
