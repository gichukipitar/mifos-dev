package com.fineract.mifos.mifos_core.commands.jobs;

import com.fineract.mifos.mifos_core.infrastructure.jobs.service.JobName;
import com.fineract.mifos.mifos_core.infrastructure.jobs.service.StepName;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
public class PurgeProcessedCommandsConfig {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private PurgeProcessedCommandsTasklet tasklet;

    @Bean
    protected Step purgeProcessedCommandsStep() {
        return new StepBuilder(StepName.PURGE_PROCESSED_COMMANDS_STEP.name()).tasklet(tasklet).build();
    }

    @Bean
    public BatchProperties.Job purgeProcessedCommandsJob() {
        return (BatchProperties.Job) new JobBuilder(JobName.PURGE_PROCESSED_COMMANDS.name()).start(purgeProcessedCommandsStep())
                .incrementer(new RunIdIncrementer()).build();
    }
}
