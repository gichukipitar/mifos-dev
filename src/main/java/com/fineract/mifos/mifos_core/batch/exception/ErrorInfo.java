package com.fineract.mifos.mifos_core.batch.exception;

import com.fineract.mifos.mifos_core.batch.dtos.Header;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

/**
 * Provides members to hold the basic information about the exceptions raised in commandStrategy classes.
 *
 * @author Rishabh Shukla
 *
 * @see ErrorHandler
 */
@Getter
@Setter(PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public final class ErrorInfo {
    private Integer statusCode;
    private Integer errorCode;
    private String message;
    private Set<Header> headers;

}
