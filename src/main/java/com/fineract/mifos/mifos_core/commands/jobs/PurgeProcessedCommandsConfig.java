package com.fineract.mifos.mifos_core.commands.jobs;

import org.springframework.beans.factory.annotation.Autowired;
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
        return new StepBuilder(StepName.PURGE_PROCESSED_COMMANDS_STEP.name(), jobRepository).tasklet(tasklet, transactionManager).build();
    }

    @Bean
    public BatchProperties.Job purgeProcessedCommandsJob() {
        return new JobBuilder(JobName.PURGE_PROCESSED_COMMANDS.name(), jobRepository).start(purgeProcessedCommandsStep())
                .incrementer(new RunIdIncrementer()).build();
    }
}
