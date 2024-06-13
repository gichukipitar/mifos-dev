package com.fineract.mifos.mifos_core.commands.service;

import com.fineract.mifos.mifos_core.commands.dtos.CommandWrapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class CommandWrapperBuilder {
    private Long officeId;
    private Long groupId;
    private Long clientId;
    private Long loanId;
    private Long savingsId;
    private String actionName;
    private String entityName;
    private Long entityId;
    private Long subentityId;
    private String href;
    private String json = "{}";
    private String transactionId;
    private Long productId;
    private Long templateId;
    private Long creditBureauId;
    private Long organisationCreditBureauId;
    private String jobName;
    private String idempotencyKey;

    @SuppressFBWarnings(value = "UWF_UNWRITTEN_FIELD", justification = "TODO: fix this!")
    public CommandWrapper build() {
        return new CommandWrapper(this.officeId, this.groupId, this.clientId, this.loanId, this.savingsId, this.actionName, this.entityName,
                this.entityId, this.subentityId, this.href, this.json, this.transactionId, this.productId, this.templateId,
                this.creditBureauId, this.organisationCreditBureauId, this.jobName, this.idempotencyKey);
    }

    public CommandWrapperBuilder excuteAccrualAccounting() {
        this.actionName = "EXECUTE";
        this.entityName = "PERIODICACCRUALACCOUNTING";
        this.entityId = null;
        this.href = "/accrualaccounting";
        return this;
    }
    public CommandWrapperBuilder withJson(final String withJson) {
        this.json = withJson;
        return this;
    }


}
