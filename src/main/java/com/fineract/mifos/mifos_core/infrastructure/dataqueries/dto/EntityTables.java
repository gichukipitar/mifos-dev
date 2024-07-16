package com.fineract.mifos.mifos_core.infrastructure.dataqueries.dto;

import com.google.common.collect.ImmutableList;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.fineract.mifos.mifos_core.infrastructure.dataqueries.dto.StatusEnum.*;

public enum EntityTables {

    CLIENT("m_client", "client_id", "id", CREATE, ACTIVATE, CLOSE), //
    GROUP("m_group", "group_id", "id", CREATE, ACTIVATE, CLOSE), //
    CENTER("m_center", "m_group", "center_id", "id"), //
    OFFICE("m_office", "office_id", "id"), //
    LOAN_PRODUCT("m_product_loan", "product_loan_id", "id"), //
    LOAN("m_loan", "loan_id", "id", CREATE, APPROVE, DISBURSE, WITHDRAWN, REJECTED, WRITE_OFF), //
    SAVINGS_PRODUCT("m_savings_product", "savings_product_id", "id"), //
    SAVINGS("m_savings_account", "savings_account_id", "id", CREATE, APPROVE, ACTIVATE, WITHDRAWN, REJECTED, CLOSE), //
    SAVINGS_TRANSACTION("m_savings_account_transaction", "savings_transaction_id", "id"), //
    SHARE_PRODUCT("m_share_product", "share_product_id", "id"), //
    ;

    public static final EntityTables[] VALUES = values();

    private static final List<String> ENTITY_NAMES = Arrays.stream(VALUES).map(EntityTables::getName).toList();

    private static final Map<String, EntityTables> BY_ENTITY_NAME = Arrays.stream(VALUES)
            .collect(Collectors.toMap(EntityTables::getName, e -> e));

    @NotNull
    private final String name;
    @NotNull
    private final String apptableName;

    @NotNull
    private final String foreignKeyColumnNameOnDatatable;
    @NotNull
    private final String refColumn; // referenced column name on apptable

    private final ImmutableList<StatusEnum> checkStatuses;

    EntityTables(@NotNull String name, @NotNull String apptableName, @NotNull String foreignKeyColumnNameOnDatatable,
                 @NotNull String refColumn, StatusEnum... statuses) {
        this.name = name;
        this.apptableName = apptableName;
        this.foreignKeyColumnNameOnDatatable = foreignKeyColumnNameOnDatatable;
        this.refColumn = refColumn;
        this.checkStatuses = statuses == null ? ImmutableList.of() : ImmutableList.copyOf(statuses);
    }

    EntityTables(@NotNull String name, @NotNull String foreignKeyColumnNameOnDatatable, @NotNull String refColumn, StatusEnum... statuses) {
        this(name, name, foreignKeyColumnNameOnDatatable, refColumn, statuses);
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getApptableName() {
        return apptableName;
    }

    @NotNull
    public String getForeignKeyColumnNameOnDatatable() {
        return this.foreignKeyColumnNameOnDatatable;
    }

    @NotNull
    public String getRefColumn() {
        return refColumn;
    }

    public List<StatusEnum> getCheckStatuses() {
        return checkStatuses;
    }

    public boolean hasCheck() {
        return checkStatuses != null && !checkStatuses.isEmpty();
    }

    public static List<String> getEntityNames() {
        return ENTITY_NAMES;
    }

    public static EntityTables fromEntityName(String name) {
        return name == null ? null : BY_ENTITY_NAME.get(name.toLowerCase());
    }

    public static String getForeignKeyColumnNameOnDatatable(String name) {
        EntityTables entityTable = fromEntityName(name);
        return entityTable == null ? null : entityTable.getForeignKeyColumnNameOnDatatable();
    }

    @NotNull
    public static List<StatusEnum> getCheckStatuses(String name) {
        EntityTables entityTable = fromEntityName(name);
        return entityTable == null ? List.of() : entityTable.getCheckStatuses();
    }

    @NotNull
    public static List<Integer> getCheckStatusCodes(String name) {
        return getCheckStatuses(name).stream().map(StatusEnum::getCode).toList();
    }

    @NotNull
    public static List<EntityTables> getFiltered(Predicate<EntityTables> filter) {
        return Arrays.stream(VALUES).filter(filter).toList();
    }
}
