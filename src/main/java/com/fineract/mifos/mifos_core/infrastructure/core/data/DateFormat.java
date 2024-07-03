package com.fineract.mifos.mifos_core.infrastructure.core.data;

import com.fineract.mifos.mifos_core.infrastructure.core.dto.ApiParameterError;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.PlatformApiDataValidationException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.util.List;

@Getter
public class DateFormat {

    private final String dateFormat;

    public DateFormat(String rawDateFormat) {
        if (StringUtils.isBlank(rawDateFormat)) {
            final ApiParameterError error = ApiParameterError.parameterError("validation.msg.invalid.dateFormat.format",
                    "Dateformat is null", rawDateFormat);
            throw new PlatformApiDataValidationException("validation.msg.invalid.dateFormat.format", "Validation errors exist.",
                    List.of(error));
        } else {
            String compatibleDateFormat = rawDateFormat.replace("yyyy", "uuuu");
            validate(compatibleDateFormat);
            dateFormat = compatibleDateFormat;
        }
    }

    private void validate(String dateTimeFormat) {
        try {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().parseLenient().appendPattern(dateTimeFormat)
                    .optionalStart().appendPattern(" HH:mm:ss").optionalEnd().parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0).parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0).toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT);
        } catch (final IllegalArgumentException | DateTimeParseException e) {
            final ApiParameterError error = ApiParameterError.parameterError("validation.msg.invalid.dateFormat.format",
                    "Invalid dateFormat: `" + dateTimeFormat, dateTimeFormat);
            throw new PlatformApiDataValidationException("validation.msg.invalid.dateFormat.format", "Validation errors exist.",
                    List.of(error), e);
        }
    }

}
