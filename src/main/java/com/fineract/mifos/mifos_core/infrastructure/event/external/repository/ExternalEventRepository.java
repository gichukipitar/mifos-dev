package com.fineract.mifos.mifos_core.infrastructure.event.external.repository;

import com.fineract.mifos.mifos_core.infrastructure.event.external.entity.ExternalEvent;
import com.fineract.mifos.mifos_core.infrastructure.event.external.entity.ExternalEventStatus;
import com.fineract.mifos.mifos_core.infrastructure.event.external.entity.ExternalEventView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public interface ExternalEventRepository extends JpaRepository<ExternalEvent, Long>, JpaSpecificationExecutor<ExternalEvent> {

    List<ExternalEventView> findByStatusOrderById(ExternalEventStatus status, Pageable batchSize);

    @Modifying(flushAutomatically = true)
    @Query("delete from ExternalEvent e where e.status = :status and e.businessDate <= :dateForPurgeCriteria")
    void deleteOlderEventsWithSentStatus(@Param("status") ExternalEventStatus status,
                                         @Param("dateForPurgeCriteria") LocalDate dateForPurgeCriteria);

    @Modifying
    @Query("UPDATE ExternalEvent e SET e.status = org.apache.fineract.infrastructure.event.external.repository.domain.ExternalEventStatus.SENT, e.sentAt = :sentAt WHERE e.id IN :ids")
    void markEventsSent(@Param("ids") List<Long> ids, @Param("sentAt") OffsetDateTime sentAt);
}
