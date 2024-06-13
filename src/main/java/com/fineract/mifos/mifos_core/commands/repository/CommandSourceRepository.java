package com.fineract.mifos.mifos_core.commands.repository;

import com.fineract.mifos.mifos_core.commands.entity.CommandSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface CommandSourceRepository extends JpaRepository<CommandSource, Long>, JpaSpecificationExecutor<CommandSource> {
    CommandSource findByActionNameAndEntityNameAndIdempotencyKey(String actionName, String entityName, String idempotencyKey);

    @Modifying(flushAutomatically = true)
    @Query("delete from CommandSource c where c.status = :status and c.madeOnDate is not null and c.madeOnDate <= :dateForPurgeCriteria")
    void deleteOlderEventsWithStatus(@Param("status") Integer status, @Param("dateForPurgeCriteria") OffsetDateTime dateForPurgeCriteria);
}
