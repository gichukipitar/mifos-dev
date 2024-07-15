package com.fineract.mifos.mifos_core.infrastructure.core.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Builder
@AllArgsConstructor
@Getter
@Setter
public final class SearchParameters {

    @Getter
    private final Long officeId;
    @Getter
    private final String externalId;
    @Getter
    private final String name;
    @Getter
    private final String hierarchy;
    @Getter
    private final String firstname;
    @Getter
    private final String lastname;
    @Getter
    private final String status;
    @Getter
    private final Integer offset;
    @Getter
    private final Integer limit;
    @Getter
    private final String orderBy;
    @Getter
    private final String sortOrder;
    @Getter
    private final String accountNo;
    @Getter
    private final String currencyCode;

    @Getter
    private final Long staffId;

    @Getter
    private final Long loanId;

    @Getter
    private final Long savingsId;
    private final Boolean orphansOnly;

    // Provisioning Entries Search Params
    @Getter
    private final Long provisioningEntryId;
    @Getter
    private final Long productId;
    @Getter
    private final Long categoryId;
    private final boolean isSelfUser;

    public static SearchParameters from(final Long officeId, final String externalId, final String name, final String hierarchy) {
        final Long staffId = null;
        final String accountNo = null;
        final Long loanId = null;
        final Long savingsId = null;
        final Boolean orphansOnly = false;
        final boolean isSelfUser = false;
        return new SearchParameters(officeId, externalId, name, hierarchy, null, null, null, null, null, null, staffId, accountNo, loanId,
                savingsId, orphansOnly, isSelfUser);
    }

    public static SearchParameters forClients(final Long officeId, final String externalId, final String displayName,
                                              final String firstname, final String lastname, final String status, final String hierarchy, final Integer offset,
                                              final Integer limit, final String orderBy, final String sortOrder, final Boolean orphansOnly, final boolean isSelfUser) {

        final Integer maxLimitAllowed = getCheckedLimit(limit);
        final Long staffId = null;
        final String accountNo = null;
        final Long loanId = null;
        final Long savingsId = null;

        return new SearchParameters(officeId, externalId, displayName, hierarchy, firstname, lastname, status, offset, maxLimitAllowed,
                orderBy, sortOrder, staffId, accountNo, loanId, savingsId, orphansOnly, isSelfUser);
    }

    public static SearchParameters forGroups(final Long officeId, final Long staffId, final String externalId, final String name,
                                             final String hierarchy, final Integer offset, final Integer limit, final String orderBy, final String sortOrder,
                                             final Boolean orphansOnly) {

        final Integer maxLimitAllowed = getCheckedLimit(limit);
        final String accountNo = null;
        final Long loanId = null;
        final Long savingsId = null;
        final boolean isSelfUser = false;

        return new SearchParameters(officeId, externalId, name, hierarchy, null, null, offset, maxLimitAllowed, orderBy, sortOrder, staffId,
                accountNo, loanId, savingsId, orphansOnly, isSelfUser);
    }

    public static SearchParameters forOffices(final String orderBy, final String sortOrder) {
        final Boolean orphansOnly = false;
        final boolean isSelfUser = false;
        return new SearchParameters(null, null, null, null, null, null, null, null, orderBy, sortOrder, null, null, null, null, orphansOnly,
                isSelfUser);
    }

    public static SearchParameters forLoans(final String externalId, final Integer offset, final Integer limit, final String orderBy,
                                            final String sortOrder, final String accountNo) {

        final Integer maxLimitAllowed = getCheckedLimit(limit);
        final Long staffId = null;
        final Long loanId = null;
        final Long savingsId = null;
        final Boolean orphansOnly = false;
        final boolean isSelfUser = false;

        return new SearchParameters(null, externalId, null, null, null, null, offset, maxLimitAllowed, orderBy, sortOrder, staffId,
                accountNo, loanId, savingsId, orphansOnly, isSelfUser);
    }

