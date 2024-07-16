package com.fineract.mifos.mifos_core.infrastructure.dataqueries.dto;

/* used to show list of parameters used by a report and also for getting a list of parameters available (the reportParameterName is left null */
public final class ReportParameterData {

    @SuppressWarnings("unused")
    private final Long id;
    @SuppressWarnings("unused")
    private final Long parameterId;
    @SuppressWarnings("unused")
    private final String parameterName;
    @SuppressWarnings("unused")
    private final String reportParameterName;

    public ReportParameterData(final Long id, final Long parameterId, final String reportParameterName, final String parameterName) {
        this.id = id;
        this.parameterId = parameterId;
        this.parameterName = parameterName;
        this.reportParameterName = reportParameterName;
    }
}

