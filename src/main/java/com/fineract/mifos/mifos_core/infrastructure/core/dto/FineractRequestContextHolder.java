package com.fineract.mifos.mifos_core.infrastructure.core.dto;

import com.fineract.mifos.mifos_core.infrastructure.core.domain.BatchRequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;

/**
 * Common context holder to store environment-specific request attributes. <br/>
 * It will try to get the attribute from the following order:
 *
 * <pre>
 *   <ul>
 *     <li>{@link HttpServletRequest} (if the parameter not null)</li>
 *     <li>{@link BatchRequestContextHolder} if it's a batch request set int the current thread</li>
 *     <li>{@link RequestContextHolder} if it exists in the current thread</li>
 *   </ul>
 * </pre>
 */
@Component
@NoArgsConstructor
@Slf4j
public class FineractRequestContextHolder {

    /**
     * Get the attribute, the request not set using only thread bound context variables
     *
     * @param key
     *            attribute key
     * @return attribute value or null if it not set in the current thread
     */
    public Object getAttribute(String key) {
        return getAttribute(key, null);
    }

    /**
     * Get the attribute.
     *
     * The method will check the request attribute first, then the thread bound context variables
     *
     * TODO: in {@link org.springframework.context.ApplicationEvent} always return null
     *
     * @param key
     *            attribute key
     * @param request
     *            {@link HttpServletRequest} object
     * @return attribute value or null if it not set in the current thread or {@link HttpServletRequest} (if not null)
     */
    public Object getAttribute(String key, HttpServletRequest request) {
        if (request != null) {
            return request.getAttribute(key);
        } else if (isBatchRequest()) {
            return Optional.ofNullable(BatchRequestContextHolder.getRequestAttributes()).map(attributes -> attributes.get(key))
                    .orElse(null);
        } else if (RequestContextHolder.getRequestAttributes() != null) {
            return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                    .map(r -> r.getAttribute(key, RequestAttributes.SCOPE_REQUEST)).orElse(null);
        }
        return null;
    }

    /**
     * Set the attribute
     *
     * @param key
     *            attribute key
     * @param value
     *            attribute value
     */
    public void setAttribute(String key, Object value) {
        setAttribute(key, value, null);
    }

    /**
     * Set the attribute.
     *
     * If the request is not null, it will set the attribute in the request otherwise it will set it in the thread bound
     * context variables
     *
     * TODO: in {@link org.springframework.context.ApplicationEvent} the attributes not set
     *
     * @param key
     *            attribute key
     * @param value
     *            attribute value
     * @param request
     *            {@link HttpServletRequest} object
     */
    public void setAttribute(String key, Object value, HttpServletRequest request) {
        if (request != null) {
            request.setAttribute(key, value);
        } else if (isBatchRequest()) {
            Optional.ofNullable(BatchRequestContextHolder.getRequestAttributes()).ifPresent(attributes -> attributes.put(key, value));
        } else if (RequestContextHolder.getRequestAttributes() != null) {
            Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                    .ifPresent(requestAttributes -> requestAttributes.setAttribute(key, value, RequestAttributes.SCOPE_REQUEST));
        }

    }

    /**
     * True if {@link BatchRequestContextHolder} will be set in the current thread
     *
     * @return true if the current request is a batch request
     */
    public static boolean isBatchRequest() {
        return BatchRequestContextHolder.isBatchRequest();
    }
}
