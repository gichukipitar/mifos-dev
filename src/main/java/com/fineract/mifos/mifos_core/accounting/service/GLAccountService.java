package com.fineract.mifos.mifos_core.accounting.service;

import com.fineract.mifos.mifos_core.accounting.api.GLAccountJsonInputParams;
import com.fineract.mifos.mifos_core.accounting.entity.GLAccount;
import com.fineract.mifos.mifos_core.accounting.repository.GLAccountRepository;
import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class GLAccountService {
    private final GLAccountRepository glAccountRepository;

    public GLAccountService(GLAccountRepository glAccountRepository) {
        this.glAccountRepository = glAccountRepository;
    }

    public static GLAccount fromJson(final GLAccount parent, final JsonCommand command, final CodeValue glAccountTagType) {
        final String name = command.stringValueOfParameterNamed(GLAccountJsonInputParams.NAME.getValue());
        final String glCode = command.stringValueOfParameterNamed(GLAccountJsonInputParams.GL_CODE.getValue());
        final boolean disabled = command.booleanPrimitiveValueOfParameterNamed(GLAccountJsonInputParams.DISABLED.getValue());
        final boolean manualEntriesAllowed = command
                .booleanPrimitiveValueOfParameterNamed(GLAccountJsonInputParams.MANUAL_ENTRIES_ALLOWED.getValue());
        final Integer usage = command.integerValueSansLocaleOfParameterNamed(GLAccountJsonInputParams.USAGE.getValue());
        final Integer type = command.integerValueSansLocaleOfParameterNamed(GLAccountJsonInputParams.TYPE.getValue());
        final String description = command.stringValueOfParameterNamed(GLAccountJsonInputParams.DESCRIPTION.getValue());
        return new GLAccount().setParent(parent).setName(name).setGlCode(glCode).setDisabled(disabled)
                .setManualEntriesAllowed(manualEntriesAllowed).setType(type).setUsage(usage).setDescription(description)
                .setTagId(glAccountTagType);
    }

    public Map<String, Object> update(final GLAccount glAccount, final JsonCommand command) {
        final Map<String, Object> actualChanges = new LinkedHashMap<>(15);
        handlePropertyUpdate(command, actualChanges, GLAccountJsonInputParams.DESCRIPTION.getValue(), glAccount.getDescription());
        handlePropertyUpdate(command, actualChanges, GLAccountJsonInputParams.DISABLED.getValue(), glAccount.isDisabled());
        handlePropertyUpdate(command, actualChanges, GLAccountJsonInputParams.GL_CODE.getValue(), glAccount.getGlCode());
        handlePropertyUpdate(command, actualChanges, GLAccountJsonInputParams.MANUAL_ENTRIES_ALLOWED.getValue(), glAccount.isManualEntriesAllowed());
        handlePropertyUpdate(command, actualChanges, GLAccountJsonInputParams.NAME.getValue(), glAccount.getName());
        handlePropertyUpdate(command, actualChanges, GLAccountJsonInputParams.PARENT_ID.getValue(), 0L);
        handlePropertyUpdate(command, actualChanges, GLAccountJsonInputParams.TYPE.getValue(), glAccount.getType(), true);
        handlePropertyUpdate(command, actualChanges, GLAccountJsonInputParams.USAGE.getValue(), glAccount.getUsage(), true);
        handlePropertyUpdate(command, actualChanges, GLAccountJsonInputParams.TAGID.getValue(),
                glAccount != null && glAccount.getTagId() != null ? glAccount.getId() : 0L);

        return actualChanges;
    }

    private void handlePropertyUpdate(final GLAccount glAccount, final JsonCommand command, final Map<String, Object> actualChanges, final String paramName,
                                      final Integer propertyToBeUpdated, final boolean sansLocale) {
        boolean changeDetected = false;
        if (sansLocale) {
            changeDetected = command.isChangeInIntegerSansLocaleParameterNamed(paramName, propertyToBeUpdated);
        } else {
            changeDetected = command.isChangeInIntegerParameterNamed(paramName, propertyToBeUpdated);
        }
        if (changeDetected) {
            Integer newValue = null;
            if (sansLocale) {
                newValue = command.integerValueSansLocaleOfParameterNamed(paramName);
            } else {
                newValue = command.integerValueOfParameterNamed(paramName);
            }
            actualChanges.put(paramName, newValue);
            // now update actual property
            if (paramName.equals(GLAccountJsonInputParams.TYPE.getValue())) {
                glAccount.setType(newValue);
            } else if (paramName.equals(GLAccountJsonInputParams.USAGE.getValue())) {
                glAccount.setUsage(newValue);
            }
        }
    }

    private void handlePropertyUpdate(final JsonCommand command, final GLAccount glAccount, final Map<String, Object> actualChanges, final String paramName,
                                      final String propertyToBeUpdated) {
        if (command.isChangeInStringParameterNamed(paramName, propertyToBeUpdated)) {
            final String newValue = command.stringValueOfParameterNamed(paramName);
            actualChanges.put(paramName, newValue);
            // now update actual property
            if (paramName.equals(GLAccountJsonInputParams.DESCRIPTION.getValue())) {
                glAccount.setDescription(newValue);
            } else if (paramName.equals(GLAccountJsonInputParams.GL_CODE.getValue())) {
                glAccount.setGlCode(newValue);
            } else if (paramName.equals(GLAccountJsonInputParams.NAME.getValue())) {
                glAccount.setName(newValue);
            }
        }
    }

    private void handlePropertyUpdate(final JsonCommand command, final Map<String, Object> actualChanges, final String paramName,
                                      final Long propertyToBeUpdated) {
        if (command.isChangeInLongParameterNamed(paramName, propertyToBeUpdated)) {
            final Long newValue = command.longValueOfParameterNamed(paramName);
            actualChanges.put(paramName, newValue);
            // now update actual property
            if (paramName.equals(GLAccountJsonInputParams.PARENT_ID.getValue())) {
                // do nothing as this is a nested property
            }
        }
    }

    private void handlePropertyUpdate(final JsonCommand command, final GLAccount glAccount, final Map<String, Object> actualChanges, final String paramName,
                                      final boolean propertyToBeUpdated) {
        if (command.isChangeInBooleanParameterNamed(paramName, propertyToBeUpdated)) {
            final Boolean newValue = command.booleanObjectValueOfParameterNamed(paramName);
            actualChanges.put(paramName, newValue);
            // now update actual property
            if (paramName.equals(GLAccountJsonInputParams.MANUAL_ENTRIES_ALLOWED.getValue())) {
                glAccount.setManualEntriesAllowed(newValue);
            } else if (paramName.equals(GLAccountJsonInputParams.DISABLED.getValue())) {
                glAccount.setDisabled(newValue);
            }
        }
    }

        public boolean isHeaderAccount () {
            return GLAccountUsage.HEADER.getValue().equals(this.usage);
        }

        public void generateHierarchy () {

            if (glAccount.getParent() != null) {
                glAccount.setHierarchy(glAccount.getParent().getHierarchy() + glAccount.getId() + ".");

            } else {
                glAccount.setHierarchy(".");
            }
        }

        private String hierarchyOf ( final Long id){
            return glAccount.getHierarchy() + id.toString() + ".";
        }

        public boolean isDetailAccount () {
            return GLAccountUsage.DETAIL.getValue().equals(this.usage);
        }
    }


