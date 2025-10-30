package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NoteCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the NoteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the NoteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class NoteCommandParserTest {

    private NoteCommandParser parser = new NoteCommandParser();

    @Test
    public void parse_validArgs_returnsNoteCommand() {
        // valid index 1
        assertParseSuccess(parser, "1", new NoteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validArgsWithLeadingWhitespace_returnsNoteCommand() {
        // leading whitespace before index
        assertParseSuccess(parser, "  1", new NoteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validArgsWithTrailingWhitespace_returnsNoteCommand() {
        // trailing whitespace after index
        assertParseSuccess(parser, "1  ", new NoteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validArgsWithLeadingAndTrailingWhitespace_returnsNoteCommand() {
        // both leading and trailing whitespace
        assertParseSuccess(parser, "  1  ", new NoteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validArgsMultipleWhitespaces_returnsNoteCommand() {
        // multiple whitespaces around index
        assertParseSuccess(parser, "    1    ", new NoteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validArgsDifferentIndexes_returnsNoteCommand() {
        // index 2
        assertParseSuccess(parser, "2", new NoteCommand(INDEX_SECOND_PERSON));
        // index 3
        assertParseSuccess(parser, "3", new NoteCommand(INDEX_THIRD_PERSON));
    }

    @Test
    public void parse_validArgsLargeIndex_returnsNoteCommand() {
        // large index value
        Index largeIndex = Index.fromOneBased(999);
        assertParseSuccess(parser, "999", new NoteCommand(largeIndex));
    }

    @Test
    public void parse_validArgsVeryLargeIndex_returnsNoteCommand() {
        // very large index value
        Index veryLargeIndex = Index.fromOneBased(1000000);
        assertParseSuccess(parser, "1000000", new NoteCommand(veryLargeIndex));
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        // empty string
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_whitespaceOnly_throwsParseException() {
        // whitespace only
        assertParseFailure(parser, "  ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleWhitespacesOnly_throwsParseException() {
        // multiple whitespaces only
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                NoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_tabsAndSpacesOnly_throwsParseException() {
        // tabs and spaces only
        assertParseFailure(parser, "\t  \t", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                NoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsNonNumeric_throwsParseException() {
        // non-numeric input
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsAlphabetic_throwsParseException() {
        // alphabetic string
        assertParseFailure(parser, "abc", String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsAlphanumeric_throwsParseException() {
        // alphanumeric string
        assertParseFailure(parser, "1a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsNegativeNumber_throwsParseException() {
        // negative number
        assertParseFailure(parser, "-1", String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsZero_throwsParseException() {
        // zero index (boundary case - invalid)
        assertParseFailure(parser, "0", String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsDecimalNumber_throwsParseException() {
        // decimal number
        assertParseFailure(parser, "1.5", String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsSpecialCharacters_throwsParseException() {
        // special characters
        assertParseFailure(parser, "@#$", String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsWithSymbols_throwsParseException() {
        // number with symbols
        assertParseFailure(parser, "1!", String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsNumberWithSpace_throwsParseException() {
        // number with space in between (multiple arguments)
        assertParseFailure(parser, "1 2", String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE));
    }
}