    public static SearchParameters forJournalEntries(final Long officeId, final Integer offset, final Integer limit, final String orderBy,
                                                     final String sortOrder, final Long loanId, final Long savingsId) {

        final Integer maxLimitAllowed = getCheckedLimit(limit);
        final Long staffId = null;
        final Boolean orphansOnly = false;
        final boolean isSelfUser = false;

        return new SearchParameters(officeId, null, null, null, null, null, offset, maxLimitAllowed, orderBy, sortOrder, staffId, null,
                loanId, savingsId, orphansOnly, isSelfUser);
    }

    public static SearchParameters forJournalEntries(final Long officeId, final Integer offset, final Integer limit, final String orderBy,
                                                     final String sortOrder, final Long loanId, final Long savingsId, final String currencyCode) {
        final Integer maxLimitAllowed = getCheckedLimit(limit);
        final Long staffId = null;
        final Boolean orphansOnly = false;

        return new SearchParameters(officeId, null, null, null, null, null, offset, maxLimitAllowed, orderBy, sortOrder, staffId, null,
                loanId, savingsId, orphansOnly, currencyCode);
    }

    public static SearchParameters forPagination(final Integer offset, final Integer limit, final String orderBy, final String sortOrder) {

        final Integer maxLimitAllowed = getCheckedLimit(limit);
        final Long staffId = null;
        final Long loanId = null;
        final Long savingsId = null;
        final Boolean orphansOnly = false;
        final boolean isSelfUser = false;

        return new SearchParameters(null, null, null, null, null, null, offset, maxLimitAllowed, orderBy, sortOrder, staffId, null, loanId,
                savingsId, orphansOnly, isSelfUser);
    }

    public static SearchParameters forPaginationAndAccountNumberSearch(final Integer offset, final Integer limit, final String orderBy,
                                                                       final String sortOrder, final String accountNumber) {

        final Integer maxLimitAllowed = getCheckedLimit(limit);
        final Long staffId = null;
        final Long loanId = null;
        final Long savingsId = null;
        final Boolean orphansOnly = false;
        final boolean isSelfUser = false;

        return new SearchParameters(null, null, null, null, null, null, offset, maxLimitAllowed, orderBy, sortOrder, staffId, accountNumber,
                loanId, savingsId, orphansOnly, isSelfUser);
    }

    public static SearchParameters forPagination(final Integer offset, final Integer limit) {

        final Integer maxLimitAllowed = getCheckedLimit(limit);
        final Long staffId = null;
        final Long loanId = null;
        final Long savingsId = null;
        final Boolean orphansOnly = false;
        final String orderBy = null;
        final String sortOrder = null;
        final boolean isSelfUser = false;

        return new SearchParameters(null, null, null, null, null, null, offset, maxLimitAllowed, orderBy, sortOrder, staffId, null, loanId,
                savingsId, orphansOnly, isSelfUser);
    }

    public static SearchParameters forProvisioningEntries(final Long provisioningEntryId, final Long officeId, final Long productId,
                                                          final Long categoryId, final Integer offset, final Integer limit) {
        return new SearchParameters(provisioningEntryId, officeId, productId, categoryId, offset, limit);
    }

    public static SearchParameters forSavings(final String externalId, final Integer offset, final Integer limit, final String orderBy,
                                              final String sortOrder) {

        final Integer maxLimitAllowed = getCheckedLimit(limit);
        final Long staffId = null;
        final String accountNo = null;
        final Long loanId = null;
        final Long savingsId = null;
        final Boolean orphansOnly = false;
        final boolean isSelfUser = false;

        return new SearchParameters(null, externalId, null, null, null, null, offset, maxLimitAllowed, orderBy, sortOrder, staffId,
                accountNo, loanId, savingsId, orphansOnly, isSelfUser);
    }

    public static SearchParameters forAccountTransfer(final String externalId, final Integer offset, final Integer limit,
                                                      final String orderBy, final String sortOrder) {

        final Integer maxLimitAllowed = getCheckedLimit(limit);
        final Long staffId = null;
        final String accountNo = null;
        final Long loanId = null;
        final Long savingsId = null;
        final Boolean orphansOnly = false;
        final boolean isSelfUser = false;

        return new SearchParameters(null, externalId, null, null, null, null, offset, maxLimitAllowed, orderBy, sortOrder, staffId,
                accountNo, loanId, savingsId, orphansOnly, isSelfUser);
    }

