package com.fineract.mifos.mifos_core.infrastructure.codes.entity;

import com.fineract.mifos.mifos_core.infrastructure.codes.CodeConstants;
import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.entity.AbstractPersistableCustom;
import com.fineract.mifos.mifos_core.infrastructure.codes.dto.CodeValueData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;


@Entity
@Table(name = "m_code_value", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "code_id", "code_value" }, name = "code_value_duplicate") })
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class CodeValue extends AbstractPersistableCustom {

    @Column(name = "code_value", length = 100)
    private String label;

    @Column(name = "order_position")
    private int position;

    @Column(name = "code_description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "code_id", nullable = false)
    private Code code;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_mandatory")
    private boolean mandatory;

    public static CodeValue createNew(final Code code, final String label, final int position, final String description,
                                      final boolean isActive, final boolean mandatory) {
        return new CodeValue().setCode(code).setLabel(StringUtils.defaultIfEmpty(label, null)).setPosition(position)
                .setDescription(description).setActive(isActive).setMandatory(mandatory);
    }

    public static CodeValue fromJson(final Code code, final JsonCommand command) {

        final String label = command.stringValueOfParameterNamed(CodeConstants.CodevalueJSONinputParams.NAME.getValue());
        Integer position = command.integerValueSansLocaleOfParameterNamed(CodeConstants.CodevalueJSONinputParams.POSITION.getValue());
        String description = command.stringValueOfParameterNamed(CodeConstants.CodevalueJSONinputParams.DESCRIPTION.getValue());
        Boolean isActiveObj = command.booleanObjectValueOfParameterNamed(CodeConstants.CodevalueJSONinputParams.IS_ACTIVE.getValue());
        boolean isActive = true;
        if (isActiveObj != null) {
            isActive = isActiveObj;
        }
        if (position == null) {
            position = 0;
        }

        Boolean mandatory = command.booleanPrimitiveValueOfParameterNamed(CodeConstants.CodevalueJSONinputParams.IS_MANDATORY.getValue());

        return new CodeValue().setCode(code).setLabel(StringUtils.defaultIfEmpty(label, null)).setPosition(position)
                .setDescription(description).setActive(isActive).setMandatory(mandatory);
    }

    public Map<String, Object> update(final JsonCommand command) {

        final Map<String, Object> actualChanges = new LinkedHashMap<>(2);

        final String labelParamName = CodeConstants.CodevalueJSONinputParams.NAME.getValue();
        if (command.isChangeInStringParameterNamed(labelParamName, this.label)) {
            final String newValue = command.stringValueOfParameterNamed(labelParamName);
            actualChanges.put(labelParamName, newValue);
            this.label = StringUtils.defaultIfEmpty(newValue, null);
        }

        final String decriptionParamName = CodeConstants.CodevalueJSONinputParams.DESCRIPTION.getValue();
        if (command.isChangeInStringParameterNamed(decriptionParamName, this.description)) {
            final String newValue = command.stringValueOfParameterNamed(decriptionParamName);
            actualChanges.put(decriptionParamName, newValue);
            this.description = StringUtils.defaultIfEmpty(newValue, null);
        }

        final String positionParamName = CodeConstants.CodevalueJSONinputParams.POSITION.getValue();
        if (command.isChangeInIntegerSansLocaleParameterNamed(positionParamName, this.position)) {
            final Integer newValue = command.integerValueSansLocaleOfParameterNamed(positionParamName);
            actualChanges.put(positionParamName, newValue);
            this.position = newValue;
        }

        final String isActiveParamName = CodeConstants.CodevalueJSONinputParams.IS_ACTIVE.getValue();
        if (command.isChangeInBooleanParameterNamed(isActiveParamName, this.isActive)) {
            final Boolean newValue = command.booleanPrimitiveValueOfParameterNamed(isActiveParamName);
            actualChanges.put(isActiveParamName, newValue);
            this.isActive = newValue;
        }

        return actualChanges;
    }

    public CodeValueData toData() {
        return CodeValueData.instance(getId(), this.label, this.position, this.isActive, this.mandatory);
    }


}
