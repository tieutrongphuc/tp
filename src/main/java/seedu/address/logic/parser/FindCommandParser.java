package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_FIELD_EMPTY;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.FindCommand.MESSAGE_MULTIPLE_SEARCH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_RESEARCH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_TITLE;

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

        if (args.contains("t/") && args.contains("note/")) {
            throw new ParseException(MESSAGE_MULTIPLE_SEARCH);
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(" " + args, PREFIX_TAG, PREFIX_TAG_RESEARCH, PREFIX_TAG_TITLE);

        Set<Tag> tagSet = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        tagSet.addAll(ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG_RESEARCH), "research"));
        tagSet.addAll(ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG_TITLE), "title"));

        String[] noteKeywordsArr = trimmedArgs.split("note/");
        if (noteKeywordsArr.length > 2) {
            throw new ParseException("Note should not contain the following string: 'note/'");
        } else if (noteKeywordsArr.length < 2 && args.contains("note/")) {
            throw new ParseException("Note field : " + MESSAGE_FIELD_EMPTY);
        }

        String noteKeywords = "";
        if (trimmedArgs.contains("note/")) {
            noteKeywords = noteKeywordsArr[1];
        }
        ArrayList<Tag> tagKeywords = new ArrayList<>(tagSet);
        String[] nameKeywords = trimmedArgs.split("\\s+");
        boolean containsTagAndName = (!tagKeywords.isEmpty() && nameKeywords.length > tagKeywords.size());
        boolean containsNoteAndName = (noteKeywords.length() > 1 && !noteKeywordsArr[0].isBlank());

        boolean containsMultipleSearch = containsTagAndName || containsNoteAndName;

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
