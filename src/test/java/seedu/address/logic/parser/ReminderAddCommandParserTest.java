package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_REMINDER;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_MESSAGE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.MESSAGE_DESC_REMINDER;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_REMINDER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MESSAGE_REMINDER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ReminderAddCommand;
import seedu.address.model.person.Name;
import seedu.address.model.reminder.Date;
import seedu.address.model.reminder.Message;

public class ReminderAddCommandParserTest {
    private final ReminderAddCommandParser parser = new ReminderAddCommandParser();

    @Test
    public void parse_allFieldsPresentByIndex_success() {
        Date expectedDate = new Date(VALID_DATE_REMINDER);
        Message expectedMessage = new Message(VALID_MESSAGE_REMINDER);
        ReminderAddCommand expectedCommand = new ReminderAddCommand(INDEX_FIRST_PERSON, expectedDate, expectedMessage);

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + "1" + DATE_DESC_REMINDER + MESSAGE_DESC_REMINDER,
                expectedCommand);
    }

    @Test
    public void parse_allFieldsPresentByName_success() {
        Name expectedName = new Name(VALID_NAME_AMY);
        Date expectedDate = new Date(VALID_DATE_REMINDER);
        Message expectedMessage = new Message(VALID_MESSAGE_REMINDER);
        ReminderAddCommand expectedCommand = new ReminderAddCommand(expectedName, expectedDate, expectedMessage);

        assertParseSuccess(parser, NAME_DESC_AMY + DATE_DESC_REMINDER + MESSAGE_DESC_REMINDER, expectedCommand);
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReminderAddCommand.MESSAGE_USAGE);

        // missing date prefix
        assertParseFailure(parser, "1" + MESSAGE_DESC_REMINDER, expectedMessage);

        // missing message prefix
        assertParseFailure(parser, "1" + DATE_DESC_REMINDER, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, "1", expectedMessage);
    }

    @Test
    public void parse_personIdentifierMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                "Please specify a person by either index or name prefix 'n/', but not both.\n"
                + ReminderAddCommand.MESSAGE_USAGE);

        // No index and no name
        assertParseFailure(parser, DATE_DESC_REMINDER + MESSAGE_DESC_REMINDER, expectedMessage);
    }

    @Test
    public void parse_bothPersonIdentifiersPresent_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                "Please specify a person by either index or name prefix 'n/', but not both.\n"
                + ReminderAddCommand.MESSAGE_USAGE);

        // Both index and name present
        assertParseFailure(parser, "1" + NAME_DESC_AMY + DATE_DESC_REMINDER + MESSAGE_DESC_REMINDER, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + DATE_DESC_REMINDER + MESSAGE_DESC_REMINDER,
                Name.MESSAGE_CONSTRAINTS);

        // invalid date
        assertParseFailure(parser, "1" + INVALID_DATE_DESC + MESSAGE_DESC_REMINDER, Date.MESSAGE_CONSTRAINTS);

        // invalid message
        assertParseFailure(parser, "1" + DATE_DESC_REMINDER + INVALID_MESSAGE_DESC, Message.MESSAGE_CONSTRAINTS);

        // non-empty preamble with name prefix
        assertParseFailure(parser, "1" + NAME_DESC_AMY + DATE_DESC_REMINDER + MESSAGE_DESC_REMINDER,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                "Please specify a person by either index or name prefix 'n/', but not both.\n"
                + ReminderAddCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        // Duplicate name
        assertParseFailure(parser, NAME_DESC_AMY + NAME_DESC_AMY + DATE_DESC_REMINDER + MESSAGE_DESC_REMINDER,
                "Multiple values specified for the following single-valued field(s): n/");

        // Duplicate date
        assertParseFailure(parser, NAME_DESC_AMY + DATE_DESC_REMINDER + DATE_DESC_REMINDER + MESSAGE_DESC_REMINDER,
                "Multiple values specified for the following single-valued field(s): d/");

        // Duplicate message
        assertParseFailure(parser, NAME_DESC_AMY + DATE_DESC_REMINDER + MESSAGE_DESC_REMINDER + MESSAGE_DESC_REMINDER,
                "Multiple values specified for the following single-valued field(s): m/");
    }
}
