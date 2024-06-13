package com.fineract.mifos.mifos_core.commands.jobs;

import com.fineract.mifos.mifos_core.commands.dtos.CommandProcessingResultType;
import com.fineract.mifos.mifos_core.commands.repository.CommandSourceRepository;
import com.fineract.mifos.mifos_core.infrastructure.core.service.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Slf4j
@AllArgsConstructor
@Component
public class PurgeProcessedCommandsTasklet implements Tasklet {
    private final CommandSourceRepository repository;
    private final ConfigurationDomainService configurationDomainService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        try {
            Long numberOfDaysForPurgeCriteria = configurationDomainService.retrieveProcessedCommandsPurgeDaysCriteria();
            OffsetDateTime dateForPurgeCriteria = DateUtils.getAuditOffsetDateTime().minusDays(numberOfDaysForPurgeCriteria);
            repository.deleteOlderEventsWithStatus(CommandProcessingResultType.PROCESSED.getValue(), dateForPurgeCriteria);
        } catch (Exception e) {
            log.error("Error occurred while purging processed commands: ", e);
        }
        return RepeatStatus.FINISHED;
    }
}
