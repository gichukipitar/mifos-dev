package com.fineract.mifos.mifos_core.batch.service;

import com.fineract.mifos.mifos_core.batch.dtos.BatchRequest;
import com.fineract.mifos.mifos_core.batch.dtos.BatchResponse;
import com.fineract.mifos.mifos_core.batch.exception.BatchReferenceInvalidException;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.FromJsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.common.base.Splitter;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provides methods to create dependency map among the various batchRequests. It also provides method that takes care of
 * dependency resolution among related requests.
 *
 * @author Rishabh Shukla
 * @see BatchApiServiceImpl
 */
@Component
@Data
@RequiredArgsConstructor
public class ResolutionHelper {
    /**
     * Provides a Node like object for the request tree.
     *
     * @author Peter Gichuki
     *
     */
    public static class BatchRequestNode {

        @Getter
        private BatchRequest request;
        private final List<BatchRequestNode> childRequests = new ArrayList<>();

        public BatchRequestNode(BatchRequest request) {
            this.request = request;
        }

        public List<BatchRequestNode> getChildNodes() {
            return this.childRequests;
        }

        public void addChildNode(final BatchRequestNode batchRequest) {
            this.childRequests.add(batchRequest);
        }

    }
    private final FromJsonHelper fromJsonHelper;

    /**
     * Returns a map containing requests that are divided in accordance of dependency relations among them. Each
     * different list is identified with a "Key" which is the "requestId" of the request at topmost level in dependency
     * hierarchy of that particular list.
     *
     * @param requests
     * @return List&lt;ArrayList&lt;BatchRequestNode&gt;&gt;
     */
    public List<BatchRequestNode> buildNodesTree(final List<BatchRequest> requests) {
        final List<BatchRequestNode> rootNodes = new ArrayList<>();
        for (BatchRequest request : requests) {
            if (request.getReference() == null) {
                final BatchRequestNode node = new BatchRequestNode(request);
                rootNodes.add(node);
            } else {
                if (!addDependingRequest(request, rootNodes)) {
                    throw new BatchReferenceInvalidException(request.getReference());
                }
            }
        }
        return rootNodes;
    }

    private boolean addDependingRequest(final BatchRequest request, final List<BatchRequestNode> parentNodes) {
        for (BatchRequestNode parentNode : parentNodes) {
            if (parentNode.getRequest().getRequestId().equals(request.getReference())) {
                final BatchRequestNode childNode = new BatchRequestNode(request);
                parentNode.addChildNode(childNode);
                return true;
            } else {
                if (addDependingRequest(request, parentNode.getChildNodes())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a BatchRequest after dependency resolution. It takes a request and the response of the request it is
     * dependent upon as its arguments and change the body or relativeUrl of the request according to parent Request.
     *
     * @param request
     * @param parentResponse
     * @return BatchRequest
     */
    public BatchRequest resolveRequest(final BatchRequest request, final BatchResponse parentResponse) {
        final ReadContext responseCtx = JsonPath.parse(parentResponse.getBody());
        // Gets the body from current Request as a JsonObject
        String requestBody = request.getBody();
        if (requestBody != null) {
            final JsonObject jsonRequestBody = this.fromJsonHelper.parse(requestBody).getAsJsonObject();
            JsonObject jsonResultBody = new JsonObject();
            // Iterate through each element in the requestBody to find dependent
            // parameter
            for (Map.Entry<String, JsonElement> element : jsonRequestBody.entrySet()) {
                final String key = element.getKey();
                final JsonElement value = resolveDependentVariables(element, responseCtx);
                jsonResultBody.add(key, value);
            }
            // Set the body after dependency resolution
            request.setBody(jsonResultBody.toString());
        }

        // Also check the relativeUrl for any dependency resolution
        String relativeUrl = request.getRelativeUrl();
        if (relativeUrl.contains("$.")) {
            String queryParams = "";
            if (relativeUrl.contains("?")) {
                queryParams = relativeUrl.substring(relativeUrl.indexOf("?"));
                relativeUrl = relativeUrl.substring(0, relativeUrl.indexOf("?"));
            }

            final Iterable<String> parameters = Splitter.on('/').split(relativeUrl);
            for (String parameter : parameters) {
                if (parameter.contains("$.")) {
                    final String resParamValue = responseCtx.read(parameter).toString();
                    relativeUrl = relativeUrl.replace(parameter, resParamValue);
                    request.setRelativeUrl(relativeUrl + queryParams);
                }
            }
        }

        return request;
    }

    private JsonElement resolveDependentVariables(final Map.Entry<String, JsonElement> entryElement, final ReadContext responseCtx) {
        JsonElement value;
        final JsonElement element = entryElement.getValue();
        if (element.isJsonObject()) {
            final JsonObject jsObject = element.getAsJsonObject();
            value = processJsonObject(jsObject, responseCtx);
        } else if (element.isJsonArray()) {
            final JsonArray jsElementArray = element.getAsJsonArray();
            value = processJsonArray(jsElementArray, responseCtx);
        } else if (element.isJsonNull()) {
            // No further processing of null values
            value = element;
        } else {
            value = resolveDependentVariable(element, responseCtx);
        }
        return value;
    }

    private JsonElement processJsonObject(final JsonObject jsObject, final ReadContext responseCtx) {
        JsonObject valueObj = new JsonObject();
        for (Map.Entry<String, JsonElement> element : jsObject.entrySet()) {
            final String key = element.getKey();
            final JsonElement value = resolveDependentVariable(element.getValue(), responseCtx);
            valueObj.add(key, value);
        }
        return valueObj;
    }

    private JsonArray processJsonArray(final JsonArray elementArray, final ReadContext responseCtx) {
        JsonArray valueArr = new JsonArray();
        for (JsonElement element : elementArray) {
            if (element.isJsonObject()) {
                final JsonObject jsObject = element.getAsJsonObject();
                valueArr.add(processJsonObject(jsObject, responseCtx));
            }
        }
        return valueArr;
    }

    private JsonElement resolveDependentVariable(final JsonElement element, final ReadContext responseCtx) {
        JsonElement value = element;
        String paramVal = element.getAsString();
        if (paramVal.contains("$.")) {
            // Get the value of the parameter from parent response
            final String resParamValue = responseCtx.read(paramVal).toString();
            value = this.fromJsonHelper.parse(resParamValue);
        }
        return value;
    }
}
