package com.fineract.mifos.mifos_core.infrastructure.accountnumberformat.dto;

public final class AccountNumberFormatConstants {
    private AccountNumberFormatConstants() {

    }
    // resource name for validation
    public static final String ENTITY_NAME = "accountNumberFormat";

    // general
    public static final String localeParamName = "locale";
    public static final String dateFormatParamName = "dateFormat";

    // REST end point
    public static final String resourceRelativeURL = "/v1/accountnumberformats";

    // request parameters
    public static final String idParamName = "id";
    public static final String accountTypeParamName = "accountType";
    public static final String prefixTypeParamName = "prefixType";
    public static final String prefixCharacterParamName = "prefixCharacter";

    // response parameters

    // associations related part of response

    // template related part of response
    public static final String accountTypeOptionsParamName = "accountTypeOptions";
    public static final String prefixTypeOptionsParamName = "prefixTypeOptions";

    /**
     * These parameters will match the class level parameters of {@link AccountNumberFormatData}. Where possible, we try
     * to get response parameters to match those of request parameters.
     */

    // Error messages codes
    public static final String EXCEPTION_DUPLICATE_ACCOUNT_TYPE = "error.msg.account.number.format.duplicate.account.type";
    public static final String EXCEPTION_ACCOUNT_NUMBER_FORMAT_NOT_FOUND = "error.msg.account.number.format.id.invalid";
    // JPA related constants
    public static final String ACCOUNT_NUMBER_FORMAT_TABLE_NAME = "c_account_number_format";
    public static final String ACCOUNT_TYPE_ENUM_COLUMN_NAME = "account_type_enum";
    public static final String PREFIX_TYPE_ENUM_COLUMN_NAME = "prefix_type_enum";
    public static final String ACCOUNT_TYPE_UNIQUE_CONSTRAINT_NAME = "account_type_enum";
    public static final String PREFIX_CHARACTER_COLUMN_NAME = "prefix_character";

}
