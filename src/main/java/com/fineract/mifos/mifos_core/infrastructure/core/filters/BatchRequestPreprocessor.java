package com.fineract.mifos.mifos_core.infrastructure.core.filters;

import com.fineract.mifos.mifos_core.batch.dtos.BatchRequest;
import io.vavr.control.Either;

public interface BatchRequestPreprocessor {
    Either<RuntimeException, BatchRequest> preprocess(BatchRequest batchRequest);
}
