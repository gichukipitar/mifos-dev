package com.fineract.mifos.mifos_core.infrastructure.codes.entity;

import com.fineract.mifos.mifos_core.infrastructure.codes.exception.SystemDefinedCodeCannotBeChangedException;
import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.domain.AbstractPersistableCustom;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "m_code", uniqueConstraints = { @UniqueConstraint(columnNames = { "code_name" }, name = "code_name") })
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Code extends AbstractPersistableCustom {
    @Column(name = "code_name", length = 100)
    private String name;

    @Column(name = "is_system_defined")
    private boolean systemDefined;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "code", orphanRemoval = true)
    private Set<CodeValue> values;

    public static Code fromJson(final JsonCommand command) {
        final String name = command.stringValueOfParameterNamed("name");
        return new Code().setName(name);
    }

    public static Code createNew(final String name) {
        return new Code().setName(name);
    }

    public Map<String, Object> update(final JsonCommand command) {

        if (this.systemDefined) {
            throw new SystemDefinedCodeCannotBeChangedException();
        }

        final Map<String, Object> actualChanges = new LinkedHashMap<>(1);

        final String firstnameParamName = "name";
        if (command.isChangeInStringParameterNamed(firstnameParamName, this.name)) {
            final String newValue = command.stringValueOfParameterNamed(firstnameParamName);
            actualChanges.put(firstnameParamName, newValue);
            this.name = StringUtils.defaultIfEmpty(newValue, null);
        }

        return actualChanges;
    }

    public boolean remove(final CodeValue codeValueToDelete) {
        return this.values.remove(codeValueToDelete);
    }

}
