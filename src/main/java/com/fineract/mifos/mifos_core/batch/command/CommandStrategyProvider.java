package com.fineract.mifos.mifos_core.batch.command;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.HttpMethod.*;

/**
 * Provides an appropriate CommandStrategy using the 'method' and 'resourceUrl'. CommandStrategy bean is created using
 * Spring Application Context.
 *
 * @author Peter Gichuki
 *
 *
 */
@Component
public class CommandStrategyProvider {
    private final ApplicationContext applicationContext;
    private static final Map<CommandContext, String> commandStrategies = new ConcurrentHashMap<>();

    /**
     * Regex pattern for specifying any number of query params or not specific any query param
     */
    private static final String OPTIONAL_QUERY_PARAM_REGEX = "(\\?(\\w+(?:\\=[\\w,]+|&)+)+)?";

    /**
     * Regex pattern for specifying query params
     */
    private static final String MANDATORY_QUERY_PARAM_REGEX = "(\\?(\\w+(?:\\=[\\w\\-,]+|&)+)+)";

    /**
     * Regex pattern for specifying any query param that has key = 'command' or not specific anything.
     */
    private static final String OPTIONAL_COMMAND_PARAM_REGEX = "(\\?command=[\\w]+)?";

    /**
     * Regex pattern for specifying a mandatory query param that has key = 'command'.
     */
    private static final String MANDATORY_COMMAND_PARAM_REGEX = "\\?command=[\\w]+";

    /**
     * Regex pattern for specifying a UUID param.
     */
    private static final String UUID_PARAM_REGEX = "[\\w\\d-]+";

    /**
     * Regex pattern for specifying a param that's should be a number.
     */
    private static final String NUMBER_REGEX = "\\d+";

    /**
     * Regex pattern for specifying a param that contains case in-sensitive alphanumeric characters with underscores.
     */
    private static final String ALPHANUMBERIC_WITH_UNDERSCORE_REGEX = "[a-zA-Z0-9_]*";

    /**
     * Constructs a CommandStrategyProvider with argument of ApplicationContext type. It also initializes
     * commandStrategies using init() function by filling it with available CommandStrategies in
     * {@link }.
     *
     * @param applicationContext
     */
    public CommandStrategyProvider(final ApplicationContext applicationContext) {

        // calls init() function of this class.
        init();

        this.applicationContext = applicationContext;
    }

    /**
     * Returns an appropriate commandStrategy after determining it using the CommandContext of the request. If no such
     * Strategy is found then a default strategy is returned.
     *
     * @param commandContext
     * @return CommandStrategy
     *
     */
    public CommandStrategy getCommandStrategy(final CommandContext commandContext) {
        if (isResourceVersioned(commandContext)) {
            return internalGetCommandStrategy(commandContext);
        } else {
            // for backward compatibility, support non-versioned relative paths too
            CommandContext alteredCommandContext = CommandContext.resource("v1/" + commandContext.getResource())
                    .method(commandContext.getMethod()).build();
            return internalGetCommandStrategy(alteredCommandContext);
        }
    }

    private CommandStrategy internalGetCommandStrategy(CommandContext commandContext) {
        if (commandStrategies.containsKey(commandContext)) {
            return (CommandStrategy) this.applicationContext.getBean(commandStrategies.get(commandContext));
        }

        for (Map.Entry<CommandContext, String> entry : commandStrategies.entrySet()) {
            if (commandContext.matcher(entry.getKey())) {
                return (CommandStrategy) applicationContext.getBean(commandStrategies.get(entry.getKey()));
            }
        }
        return new UnknownCommandStrategy();
    }

