package com.fineract.mifos.mifos_core.infrastructure.businessdate.validator;

import com.fineract.mifos.mifos_core.infrastructure.businessdate.dto.BusinessDateData;
import com.fineract.mifos.mifos_core.infrastructure.businessdate.dto.BusinessDateType;
import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.DataValidatorBuilder;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.FromJsonHelper;
import com.fineract.mifos.mifos_core.infrastructure.businessdate.validator.BusinessDateDataParserAndValidator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Component
public class BusinessDateDataParserAndValidator {
    private final FromJsonHelper jsonHelper;

    public BusinessDateData validateAndParseUpdate(@NotNull final JsonCommand command) {
        final DataValidatorBuilder dataValidator = new DataValidatorBuilder(new ArrayList<>()).resource("businessdate.update");
        JsonObject element = extractJsonObject(command);

        BusinessDateData result = validateAndParseUpdate(dataValidator, element, jsonHelper);
        throwExceptionIfValidationWarningsExist(dataValidator);

        return result;
    }

    private JsonObject extractJsonObject(JsonCommand command) {
        String json = command.json();
        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }

        final JsonElement element = jsonHelper.parse(json);
        return element.getAsJsonObject();
    }

    private void throwExceptionIfValidationWarningsExist(DataValidatorBuilder dataValidator) {
        if (dataValidator.hasError()) {
            log.error("Business date - Validation errors: {}", dataValidator.getDataValidationErrors());
            throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist", "Validation errors exist.",
                    dataValidator.getDataValidationErrors());
        }
    }

    private BusinessDateData validateAndParseUpdate(final DataValidatorBuilder dataValidator, JsonObject element,
                                                    FromJsonHelper jsonHelper) {
        if (element == null) {
            return null;
        }

        jsonHelper.checkForUnsupportedParameters(element, List.of("type", "date", "dateFormat", "locale"));

        String businessDateTypeName = jsonHelper.extractStringNamed("type", element);
        final String localeValue = jsonHelper.extractStringNamed("locale", element);
        final String dateFormat = jsonHelper.extractDateFormatParameter(element);
        final String dateValue = jsonHelper.extractStringNamed("date", element);
        dataValidator.reset().parameter("type").value(businessDateTypeName).notBlank();
        dataValidator.reset().parameter("locale").value(localeValue).notBlank();
        dataValidator.reset().parameter("dateFormat").value(dateFormat).notBlank();
        dataValidator.reset().parameter("date").value(dateValue).notBlank();

        if (dataValidator.hasError()) {
            return null;
        }
        Locale locale = jsonHelper.extractLocaleParameter(element);
        BusinessDateType type;
        try {
            type = BusinessDateType.valueOf(businessDateTypeName);
        } catch (IllegalArgumentException e) {
            dataValidator.reset().parameter("type").failWithCode("Invalid Business Type value: `" + businessDateTypeName + "`");
            return null;
        }
        LocalDate date = jsonHelper.extractLocalDateNamed("date", element, dateFormat, locale);
        dataValidator.reset().parameter("date").value(date).notNull();
        return dataValidator.hasError() ? null : BusinessDateData.instance(type, date);
    }

}
