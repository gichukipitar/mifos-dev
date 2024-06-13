package com.fineract.mifos.mifos_core.infrastructure.core.data;

import com.fineract.mifos.mifos_core.infrastructure.core.domain.ExternalId;
import lombok.Data;

import java.util.Map;

/**
 * Represents the successful result of an REST API call that results in processing a command.
 */

@Data
public class CommandProcessingResultBuilder {

    private Long commandId;
    private Long officeId;
    private Long groupId;
    private Long clientId;
    private Long loanId;
    private Long savingsId;
    private String resourceIdentifier;
    private Long entityId;
    private Long subEntityId;
    private Long gsimId;
    private Long glimId;
    private String transactionId;
    private Map<String, Object> changes;
    private Map<String, Object> creditBureauReportData;
    private Long productId;
    private boolean rollbackTransaction = false;
    private ExternalId entityExternalId = ExternalId.empty();

    private ExternalId subEntityExternalId = ExternalId.empty();

    public CommandProcessingResult build() {
        return CommandProcessingResult.fromDetails(this.commandId, this.officeId, this.groupId, this.clientId, this.loanId, this.savingsId,
                this.resourceIdentifier, this.entityId, this.gsimId, this.glimId, this.creditBureauReportData, this.transactionId,
                this.changes, this.productId, this.rollbackTransaction, this.subEntityId, this.entityExternalId, this.subEntityExternalId);
    }

    public CommandProcessingResultBuilder withCommandId(final Long withCommandId) {
        this.commandId = withCommandId;
        return this;
    }

    public CommandProcessingResultBuilder with(final Map<String, Object> withChanges) {
        this.changes = withChanges;
        return this;
    }

    public CommandProcessingResultBuilder withResourceIdAsString(final String withResourceIdentifier) {
        this.resourceIdentifier = withResourceIdentifier;
        return this;
    }

    public CommandProcessingResultBuilder withEntityId(final Long withEntityId) {
        this.entityId = withEntityId;
        return this;
    }

    public CommandProcessingResultBuilder withSubEntityId(final Long withSubEntityId) {
        this.subEntityId = withSubEntityId;
        return this;
    }

    public CommandProcessingResultBuilder withOfficeId(final Long withOfficeId) {
        this.officeId = withOfficeId;
        return this;
    }

    public CommandProcessingResultBuilder withClientId(final Long withClientId) {
        this.clientId = withClientId;
        return this;
    }

    public CommandProcessingResultBuilder withGroupId(final Long withGroupId) {
        this.groupId = withGroupId;
        return this;
    }

    public CommandProcessingResultBuilder withLoanId(final Long withLoanId) {
        this.loanId = withLoanId;
        return this;
    }

    public CommandProcessingResultBuilder withSavingsId(final Long withSavingsId) {
        this.savingsId = withSavingsId;
        return this;
    }

    public CommandProcessingResultBuilder withTransactionId(final String withTransactionId) {
        this.transactionId = withTransactionId;
        return this;
    }

    public CommandProcessingResultBuilder withProductId(final Long productId) {
        this.productId = productId;
        return this;
    }

    public CommandProcessingResultBuilder withGsimId(final Long gsimId) {
        this.gsimId = gsimId;
        return this;
    }

    public CommandProcessingResultBuilder withGlimId(final Long glimId) {
        this.glimId = glimId;
        return this;
    }

    public CommandProcessingResultBuilder withCreditReport(final Map<String, Object> withCreditReport) {
        this.creditBureauReportData = withCreditReport;
        return this;
    }

    public CommandProcessingResultBuilder setRollbackTransaction(final boolean rollbackTransaction) {
        this.rollbackTransaction |= rollbackTransaction;
        return this;
    }

    public CommandProcessingResultBuilder withEntityExternalId(final ExternalId entityExternalId) {
        this.entityExternalId = entityExternalId;
        return this;
    }

    public CommandProcessingResultBuilder withSubEntityExternalId(final ExternalId subEntityExternalId) {
        this.subEntityExternalId = subEntityExternalId;
        return this;
    }

}
