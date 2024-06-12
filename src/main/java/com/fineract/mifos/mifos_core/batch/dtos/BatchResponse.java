package com.fineract.mifos.mifos_core.batch.dtos;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * Provides an object for separate HTTP responses in the Batch Response for Batch API. It contains all the information
 * about a particular HTTP response in the Batch Response. Getter and Setter functions are also included to access
 * response data fields.
 *
 * @author Peter Gichuki
 *

 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class BatchResponse {
    private Long requestId;
    private Integer statusCode;
    private Set<Header> headers;
    private String body;
}
