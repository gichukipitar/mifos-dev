package com.fineract.mifos.mifos_core.infrastructure.core.service;

import com.fineract.mifos.mifos_core.infrastructure.core.serialization.JsonParserHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Locale;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
public class PagedLocalRequest<T> extends PagedRequest<T> {

    private String dateFormat;

    private String dateTimeFormat;

    private String locale;

    public Locale getLocaleObject() {
        return locale == null ? null : JsonParserHelper.localeFromString(locale);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof PagedLocalRequest)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        PagedLocalRequest<?> that = (PagedLocalRequest<?>) o;
        return Objects.equals(dateFormat, that.dateFormat) && Objects.equals(dateTimeFormat, that.dateTimeFormat)
                && Objects.equals(locale, that.locale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dateFormat, dateTimeFormat, locale);
    }
}
