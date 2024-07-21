package com.fineract.mifos.mifos_core.infrastructure.event.external.jobs;

import com.fineract.mifos.mifos_core.infrastructure.jobs.service.JobName;
import com.fineract.mifos.mifos_core.infrastructure.jobs.service.StepName;
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
public class SendAsynchronousEventsConfig {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private SendAsynchronousEventsTasklet tasklet;

    @Bean
    protected Step sendAsynchronousEventsStep() {
        return new StepBuilder(StepName.SEND_ASYNCHRONOUS_EVENTS_STEP.name()).tasklet(tasklet, transactionManager).build();
    }

    @Bean
    public Job sendAsynchronousEventsJob() {
        return new JobBuilder(JobName.SEND_ASYNCHRONOUS_EVENTS.name()).start(sendAsynchronousEventsStep())
                .incrementer(new RunIdIncrementer()).build();
    }

}
