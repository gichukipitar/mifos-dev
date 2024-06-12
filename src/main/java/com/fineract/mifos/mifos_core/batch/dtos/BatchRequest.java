package com.fineract.mifos.mifos_core.batch.dtos;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * Provides an object for separate HTTP requests in the Batch Request for Batch API. A requestId is also included as
 * data field which takes care of dependency issues among various requests. This class also provides getter and setter
 * functions to access Batch Request data fields.
 *
 * @author Peter Gichuki
 *
 *
 * @see Header
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class BatchRequest {
    private Long requestId;
    private String relativeUrl;
    private String method;
    private Set<Header> headers;
    private Long reference;
    private String body;
}
