package com.fineract.mifos.mifos_core.infrastructure.codes;

import java.util.HashSet;
import java.util.Set;

public class CodeConstants {
    /***
     * Enum of all parameters passed in while creating/updating a code and code value
     ***/

    public enum CodevalueJSONinputParams {

        CODEVALUE_ID("id"), NAME("name"), POSITION("position"), DESCRIPTION("description"), IS_ACTIVE("isActive"), IS_MANDATORY(
                "isMandatory");

        private final String value;

        CodevalueJSONinputParams(final String value) {
            this.value = value;
        }

        private static final Set<String> values = new HashSet<>();

        static {
            for (final CodevalueJSONinputParams type : CodevalueJSONinputParams.values()) {
                values.add(type.value);
            }
        }

        public static Set<String> getAllValues() {
            return values;
        }

        @Override
        public String toString() {
            return name().toString().replaceAll("_", " ");
        }

        public String getValue() {
            return this.value;
        }
    }

}
