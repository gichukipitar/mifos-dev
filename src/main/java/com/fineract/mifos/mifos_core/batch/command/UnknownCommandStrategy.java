package com.fineract.mifos.mifos_core.batch.command;

import com.fineract.mifos.mifos_core.batch.dtos.BatchRequest;
import com.fineract.mifos.mifos_core.batch.dtos.BatchResponse;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.UriInfo;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_IMPLEMENTED;

/**
 * Provides a default CommandStrategy by implementing in case
 * there is no appropriate command strategy with requested 'method' and 'resourceUrl'.
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
