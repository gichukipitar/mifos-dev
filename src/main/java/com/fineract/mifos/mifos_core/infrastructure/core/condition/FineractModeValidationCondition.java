package com.fineract.mifos.mifos_core.infrastructure.core.condition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Optional;

@Slf4j
public class FineractModeValidationCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        boolean isReadModeEnabled = Optional.ofNullable(context.getEnvironment().getProperty("fineract.mode.read-enabled", Boolean.class))
                .orElse(true);
        boolean isWriteModeEnabled = Optional.ofNullable(context.getEnvironment().getProperty("fineract.mode.write-enabled", Boolean.class))
                .orElse(true);
        boolean isBatchManagerModeEnabled = Optional
                .ofNullable(context.getEnvironment().getProperty("fineract.mode.batch-manager-enabled", Boolean.class)).orElse(true);
        boolean isBatchWorkerModeEnabled = Optional
                .ofNullable(context.getEnvironment().getProperty("fineract.mode.batch-worker-enabled", Boolean.class)).orElse(true);
        boolean isValidationFails = !isReadModeEnabled && !isWriteModeEnabled && !isBatchManagerModeEnabled && !isBatchWorkerModeEnabled;
        if (isValidationFails) {
            log.error(
                    "The Fineract instance type is not configured properly. At least one of these environment variables should be true: FINERACT_MODE_READ_ENABLED, FINERACT_MODE_WRITE_ENABLED, FINERACT_MODE_BATCH_ENABLED");
        }
        return isValidationFails;
    }

}
