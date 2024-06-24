package com.fineract.mifos.mifos_core.infrastructure.codes.service;


import com.fineract.mifos.mifos_core.infrastructure.codes.entity.CodeValue;
import com.fineract.mifos.mifos_core.infrastructure.codes.exception.CodeValueNotFoundException;
import com.fineract.mifos.mifos_core.infrastructure.codes.repository.CodeValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Wrapper for {@link CodeValueRepository} that is responsible for checking if {@link CodeValue} is returned when using
 * <code>findOne</code> and <code>findByCodeNameAndId</code> repository methods and throwing an appropriate not found
 * exception.
 * </p>
 *
 * <p>
 * This is to avoid need for checking and throwing in multiple areas of code base where {@link CodeValueRepository} is
 * required.
 * </p>
 */
@Service
public class CodeValueRepositoryWrapper {
    private final CodeValueRepository repository;

    @Autowired
    public CodeValueRepositoryWrapper(final CodeValueRepository repository) {
        this.repository = repository;
    }

    public CodeValue findOneWithNotFoundDetection(final Long id) {
        return this.repository.findById(id).orElseThrow(() -> new CodeValueNotFoundException(id));
    }

    public CodeValue findOneByCodeNameAndIdWithNotFoundDetection(final String codeName, final Long id) {
        final CodeValue codeValue = this.repository.findByCodeNameAndId(codeName, id);
        if (codeValue == null) {
            throw new CodeValueNotFoundException(codeName, id);
        }
        return codeValue;
    }

    public CodeValue findOneByCodeNameAndLabelWithNotFoundDetection(final String codeName, final String label) {
        final CodeValue codeValue = this.repository.findByCodeNameAndLabel(codeName, label);
        if (codeValue == null) {
            throw new CodeValueNotFoundException(codeName, label);
        }
        return codeValue;
    }
}
