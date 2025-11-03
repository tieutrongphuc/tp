package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ReminderAddCommand;
import seedu.address.logic.commands.ReminderCommand;
import seedu.address.logic.commands.ReminderListCommand;
import seedu.address.logic.commands.ReminderMarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.reminder.Date;
import seedu.address.model.reminder.Message;

public class ReminderCommandParserTest {

    private final ReminderCommandParser parser = new ReminderCommandParser();

    @Test
    public void parse_addCommand_returnsReminderAddCommand() throws Exception {
        String dateStr = "2026-12-25";
        String messageStr = "Christmas party";
        Date date = new Date(dateStr);
        Message message = new Message(messageStr);
        ReminderAddCommand command = (ReminderAddCommand) parser.parse(
                ReminderAddCommand.SUB_COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased()
                + " d/" + dateStr + " m/" + messageStr);
        assertEquals(new ReminderAddCommand(INDEX_FIRST_PERSON, date, message), command);
    }

    @Test
    public void parse_listCommand_returnsReminderListCommand() throws Exception {
        assertTrue(parser.parse(ReminderListCommand.SUB_COMMAND_WORD) instanceof ReminderListCommand);
        assertTrue(parser.parse(ReminderListCommand.SUB_COMMAND_WORD + " 3") instanceof ReminderListCommand);
    }

    @Test
    public void parse_markCommand_returnsReminderMarkCommand() throws Exception {
        ReminderMarkCommand command = (ReminderMarkCommand) parser.parse(
                ReminderMarkCommand.SUB_COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new ReminderMarkCommand(List.of(INDEX_FIRST_PERSON)), command);
    }

    @Test
    public void parse_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ReminderCommand.MESSAGE_USAGE), () -> parser.parse(""));
    }

    @Test
    public void parse_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parse("unknownCommand"));
    }
}
