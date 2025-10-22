package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.FindCommand.MESSAGE_MULTIPLE_SEARCH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NoteContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TAG);
        Set<Tag> tagSet = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        String[] noteKeywordsArr = args.split("note/");
        if (noteKeywordsArr.length > 1) {
            throw new ParseException("Note should not contain the following string: 'note/'");
        }
        String noteKeywords = "";
        if (trimmedArgs.contains("note/")) {
            noteKeywords = noteKeywordsArr[0];
        }
        ArrayList<Tag> tagKeywords = new ArrayList<>(tagSet);
        String[] nameKeywords = trimmedArgs.split("\\s+");
        boolean containsMultipleSearch =
                trimmedArgs.split("note/").length > 1 || (tagKeywords.size() - nameKeywords.length == 0);
        containsMultipleSearch = containsMultipleSearch || (!noteKeywords.isBlank() && !tagKeywords.isEmpty());

        if (containsMultipleSearch) {
            throw new ParseException(MESSAGE_MULTIPLE_SEARCH);
        } else if (!tagKeywords.isEmpty()) {
            return new FindCommand(new TagContainsKeywordsPredicate(tagKeywords));
        } else if (!noteKeywords.isEmpty()) {
            return new FindCommand(new NoteContainsKeywordsPredicate(noteKeywords));
        } else {
            return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
        }
    }
}
