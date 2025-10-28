package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MESSAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ReminderAddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;
import seedu.address.model.reminder.Date;
import seedu.address.model.reminder.Message;

/**
 * Parses input arguments and creates a new ReminderAddCommand object.
 */
public class ReminderAddCommandParser implements Parser<ReminderAddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ReminderAddCommand
     * and returns a ReminderAddCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public ReminderAddCommand parse(String args) throws ParseException {
        // Tokenize for all possible prefixes
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DATE, PREFIX_MESSAGE);

        // All fields except the person identifier (index or name) are mandatory.
        if (!arePrefixesPresent(argMultimap, PREFIX_DATE, PREFIX_MESSAGE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReminderAddCommand.MESSAGE_USAGE));
        }

        // Validate that there is exactly one person identifier (either index in preamble OR n/ prefix)
        boolean isIndexPresent = !argMultimap.getPreamble().isEmpty();
        boolean isNamePresent = argMultimap.getValue(PREFIX_NAME).isPresent();

        // This condition is true if both are present or both are absent
        if (isIndexPresent == isNamePresent) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    "Please specify a person by either index or name prefix 'n/', but not both.\n"
                    + ReminderAddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_DATE, PREFIX_MESSAGE);

        Date date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
        Message message = ParserUtil.parseMessage(argMultimap.getValue(PREFIX_MESSAGE).get());

        if (isIndexPresent) {
            Index index = ParserUtil.parseIndex(argMultimap.getPreamble());
            return new ReminderAddCommand(index, date, message);
        } else {
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
            return new ReminderAddCommand(name, date, message);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
