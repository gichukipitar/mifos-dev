package com.fineract.mifos.mifos_core.batch.command;

import com.fineract.mifos.mifos_core.batch.dtos.BatchRequest;
import com.fineract.mifos.mifos_core.batch.dtos.BatchResponse;

/**
 * An interface for various Command Strategies. It contains a single function which returns appropriate response from a
 * particular command strategy.
 *
 * @author Peter Gichuki
 *
 * @see com.fineract.mifos.mifos_core.batch.command.UnknownCommandStrategy
 */
public interface CommandStrategy {
    /**
     * Returns an object of type {}. This takes
     * {} as it's single argument and provides appropriate response.
     *
     * @return BatchResponse
     */
    BatchResponse execute(BatchRequest batchRequest, UriInfo uriInfo);
}
