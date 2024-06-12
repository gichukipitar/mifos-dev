package com.fineract.mifos.mifos_core.batch.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Provides an object to handle HTTP headers as name and value pairs for Batch API. It is used in {@link BatchRequest}
 * and {@link BatchResponse} to store the information regarding the headers in incoming and outgoing JSON Strings.
 *
 * @author Peter Gichuki
 *
 * @see BatchRequest
 * @see BatchResponse
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Header {
    private String name;
    private String value;
}
