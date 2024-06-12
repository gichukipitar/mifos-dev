package com.fineract.mifos.mifos_core.batch.exception;

import com.fineract.mifos.mifos_core.batch.dtos.BatchRequest;
import lombok.Data;

@Data
public class BatchExecutionException extends RuntimeException{
    private final BatchRequest request;

    public BatchExecutionException(BatchRequest request, RuntimeException ex) {
        super("Error executing batch request: " + request, ex);
        this.request = request;
    }
}
