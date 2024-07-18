package com.fineract.mifos.mifos_core.infrastructure.event.business.dto;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class BulkBusinessEvent extends AbstractBusinessEvent<List<BusinessEvent<?>>> {

    private static final String CATEGORY = "Bulk";
    public static final String TYPE = "BulkBusinessEvent";

    public BulkBusinessEvent(List<BusinessEvent<?>> value) {
        super(value);
        verifySameAggregate(value);
    }

    private void verifySameAggregate(List<BusinessEvent<?>> events) {
        Set<Long> aggregateRootIds = events.stream().map(BusinessEvent::getAggregateRootId).filter(Objects::nonNull).collect(toSet());
        if (aggregateRootIds.size() > 1) {
            throw new IllegalArgumentException("The business events are related to multiple aggregate roots which is not allowed");
        }
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getCategory() {
        return CATEGORY;
    }

    @Override
    public Long getAggregateRootId() {
        return get().iterator().next().getAggregateRootId();
    }
}
