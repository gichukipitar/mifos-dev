package com.fineract.mifos.mifos_core.infrastructure.codes.exception;

import com.fineract.mifos.mifos_core.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * A {@link RuntimeException} thrown when client resources are not found.
 */

public class CodeValueNotFoundException extends AbstractPlatformResourceNotFoundException {

    public CodeValueNotFoundException(final Long id) {
        super("error.msg.codevalue.id.invalid", "Code value with identifier " + id + " does not exist", id);
    }

    public CodeValueNotFoundException(final String codeName, final Long id) {
        super("error.msg.codevalue.codename.id.combination.invalid",
                "Code value with identifier " + id + " does not exist for a code with name " + codeName, id, codeName);
    }

    public CodeValueNotFoundException(final String codeName, final String label) {
        super("error.msg.codevalue.codename.id.combination.invalid",
                "Code value with label " + label + " does not exist for a code with name " + codeName, label, codeName);
    }

    public CodeValueNotFoundException(Long id, EmptyResultDataAccessException e) {
        super("error.msg.codevalue.id.invalid", "Code value with identifier " + id + " does not exist", id, e);
    }

}