    public static SearchParameters forSMSCampaign(final Integer offset, final Integer limit, final String orderBy, final String sortOrder) {

        final String externalId = null;
        final Integer maxLimitAllowed = getCheckedLimit(limit);
        final Long staffId = null;
        final String accountNo = null;
        final Long loanId = null;
        final Long savingsId = null;
        final Boolean orphansOnly = false;
        final boolean isSelfUser = false;

        return new SearchParameters(null, externalId, null, null, null, null, offset, maxLimitAllowed, orderBy, sortOrder, staffId,
                accountNo, loanId, savingsId, orphansOnly, isSelfUser);
    }

    public static SearchParameters forEmailCampaign(final Integer offset, final Integer limit, final String orderBy,
                                                    final String sortOrder) {

        final String externalId = null;
        final Integer maxLimitAllowed = getCheckedLimit(limit);
        final Long staffId = null;
        final String accountNo = null;
        final Long loanId = null;
        final Long savingsId = null;
        final Boolean orphansOnly = false;
        final boolean isSelfUser = false;

        return new SearchParameters(null, externalId, null, null, null, null, offset, maxLimitAllowed, orderBy, sortOrder, staffId,
                accountNo, loanId, savingsId, orphansOnly, isSelfUser);
    }

    private SearchParameters(final Long officeId, final String externalId, final String name, final String hierarchy,
                             final String firstname, final String lastname, final String status, final Integer offset, final Integer limit,
                             final String orderBy, final String sortOrder, final Long staffId, final String accountNo, final Long loanId,
                             final Long savingsId, final Boolean orphansOnly, boolean isSelfUser) {
        this.officeId = officeId;
        this.externalId = externalId;
        this.name = name;
        this.hierarchy = hierarchy;
        this.firstname = firstname;
        this.lastname = lastname;
        this.offset = offset;
        this.limit = limit;
        this.orderBy = orderBy;
        this.sortOrder = sortOrder;
        this.staffId = staffId;
        this.accountNo = accountNo;
        this.loanId = loanId;
        this.savingsId = savingsId;
        this.orphansOnly = orphansOnly;
        this.currencyCode = null;
        this.provisioningEntryId = null;
        this.productId = null;
        this.categoryId = null;
        this.isSelfUser = isSelfUser;
        this.status = status;

    }

    private SearchParameters(final Long officeId, final String externalId, final String name, final String hierarchy,
                             final String firstname, final String lastname, final Integer offset, final Integer limit, final String orderBy,
                             final String sortOrder, final Long staffId, final String accountNo, final Long loanId, final Long savingsId,
                             final Boolean orphansOnly, boolean isSelfUser) {
        this.officeId = officeId;
        this.externalId = externalId;
        this.name = name;
        this.hierarchy = hierarchy;
        this.firstname = firstname;
        this.lastname = lastname;
        this.offset = offset;
        this.limit = limit;
        this.orderBy = orderBy;
        this.sortOrder = sortOrder;
        this.staffId = staffId;
        this.accountNo = accountNo;
        this.loanId = loanId;
        this.savingsId = savingsId;
        this.orphansOnly = orphansOnly;
        this.currencyCode = null;
        this.provisioningEntryId = null;
        this.productId = null;
        this.categoryId = null;
        this.isSelfUser = isSelfUser;
        this.status = null;
    }

    private SearchParameters(final Long provisioningEntryId, final Long officeId, final Long productId, final Long categoryId,
                             final Integer offset, final Integer limit) {
        this.externalId = null;
        this.name = null;
        this.hierarchy = null;
        this.firstname = null;
        this.lastname = null;
        this.orderBy = null;
        this.sortOrder = null;
        this.staffId = null;
        this.accountNo = null;
        this.loanId = null;
        this.savingsId = null;
        this.orphansOnly = null;
        this.currencyCode = null;
        this.officeId = officeId;
        this.offset = offset;
        this.limit = limit;
        this.provisioningEntryId = provisioningEntryId;
        this.productId = productId;
        this.categoryId = categoryId;
        this.isSelfUser = false;
        this.status = null;

    }

