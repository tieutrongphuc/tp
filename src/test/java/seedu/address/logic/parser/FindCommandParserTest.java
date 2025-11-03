package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_FIELD_EMPTY;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NoteContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;
import seedu.address.model.tag.Tag;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);

        // find tag command
        Tag testTag1 = new Tag("test1");
        Tag testTag2 = new Tag("test2", "title");
        FindCommand expectedFindTagCommand =
                new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList(testTag1, testTag2)));
        assertParseSuccess(parser, "t/test1 jtt/test2", expectedFindTagCommand);

        // find note command
        FindCommand expectedFindNoteCommand =
                new FindCommand(new NoteContainsKeywordsPredicate("hello"));
        assertParseSuccess(parser, "note/hello", expectedFindNoteCommand);
    }

    @Test
    public void parse_invalidArgs() {
        // no blank field
        assertParseFailure(parser, "t/hi t/", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "note/ ", "Note field : " + MESSAGE_FIELD_EMPTY);

        // multiple search fields
        assertParseFailure(parser, "Alice t/test1", FindCommand.MESSAGE_MULTIPLE_SEARCH);
        assertParseFailure(parser, "t/test1 note/yapa", FindCommand.MESSAGE_MULTIPLE_SEARCH);
        assertParseFailure(parser, "Alice note/yapa", FindCommand.MESSAGE_MULTIPLE_SEARCH);

        // find note command
        String doubleNoteFailure = "Note should not contain the following string: 'note/'";
        assertParseFailure(parser, "note/take note/details of friend", doubleNoteFailure);
    }



}
