package com.fineract.mifos.mifos_core.infrastructure.dataqueries.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class ReportExportType implements Serializable {

    private final String key;
    private final String queryParameter;
}
