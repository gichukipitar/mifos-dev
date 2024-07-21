package com.fineract.mifos.mifos_core.infrastructure.event.external.jobs;

import com.fineract.mifos.mifos_core.infrastructure.jobs.service.JobName;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class PurgeExternalEventsConfig {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private PurgeExternalEventsTasklet tasklet;

    @Bean
    protected Step purgeExternalEventsStep() {
        return new StepBuilder(JobName.PURGE_EXTERNAL_EVENTS.name(), jobRepository).tasklet(tasklet, transactionManager).build();
    }

    @Bean
    public Job purgeExternalEventsJob() {
        return new JobBuilder(JobName.PURGE_EXTERNAL_EVENTS.name(), jobRepository).start(purgeExternalEventsStep())
                .incrementer(new RunIdIncrementer()).build();
    }
}
