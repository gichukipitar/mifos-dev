package com.fineract.mifos.mifos_core.infrastructure.event.business.service;

import com.fineract.mifos.mifos_core.infrastructure.core.config.FineractProperties;
import com.fineract.mifos.mifos_core.infrastructure.event.business.BusinessEventListener;
import com.fineract.mifos.mifos_core.infrastructure.event.business.dto.BulkBusinessEvent;
import com.fineract.mifos.mifos_core.infrastructure.event.business.dto.BusinessEvent;
import com.fineract.mifos.mifos_core.infrastructure.event.business.dto.NoExternalEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
@RequiredArgsConstructor
@Slf4j
public class BusinessEventNotifierServiceImpl implements BusinessEventNotifierService, InitializingBean {

    private final Map<Class, List<BusinessEventListener>> preListeners = new HashMap<>();
    private final Map<Class, List<BusinessEventListener>> postListeners = new HashMap<>();

    private final ThreadLocal<Boolean> eventRecordingEnabled = ThreadLocal.withInitial(() -> false);
    private final ThreadLocal<List<BusinessEvent<?>>> recordedEvents = ThreadLocal.withInitial(ArrayList::new);

    private final ExternalEventService externalEventService;
    private final ExternalEventConfigurationRepository eventConfigurationRepository;
    private final FineractProperties fineractProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (isExternalEventPostingEnabled()) {
            log.info("External event posting is enabled");
        } else {
            log.info("External event posting is disabled");
        }
    }

    @Override
    public void notifyPreBusinessEvent(BusinessEvent<?> businessEvent) {
        throwExceptionIfBulkEvent(businessEvent);
        List<BusinessEventListener> businessEventListeners = findSuitableListeners(preListeners, businessEvent.getClass());
        for (BusinessEventListener eventListener : businessEventListeners) {
            eventListener.onBusinessEvent(businessEvent);
        }
    }

    @Override
    public <T extends BusinessEvent<?>> void addPreBusinessEventListener(Class<T> eventType, BusinessEventListener<T> listener) {
        List<BusinessEventListener> businessEventListeners = preListeners.computeIfAbsent(eventType, k -> new ArrayList<>());
        businessEventListeners.add(listener);
    }

    @Override
    public void notifyPostBusinessEvent(BusinessEvent<?> businessEvent) {
        throwExceptionIfBulkEvent(businessEvent);
        boolean isExternalEvent = !(businessEvent instanceof NoExternalEvent);
        List<BusinessEventListener> businessEventListeners = findSuitableListeners(postListeners, businessEvent.getClass());
        for (BusinessEventListener eventListener : businessEventListeners) {
            eventListener.onBusinessEvent(businessEvent);
        }
        if (isExternalEvent && isExternalEventPostingEnabled()) {
            // we only want to create external events for operations that were successful, hence the post listener
            if (isExternalEventConfiguredForPosting(businessEvent.getType())) {
                if (isExternalEventRecordingEnabled()) {
                    recordedEvents.get().add(businessEvent);
                } else {
                    externalEventService.postEvent(businessEvent);
                }
            }
        }
    }

    private List<BusinessEventListener> findSuitableListeners(Map<Class, List<BusinessEventListener>> listeners, Class<?> eventClazz) {
        List<BusinessEventListener> result = new ArrayList<>();
        for (Map.Entry<Class, List<BusinessEventListener>> entry : listeners.entrySet()) {
            Class<?> registeredClazz = entry.getKey();
            if (registeredClazz.isAssignableFrom(eventClazz)) {
                result.addAll(entry.getValue());
            }
        }
        return result;
    }

    @Override
    public <T extends BusinessEvent<?>> void addPostBusinessEventListener(Class<T> eventType, BusinessEventListener<T> listener) {
        List<BusinessEventListener> businessEventListeners = postListeners.get(eventType);
        if (businessEventListeners == null) {
            businessEventListeners = new ArrayList<>();
            postListeners.put(eventType, businessEventListeners);
        }
        businessEventListeners.add(listener);
    }

    private boolean isExternalEventRecordingEnabled() {
        return eventRecordingEnabled.get();
    }

    private boolean isExternalEventPostingEnabled() {
        return fineractProperties.getEvents().getExternal().isEnabled();
    }

    private boolean isExternalEventConfiguredForPosting(String eventType) {
        return eventConfigurationRepository.findExternalEventConfigurationByTypeWithNotFoundDetection(eventType).isEnabled();
    }

    private void throwExceptionIfBulkEvent(BusinessEvent<?> businessEvent) {
        if (businessEvent instanceof BulkBusinessEvent) {
            throw new IllegalArgumentException("BulkBusinessEvent cannot be raised directly");
        }
    }

    @Override
    public void startExternalEventRecording() {
        eventRecordingEnabled.set(true);
    }

    @Override
    public void stopExternalEventRecording() {
        eventRecordingEnabled.set(false);
        try {
            List<BusinessEvent<?>> recordedBusinessEvents = recordedEvents.get();
            if (isExternalEventPostingEnabled()) {
                if (recordedBusinessEvents.isEmpty()) {
                    log.debug("Not posting a BulkBusinessEvent since there were no events recorded");
                } else {
                    if (recordedBusinessEvents.size() == 1) {
                        log.debug("Posting a singular event instead of a BulkBusinessEvent since there was only a single event recorded");
                        externalEventService.postEvent(recordedBusinessEvents.get(0));
                    } else {
                        log.debug("Posting the BulkBusinessEvent for the recorded {} events", recordedBusinessEvents.size());
                        externalEventService.postEvent(new BulkBusinessEvent(recordedBusinessEvents));
                    }
                }
            }
        } finally {
            recordedEvents.remove();
        }
    }

    @Override
    public void resetEventRecording() {
        eventRecordingEnabled.set(false);
        recordedEvents.remove();
    }
}
