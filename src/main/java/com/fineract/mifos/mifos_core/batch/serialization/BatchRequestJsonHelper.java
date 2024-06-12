package com.fineract.mifos.mifos_core.batch.serialization;

import com.fineract.mifos.mifos_core.batch.dtos.BatchRequest;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.FromJsonHelper;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Extends {@link com.fineract.mifos.mifos_core.infrastructure.core.serialization.FromJsonHelper} to de-serialize the incoming
 * String into a JSON List of type {@link com.fineract.mifos.mifos_core.batch.dtos.BatchRequest}
 *
 * @author Rishabh Shukla
 *
 * @see com.fineract.mifos.mifos_core.batch.dtos.BatchRequest
 *
 */
@Component
public class BatchRequestJsonHelper extends FromJsonHelper {
    /**
     * Returns a list of batchRequests after de-serializing it from the input JSON string.
     *
     * @return List&lt;BatchRequest&gt;
     */

    public List<BatchRequest> extractList(final String json) {
        final Type listType = new TypeToken<List<BatchRequest>>() {}.getType();
        return super.getGsonConverter().fromJson(json, listType);
    }
}
