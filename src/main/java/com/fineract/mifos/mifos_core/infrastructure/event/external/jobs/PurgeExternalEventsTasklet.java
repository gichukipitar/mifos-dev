package com.fineract.mifos.mifos_core.infrastructure.event.external.jobs;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@AllArgsConstructor
@Component
public class PurgeExternalEventsTasklet implements Tasklet {

    private final ExternalEventRepository repository;
    private final ConfigurationDomainService configurationDomainService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        try {
            Long numberOfDaysForPurgeCriteria = configurationDomainService.retrieveExternalEventsPurgeDaysCriteria();
            LocalDate dateForPurgeCriteria = DateUtils.getBusinessLocalDate().minusDays(numberOfDaysForPurgeCriteria);
            repository.deleteOlderEventsWithSentStatus(ExternalEventStatus.SENT, dateForPurgeCriteria);
        } catch (Exception e) {
            log.error("Error occurred while purging external events: ", e);
        }
        return RepeatStatus.FINISHED;
    }

}