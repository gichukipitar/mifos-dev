package com.fineract.mifos.mifos_core.infrastructure.core.condition;

import com.fineract.mifos.mifos_core.infrastructure.core.config.FineractProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class FineractPartitionJobConfigValidationCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        FineractProperties.FineractPartitionedJob partitionedJobProperties = ExplicitConfigurationPropertiesFactory.getProperty(context,
                "fineract.partitioned-job", FineractProperties.FineractPartitionedJob.class);
        if (partitionedJobProperties != null) {
            List<FineractProperties.PartitionedJobProperty> invalidConfigs = partitionedJobProperties.getPartitionedJobProperties().stream()
                    .filter(isAnyConfigBelowOne().or(FineractPartitionJobConfigValidationCondition::invalidMaxPoolSize)).toList();
            if (!invalidConfigs.isEmpty()) {
                for (FineractProperties.PartitionedJobProperty invalidConfig : invalidConfigs) {
                    log.error(
                            "{} partitioned job is not configured properly. The partition size, chunk size and thread count must be more than 0, and partition size must be less then chunk size * thread count",
                            invalidConfig.getJobName());
                }
            }
            return !invalidConfigs.isEmpty();
        } else {
            return false;
        }
    }

    private static Predicate<FineractProperties.PartitionedJobProperty> isAnyConfigBelowOne() {
        return partitionedJobProperty -> !(partitionedJobProperty.getPartitionSize() > 0 && partitionedJobProperty.getChunkSize() > 0
                && partitionedJobProperty.getThreadPoolCorePoolSize() > 0 && partitionedJobProperty.getThreadPoolMaxPoolSize() > 0
                && partitionedJobProperty.getThreadPoolQueueCapacity() > 0);
    }

    private static boolean invalidMaxPoolSize(FineractProperties.PartitionedJobProperty partitionedJobProperty) {
        return partitionedJobProperty.getThreadPoolMaxPoolSize() < partitionedJobProperty.getThreadPoolCorePoolSize();
    }
}
