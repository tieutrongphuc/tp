package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ReminderMarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ReminderMarkCommand object
 */
public class ReminderMarkCommandParser implements Parser<ReminderMarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ReminderMarkCommand
     * and returns a ReminderMarkCommand object for execution.
     * Duplicate indexes are removed
     * @throws ParseException if the user input does not conform the expected format
     */
    public ReminderMarkCommand parse(String args) throws ParseException {
        try {
            List<Index> indexes = ParserUtil.parseIndexes(args);
            List<Index> uniqueIndexes = indexes.stream().distinct().toList();
            return new ReminderMarkCommand(uniqueIndexes);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReminderMarkCommand.MESSAGE_USAGE), pe);
        }
    }

}
