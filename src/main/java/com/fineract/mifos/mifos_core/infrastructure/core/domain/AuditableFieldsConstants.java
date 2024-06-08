package com.fineract.mifos.mifos_core.infrastructure.core.domain;

public final class AuditableFieldsConstants {

    public static final String CREATED_BY = "createdBy";
    public static final String CREATED_DATE = "createdDate";
    public static final String LAST_MODIFIED_BY = "lastModifiedBy";
    public static final String LAST_MODIFIED_DATE = "lastModifiedDate";

    public static final String CREATED_BY_DB_FIELD = "created_by";
    public static final String CREATED_DATE_DB_FIELD = "created_on_utc";
    public static final String LAST_MODIFIED_BY_DB_FIELD = "last_modified_by";
    public static final String LAST_MODIFIED_DATE_DB_FIELD = "last_modified_on_utc";

    private AuditableFieldsConstants() {}
}
