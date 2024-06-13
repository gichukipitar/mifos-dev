package com.fineract.mifos.mifos_core.commands.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum CommandProcessingResultType {
    INVALID(0, "commandProcessingResultType.invalid"), //
    PROCESSED(1, "commandProcessingResultType.processed"), //
    AWAITING_APPROVAL(2, "commandProcessingResultType.awaiting.approval"), //
    REJECTED(3, "commandProcessingResultType.rejected"), //
    UNDER_PROCESSING(4, "commandProcessingResultType.underProcessing"), //
    ERROR(5, "commandProcessingResultType.error");
    private static final Map<Integer, CommandProcessingResultType> BY_ID = Arrays.stream(values())
            .collect(Collectors.toMap(CommandProcessingResultType::getValue, v -> v));

    private final Integer value;
    private final String code;

    public static CommandProcessingResultType fromInt(final Integer value) {
        CommandProcessingResultType transactionType = BY_ID.get(value);
        return transactionType == null ? INVALID : transactionType;
    }
}
