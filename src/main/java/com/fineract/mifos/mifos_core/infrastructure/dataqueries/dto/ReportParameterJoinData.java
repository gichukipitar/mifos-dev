package com.fineract.mifos.mifos_core.infrastructure.dataqueries.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ReportParameterJoinData {

    private final Long reportId;
    private final String reportName;
    private final String reportType;
    private final String reportSubType;
    private final String reportCategory;
    private final String description;
    private final String reportSql;
    private final Boolean coreReport;
    private final Boolean useReport;

    private final Long reportParameterId;
    private final Long parameterId;
    private final String reportParameterName;
    private final String parameterName;

}
