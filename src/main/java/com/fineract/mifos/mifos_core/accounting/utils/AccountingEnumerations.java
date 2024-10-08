package com.fineract.mifos.mifos_core.accounting.utils;

import com.fineract.mifos.mifos_core.accounting.dtos.JournalEntryType;
import com.fineract.mifos.mifos_core.accounting.service.GLAccountType;
import com.fineract.mifos.mifos_core.accounting.service.GLAccountUsage;
import com.fineract.mifos.mifos_core.accounting.service.PortfolioProductType;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.EnumOptionData;

import java.util.ArrayList;
import java.util.List;

public final class AccountingEnumerations {
    public AccountingEnumerations() {
    }
    public static EnumOptionData gLAccountType(final int id) {
        return gLAccountType(GLAccountType.fromInt(id));
    }

    public static EnumOptionData gLAccountType(final GLAccountType accountType) {
        final EnumOptionData optionData = new EnumOptionData(accountType.getValue().longValue(), accountType.getCode(),
                accountType.toString());
        return optionData;
    }

    public static List<EnumOptionData> gLAccountType(final GLAccountType[] accountTypes) {
        final List<EnumOptionData> optionDatas = new ArrayList<>();
        for (final GLAccountType accountType : accountTypes) {
            optionDatas.add(gLAccountType(accountType));
        }
        return optionDatas;
    }

    public static EnumOptionData gLAccountUsage(final int id) {
        return gLAccountUsage(GLAccountUsage.fromInt(id));
    }

    public static EnumOptionData gLAccountUsage(final GLAccountUsage accountUsage) {
        final EnumOptionData optionData = new EnumOptionData(accountUsage.getValue().longValue(), accountUsage.getCode(),
                accountUsage.toString());
        return optionData;
    }

    public static List<EnumOptionData> gLAccountUsage(final GLAccountUsage[] accountUsages) {
        final List<EnumOptionData> optionDatas = new ArrayList<>();
        for (final GLAccountUsage accountUsage : accountUsages) {
            optionDatas.add(gLAccountUsage(accountUsage));
        }
        return optionDatas;
    }

    public static EnumOptionData journalEntryType(final int id) {
        return journalEntryType(JournalEntryType.fromInt(id));
    }

    public static EnumOptionData journalEntryType(final JournalEntryType journalEntryType) {
        final EnumOptionData optionData = new EnumOptionData(journalEntryType.getValue().longValue(), journalEntryType.getCode(),
                journalEntryType.toString());
        return optionData;
    }

    public static List<EnumOptionData> journalEntryTypes(final JournalEntryType[] journalEntryTypes) {
        final List<EnumOptionData> optionDatas = new ArrayList<>();
        for (final JournalEntryType journalEntryType : journalEntryTypes) {
            optionDatas.add(journalEntryType(journalEntryType));
        }
        return optionDatas;
    }

    public static EnumOptionData portfolioProductType(final int id) {
        return portfolioProductType(PortfolioProductType.fromInt(id));
    }

    public static EnumOptionData portfolioProductType(final PortfolioProductType portfolioProductType) {
        final EnumOptionData optionData = new EnumOptionData(portfolioProductType.getValue().longValue(), portfolioProductType.getCode(),
                portfolioProductType.toString());
        return optionData;
    }

    public static EnumOptionData accountingRuleType(final int id) {
        return accountingRuleType(AccountingRuleType.fromInt(id));
    }

    public static EnumOptionData accountingRuleType(final AccountingRuleType type) {
        final EnumOptionData optionData = new EnumOptionData(type.getValue().longValue(), type.getCode(), type.toString());
        return optionData;
    }

    public static List<EnumOptionData> accountingRuleTypes(final AccountingRuleType[] accountingRuleTypes) {
        final List<EnumOptionData> optionDatas = new ArrayList<>();
        for (final AccountingRuleType accountingRuleType : accountingRuleTypes) {
            optionDatas.add(accountingRuleType(accountingRuleType));
        }
        return optionDatas;
    }


}
