package com.fineract.mifos.mifos_core.infrastructure.core.api;


import com.fineract.mifos.mifos_core.infrastructure.core.serialization.JsonParserHelper;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Locale;

/**
 * Class for parsing dates sent as query parameters
 */
public class DateParam {

    private final String dateAsString;

    public DateParam(final String dateStr) {
        this.dateAsString = dateStr;
    }

    public LocalDate getDate(final String parameterName, final DateFormat dateFormat, final String localeAsString) {
        final Locale locale = JsonParserHelper.localeFromString(localeAsString);
        return JsonParserHelper.convertFrom(this.dateAsString, parameterName, dateFormat.toString(), locale);
    }

}