    public SearchParameters(final Long officeId, final String externalId, final String name, final String hierarchy, final String firstname,
                            final String lastname, final Integer offset, final Integer limit, final String orderBy, final String sortOrder,
                            final Long staffId, final String accountNo, final Long loanId, final Long savingsId, final Boolean orphansOnly,
                            final String currencyCode) {
        this.officeId = officeId;
        this.externalId = externalId;
        this.name = name;
        this.hierarchy = hierarchy;
        this.firstname = firstname;
        this.lastname = lastname;
        this.offset = offset;
        this.limit = limit;
        this.orderBy = orderBy;
        this.sortOrder = sortOrder;
        this.staffId = staffId;
        this.accountNo = accountNo;
        this.loanId = loanId;
        this.savingsId = savingsId;
        this.orphansOnly = orphansOnly;
        this.currencyCode = currencyCode;
        this.provisioningEntryId = null;
        this.productId = null;
        this.categoryId = null;
        this.isSelfUser = false;
        this.status = null;

    }

    public boolean isOrderByRequested() {
        return StringUtils.isNotBlank(this.orderBy);
    }

    public boolean isSortOrderProvided() {
        return StringUtils.isNotBlank(this.sortOrder);
    }

    public static Integer getCheckedLimit(final Integer limit) {

        final Integer maxLimitAllowed = 200;
        // default to max limit first off
        Integer checkedLimit = maxLimitAllowed;

        if (limit != null && limit > 0) {
            checkedLimit = limit;
        } else if (limit != null) {
            // unlimited case: limit provided and 0 or less
            checkedLimit = null;
        }

        return checkedLimit;
    }

    public boolean isOfficeIdPassed() {
        return this.officeId != null && this.officeId != 0;
    }

    public boolean isCurrencyCodePassed() {
        return this.currencyCode != null;
    }

    public boolean isLimited() {
        return this.limit != null && this.limit.intValue() > 0;
    }

    public boolean isOffset() {
        return this.offset != null;
    }

    public boolean isScopedByOfficeHierarchy() {
        return StringUtils.isNotBlank(this.hierarchy);
    }

    public boolean isStaffIdPassed() {
        return this.staffId != null && this.staffId != 0;
    }

    public boolean isLoanIdPassed() {
        return this.loanId != null && this.loanId != 0;
    }

    public boolean isSavingsIdPassed() {
        return this.savingsId != null && this.savingsId != 0;
    }

    public Boolean isOrphansOnly() {
        if (this.orphansOnly != null) {
            return this.orphansOnly;
        }
        return false;
    }

    public boolean isProvisioningEntryIdPassed() {
        return this.provisioningEntryId != null && this.provisioningEntryId != 0;
    }

    public boolean isProductIdPassed() {
        return this.productId != null && this.productId != 0;
    }

    public boolean isCategoryIdPassed() {
        return this.categoryId != null && this.categoryId != 0;
    }

    public boolean isSelfUser() {
        return this.isSelfUser;
    }

    /**
     * creates an instance of the SearchParameters from a request for the report mailing job run history
     *
     * @return SearchParameters object
     **/
    public static SearchParameters fromReportMailingJobRunHistory(final Integer offset, final Integer limit, final String orderBy,
                                                                  final String sortOrder) {
        final Integer maxLimitAllowed = getCheckedLimit(limit);

        return new SearchParameters(null, null, null, null, null, null, offset, maxLimitAllowed, orderBy, sortOrder, null, null, null, null,
                null, false);
    }

    /**
     * creates an instance of the {@link SearchParameters} from a request for the report mailing job
     *
     * @return {@link SearchParameters} object
     */
    public static SearchParameters fromReportMailingJob(final Integer offset, final Integer limit, final String orderBy,
                                                        final String sortOrder) {
        final Integer maxLimitAllowed = getCheckedLimit(limit);

        return new SearchParameters(null, null, null, null, null, null, offset, maxLimitAllowed, orderBy, sortOrder, null, null, null, null,
                null, false);
    }
}
