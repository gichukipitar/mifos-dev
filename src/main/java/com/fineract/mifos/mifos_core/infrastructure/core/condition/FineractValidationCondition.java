package com.fineract.mifos.mifos_core.infrastructure.core.condition;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.context.annotation.Conditional;

public class FineractValidationCondition extends AnyNestedCondition {

    public FineractValidationCondition() {
        super(ConfigurationPhase.PARSE_CONFIGURATION);
    }

    @Conditional(FineractModeValidationCondition.class)
    static class FineractModeValidation {}

    @Conditional(FineractPartitionJobConfigValidationCondition.class)
    static class FineractPartitionedJobValidation {}

    @Conditional(FineractRemoteJobMessageHandlerCondition.class)
    static class FineractRemoteJobMessageHandlerValidation {}

    @Conditional(FineractExternalEventConfigCondition.class)
    static class FineractExternalEventConfigValidation {}

}
