package com.fineract.mifos.mifos_core.batch.command;
/**
 * An interface for various Command Strategies. It contains a single function which returns appropriate response from a
 * particular command strategy.
 *
 * @author Peter Gichuki
 *
 * @see org.apache.fineract.batch.command.internal.UnknownCommandStrategy
 */
public interface CommandStrategy {
    /**
     * Returns an object of type {@link org.apache.fineract.batch.domain.BatchResponse}. This takes
     * {@link org.apache.fineract.batch.domain.BatchRequest} as it's single argument and provides appropriate response.
     *
     * @param batchRequest
     * @param uriInfo
     * @return BatchResponse
     */
    BatchResponse execute(BatchRequest batchRequest, UriInfo uriInfo);
}
