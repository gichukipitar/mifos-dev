package com.fineract.mifos.mifos_core.accounting.exception;


import com.fineract.mifos.mifos_core.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * A {@link RuntimeException} thrown when GL account resources are not found.
 */
public class GLAccountNotFoundException  extends AbstractPlatformResourceNotFoundException {

    public GLAccountNotFoundException(final Long id) {
        super("error.msg.glaccount.id.invalid", "General Ledger account with identifier " + id + " does not exist ", id);
    }

    public GLAccountNotFoundException(final Long id, EmptyResultDataAccessException e) {
        super("error.msg.glaccount.id.invalid", "General Ledger account with identifier " + id + " does not exist ", id, e);
    }

    public GLAccountNotFoundException(final String glCode) {
        super("error.msg.glaccount.code.invalid", "General Ledger account with GlCode " + glCode + " does not exist ", glCode);
    }

}