    /**
     * Contains various available command strategies in {@link }. Any new
     * command Strategy will have to be added within this function in order to initiate it within the constructor.
     */
    private static void init() {
        commandStrategies.put(CommandContext.resource("v1\\/clients").method(String.valueOf(POST)).build(), "createClientCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/clients\\/" + NUMBER_REGEX).method(String.valueOf(PUT)).build(), "updateClientCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/loans").method(String.valueOf(POST)).build(), "applyLoanCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/loans\\/" + NUMBER_REGEX + OPTIONAL_QUERY_PARAM_REGEX).method(String.valueOf(GET)).build(),
                "getLoanByIdCommandStrategy");
        commandStrategies.put(
                CommandContext.resource("v1\\/loans\\/external-id\\/" + UUID_PARAM_REGEX + OPTIONAL_QUERY_PARAM_REGEX).method(String.valueOf(GET)).build(),
                "getLoanByExternalIdCommandStrategy");
        commandStrategies.put(
                CommandContext.resource("v1\\/savingsaccounts\\/" + NUMBER_REGEX + OPTIONAL_QUERY_PARAM_REGEX).method(String.valueOf(GET)).build(),
                "getSavingsAccountByIdCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/savingsaccounts").method(String.valueOf(POST)).build(), "applySavingsCommandStrategy");
        commandStrategies.put(CommandContext
                        .resource("v1\\/savingsaccounts\\/" + NUMBER_REGEX + "\\/transactions" + OPTIONAL_COMMAND_PARAM_REGEX).method(String.valueOf(POST)).build(),
                "savingsAccountTransactionCommandStrategy");
        commandStrategies.put(CommandContext
                .resource("v1\\/savingsaccounts\\/" + NUMBER_REGEX + "\\/transactions\\/" + NUMBER_REGEX + OPTIONAL_COMMAND_PARAM_REGEX)
                .method(String.valueOf(POST)).build(), "savingsAccountAdjustTransactionCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/loans\\/" + NUMBER_REGEX + "\\/charges").method(String.valueOf(POST)).build(),
                "createChargeCommandStrategy");
        commandStrategies.put(
                CommandContext.resource("v1\\/loans\\/external-id\\/" + UUID_PARAM_REGEX + "\\/charges" + OPTIONAL_COMMAND_PARAM_REGEX + "")
                        .method(String.valueOf(POST)).build(),
                "createChargeByLoanExternalIdCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/loans\\/" + NUMBER_REGEX + "\\/charges").method(String.valueOf(GET)).build(),
                "collectChargesCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/loans\\/external-id\\/" + UUID_PARAM_REGEX + "\\/charges").method(String.valueOf(GET)).build(),
                "collectChargesByLoanExternalIdCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/loans\\/" + NUMBER_REGEX + "\\/charges\\/" + NUMBER_REGEX).method(String.valueOf(GET)).build(),
                "getChargeByIdCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/loans\\/external-id\\/" + UUID_PARAM_REGEX + "\\/charges\\/external-id\\/"
                + UUID_PARAM_REGEX + OPTIONAL_QUERY_PARAM_REGEX).method(String.valueOf(GET)).build(), "getChargeByChargeExternalIdCommandStrategy");
        commandStrategies.put(
                CommandContext.resource("v1\\/loans\\/" + NUMBER_REGEX + "\\/charges\\/" + NUMBER_REGEX + MANDATORY_COMMAND_PARAM_REGEX)
                        .method(String.valueOf(POST)).build(),
                "adjustChargeCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/loans\\/external-id\\/" + UUID_PARAM_REGEX + "\\/charges\\/external-id\\/"
                + UUID_PARAM_REGEX + MANDATORY_COMMAND_PARAM_REGEX).method(String.valueOf(POST)).build(), "adjustChargeByChargeExternalIdCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/loans\\/" + NUMBER_REGEX + "\\/transactions" + MANDATORY_COMMAND_PARAM_REGEX)
                .method(String.valueOf(POST)).build(), "createTransactionLoanCommandStrategy");
        commandStrategies.put(CommandContext
                .resource("v1\\/loans\\/external-id\\/" + UUID_PARAM_REGEX + "\\/transactions" + MANDATORY_COMMAND_PARAM_REGEX).method(String.valueOf(POST))
                .build(), "createTransactionByLoanExternalIdCommandStrategy");
        commandStrategies.put(
                CommandContext.resource("v1\\/loans\\/" + NUMBER_REGEX + "\\/transactions\\/" + NUMBER_REGEX + OPTIONAL_COMMAND_PARAM_REGEX)
                        .method(String.valueOf(POST)).build(),
                "adjustLoanTransactionCommandStrategy");
        commandStrategies.put(
                CommandContext.resource("v1\\/loans\\/external-id\\/" + UUID_PARAM_REGEX + "\\/transactions\\/external-id\\/"
                        + UUID_PARAM_REGEX + OPTIONAL_COMMAND_PARAM_REGEX).method(String.valueOf(POST)).build(),
                "adjustLoanTransactionByExternalIdCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/clients\\/" + NUMBER_REGEX + "\\?command=activate").method(String.valueOf(POST)).build(),
                "activateClientCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/loans\\/" + NUMBER_REGEX + "\\?command=approve").method(String.valueOf(POST)).build(),
                "approveLoanCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/loans\\/" + NUMBER_REGEX + "\\?command=disburse").method(String.valueOf(POST)).build(),
                "disburseLoanCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/loans\\/external-id\\/" + UUID_PARAM_REGEX + MANDATORY_COMMAND_PARAM_REGEX)
                .method(String.valueOf(POST)).build(), "loanStateTransistionsByExternalIdCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/rescheduleloans").method(String.valueOf(POST)).build(),
                "createLoanRescheduleRequestCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/rescheduleloans\\/" + NUMBER_REGEX + "\\?command=approve").method(String.valueOf(POST)).build(),
                "approveLoanRescheduleCommandStrategy");
        commandStrategies.put(
                CommandContext.resource("v1\\/loans\\/" + NUMBER_REGEX + "\\/transactions\\/" + NUMBER_REGEX).method(String.valueOf(GET)).build(),
                "getLoanTransactionByIdCommandStrategy");
        commandStrategies.put(
                CommandContext.resource("v1\\/loans\\/external-id\\/" + UUID_PARAM_REGEX + "\\/transactions\\/external-id\\/"
                        + UUID_PARAM_REGEX + OPTIONAL_QUERY_PARAM_REGEX).method(String.valueOf(GET)).build(),
                "getLoanTransactionByExternalIdCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/datatables\\/" + ALPHANUMBERIC_WITH_UNDERSCORE_REGEX + "\\/" + NUMBER_REGEX)
                .method(String.valueOf(POST)).build(), "createDatatableEntryCommandStrategy");
        commandStrategies.put(CommandContext
                .resource("v1\\/datatables\\/" + ALPHANUMBERIC_WITH_UNDERSCORE_REGEX + "\\/" + NUMBER_REGEX + "\\/" + NUMBER_REGEX)
                .method(String.valueOf(PUT)).build(), "updateDatatableEntryOneToManyCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/datatables\\/" + ALPHANUMBERIC_WITH_UNDERSCORE_REGEX + "\\/" + NUMBER_REGEX)
                .method(String.valueOf(PUT)).build(), "updateDatatableEntryOneToOneCommandStrategy");
        commandStrategies.put(CommandContext
                .resource("v1\\/datatables\\/" + ALPHANUMBERIC_WITH_UNDERSCORE_REGEX + "\\/" + NUMBER_REGEX + "\\/" + NUMBER_REGEX)
                .method(String.valueOf(DELETE)).build(), "deleteDatatableEntryOneToManyCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/datatables\\/" + ALPHANUMBERIC_WITH_UNDERSCORE_REGEX + "\\/" + NUMBER_REGEX)
                .method(String.valueOf(DELETE)).build(), "deleteDatatableEntryOneToOneCommandStrategy");
        commandStrategies.put(CommandContext
                .resource("v1\\/datatables\\/" + ALPHANUMBERIC_WITH_UNDERSCORE_REGEX + "\\/" + NUMBER_REGEX + OPTIONAL_QUERY_PARAM_REGEX)
                .method(String.valueOf(GET)).build(), "getDatatableEntryByAppTableIdCommandStrategy");
        commandStrategies.put(
                CommandContext.resource("v1\\/datatables\\/" + ALPHANUMBERIC_WITH_UNDERSCORE_REGEX + "\\/" + NUMBER_REGEX + "\\/"
                        + NUMBER_REGEX + OPTIONAL_QUERY_PARAM_REGEX).method(String.valueOf(GET)).build(),
                "getDatatableEntryByAppTableIdAndDataTableIdCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/loans\\/" + NUMBER_REGEX + OPTIONAL_COMMAND_PARAM_REGEX).method(String.valueOf(PUT)).build(),
                "modifyLoanApplicationCommandStrategy");
        commandStrategies.put(CommandContext.resource("v1\\/loans\\/external-id\\/" + UUID_PARAM_REGEX + OPTIONAL_COMMAND_PARAM_REGEX)
                .method(String.valueOf(PUT)).build(), "modifyLoanApplicationByExternalIdCommandStrategy");
        commandStrategies.put(CommandContext
                .resource("v1\\/datatables\\/" + ALPHANUMBERIC_WITH_UNDERSCORE_REGEX + "\\/query" + MANDATORY_QUERY_PARAM_REGEX).method(String.valueOf(GET))
                .build(), "getDatatableEntryByQueryCommandStrategy");
    }

}
