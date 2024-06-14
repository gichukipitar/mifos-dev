package com.fineract.mifos.mifos_core.commands.service;

import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.GoogleGsonSerializerHelper;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.ToApiJsonSerializer;
import com.fineract.mifos.mifos_core.infrastructure.security.service.PlatformSecurityContext;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SynchronousCommandProcessingService implements CommandProcessingService {

    public static final String IDEMPOTENCY_KEY_STORE_FLAG = "idempotencyKeyStoreFlag";

    public static final String IDEMPOTENCY_KEY_ATTRIBUTE = "IdempotencyKeyAttribute";
    public static final String COMMAND_SOURCE_ID = "commandSourceId";
    private final PlatformSecurityContext context;
    private final ApplicationContext applicationContext;
    private final ToApiJsonSerializer<Map<String, Object>> toApiJsonSerializer;
    private final ToApiJsonSerializer<CommandProcessingResult> toApiResultJsonSerializer;
    private final ConfigurationDomainService configurationDomainService;
    private final CommandHandlerProvider commandHandlerProvider;
    private final IdempotencyKeyResolver idempotencyKeyResolver;
    private final CommandSourceService commandSourceService;

    private final FineractRequestContextHolder fineractRequestContextHolder;
    private final Gson gson = GoogleGsonSerializerHelper.createSimpleGson();

}
