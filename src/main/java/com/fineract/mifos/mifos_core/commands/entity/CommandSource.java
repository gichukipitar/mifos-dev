package com.fineract.mifos.mifos_core.commands.entity;

import com.fineract.mifos.mifos_core.commands.dtos.CommandProcessingResultType;
import com.fineract.mifos.mifos_core.commands.dtos.CommandWrapper;
import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import com.fineract.mifos.mifos_core.infrastructure.core.domain.AbstractPersistableCustom;
import com.fineract.mifos.mifos_core.infrastructure.core.domain.ExternalId;
import com.fineract.mifos.mifos_core.infrastructure.core.service.DateUtils;
import com.fineract.mifos.mifos_core.useradministration.entity.AppUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Data
@Table(name = "m_portfolio_command_source")
public class CommandSource extends AbstractPersistableCustom {

    @Getter
    @Column(name = "action_name", nullable = true, length = 100)
    private String actionName;

    @Getter
    @Column(name = "entity_name", nullable = true, length = 100)
    private String entityName;

    /**
     * -- GETTER --
     *
     * @return the officeId
     */
    @Getter
    @Column(name = "office_id")
    private Long officeId;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "client_id")
    private Long clientId;

    /**
     * -- GETTER --
     *
     * @return the loanId
     */
    @Getter
    @Column(name = "loan_id")
    private Long loanId;

    /**
     * -- GETTER --
     *
     * @return the savingsId
     */
    @Getter
    @Column(name = "savings_account_id")
    private Long savingsId;

    @Column(name = "api_get_url", length = 100)
    private String resourceGetUrl;

    @Setter
    @Getter
    @Column(name = "resource_id")
    private Long resourceId;

    @Setter
    @Getter
    @Column(name = "subresource_id")
    private Long subResourceId;

    @Column(name = "command_as_json", length = 1000)
    private String commandAsJson;

    @Getter
    @ManyToOne
    @JoinColumn(name = "maker_id", nullable = false)
    private AppUser maker;

    /*
     * Deprecated: Columns and data left untouched to help migration.
     *
     * @Column(name = "made_on_date", nullable = false) private LocalDateTime madeOnDate;
     *
     * @Column(name = "checked_on_date", nullable = true) private LocalDateTime checkedOnDate;
     */

    @Column(name = "made_on_date_utc", nullable = false)
    private OffsetDateTime madeOnDate;

    @Column(name = "checked_on_date_utc")
    private OffsetDateTime checkedOnDate;

    @Getter
    @ManyToOne
    @JoinColumn(name = "checker_id", nullable = true)
    private AppUser checker;

    @Setter
    @Getter
    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "product_id")
    private Long productId;

    /**
     * -- GETTER --
     *
     * @return the transactionId
     */
    @Setter
    @Getter
    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    @Setter
    @Getter
    @Column(name = "creditbureau_id")
    private Long creditBureauId;

    @Setter
    @Getter
    @Column(name = "organisation_creditbureau_id")
    private Long organisationCreditBureauId;

    @Getter
    @Column(name = "job_name")
    private String jobName;

    @Setter
    @Getter
    @Column(name = "idempotency_key", length = 50)
    private String idempotencyKey;

    @Column(name = "resource_external_id")
    private ExternalId resourceExternalId;

    @Column(name = "subresource_external_id")
    private ExternalId subResourceExternalId;

    @Setter
    @Getter
    @Column(name = "result")
    private String result;

    @Getter
    @Setter
    @Column(name = "result_status_code")
    private Integer resultStatusCode;
    private CommandSource(final String actionName, final String entityName, final String href, final Long resourceId,
                          final Long subResourceId, final String commandSerializedAsJson, final AppUser maker, final String idempotencyKey,
                          final Integer status) {
        this.actionName = actionName;
        this.entityName = entityName;
        this.resourceGetUrl = href;
        this.resourceId = resourceId;
        this.subResourceId = subResourceId;
        this.commandAsJson = commandSerializedAsJson;
        this.maker = maker;
        this.madeOnDate = DateUtils.getAuditOffsetDateTime();
        this.status = status;
        this.idempotencyKey = idempotencyKey;
    }

    public static CommandSource fullEntryFrom(final CommandWrapper wrapper, final JsonCommand command, final AppUser maker,
                                              String idempotencyKey, Integer status) {
        CommandSource commandSource = new CommandSource(wrapper.actionName(), wrapper.entityName(), wrapper.getHref(), command.entityId(),
                command.subentityId(), command.json(), maker, idempotencyKey, status);
        commandSource.officeId = wrapper.getOfficeId();
        commandSource.groupId = command.getGroupId();
        commandSource.clientId = command.getClientId();
        commandSource.loanId = command.getLoanId();
        commandSource.savingsId = command.getSavingsId();
        commandSource.productId = command.getProductId();
        commandSource.transactionId = command.getTransactionId();
        commandSource.creditBureauId = command.getCreditBureauId();
        commandSource.organisationCreditBureauId = command.getOrganisationCreditBureauId();
        return commandSource;
    }

    protected CommandSource() {
        //
    }

    public String getCommandJson() {
        return this.commandAsJson;
    }

    public void setCommandJson(final String json) {
        this.commandAsJson = json;
    }

    public String getPermissionCode() {
        return this.actionName + "_" + this.entityName;
    }

    public void setStatus(CommandProcessingResultType status) {
        setStatus(CommandProcessingResultType.fromInt(status == null ? null : status.getValue()));
    }

    public void markAsAwaitingApproval() {
        this.status = CommandProcessingResultType.AWAITING_APPROVAL.getValue();
    }

    public boolean isMarkedAsAwaitingApproval() {
        return this.status.equals(CommandProcessingResultType.AWAITING_APPROVAL.getValue());
    }

    public void markAsChecked(final AppUser checker) {
        this.checker = checker;
        this.checkedOnDate = DateUtils.getAuditOffsetDateTime();
        this.status = CommandProcessingResultType.PROCESSED.getValue();
    }

    public void markAsRejected(final AppUser checker) {
        this.checker = checker;
        this.checkedOnDate = DateUtils.getAuditOffsetDateTime();
        this.status = CommandProcessingResultType.REJECTED.getValue();
    }

    public void updateForAudit(final CommandProcessingResult result) {
        this.officeId = result.getOfficeId();
        this.groupId = result.getGroupId();
        this.clientId = result.getClientId();
        this.loanId = result.getLoanId();
        this.savingsId = result.getSavingsId();
        this.productId = result.getProductId();
        this.transactionId = result.getTransactionId();
        this.resourceId = result.getResourceId();
        this.resourceExternalId = result.getResourceExternalId();
        this.subResourceId = result.getSubResourceId();
        this.subResourceExternalId = result.getSubResourceExternalId();
    }
}

