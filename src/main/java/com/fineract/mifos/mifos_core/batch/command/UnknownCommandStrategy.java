package com.fineract.mifos.mifos_core.batch.command;

import org.springframework.stereotype.Component;

/**
 * Provides a default CommandStrategy by implementing {@link org.apache.fineract.batch.command.CommandStrategy} in case
 * there is no appropriate command strategy with requested 'method' and 'resoureUrl'.
 *
 * @author Peter Gichuki
 */
@Component
public class UnknownCommandStrategy implements CommandStrategy {
    @Override
    public BatchResponse execute(BatchRequest batchRequest, @SuppressWarnings("unused") UriInfo uriInfo) {
        return new BatchResponse().setRequestId(batchRequest.getRequestId()).setStatusCode(SC_NOT_IMPLEMENTED)
                .setBody("Resource with method " + batchRequest.getMethod() + " and relativeUrl " + batchRequest.getRelativeUrl()
                        + " doesn't exist");
    }
}
