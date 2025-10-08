package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Set;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
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
                ArgumentTokenizer.tokenize(args,PREFIX_TAG);
        Set<Tag> tagSet = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        ArrayList<Tag> tagKeywords = new ArrayList<>(tagSet);
        if (!tagKeywords.isEmpty()) {
            return new FindCommand(new TagContainsKeywordsPredicate(tagKeywords));
        } else {
            String[] nameKeywords = trimmedArgs.split("\\s+");
            return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
        }
    }
}
