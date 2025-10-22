package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_RESEARCH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_TITLE;
import static seedu.address.logic.parser.ParserUtil.parseTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagCommand object
 */
public class TagCommandParser implements Parser<TagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the TagCommand
     * and returns an TagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public TagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE), pe);
        }

        List<String> defaultTags = argMultimap.getAllValues(PREFIX_TAG);
        List<String> researchTags = argMultimap.getAllValues(PREFIX_TAG_RESEARCH);
        List<String> jobTitleTags = argMultimap.getAllValues(PREFIX_TAG_TITLE);

        Set<Tag> newTag = parseTags(defaultTags, "default");
        newTag.addAll(parseTags(researchTags, "research"));
        newTag.addAll(parseTags(jobTitleTags, "title"));

        return new TagCommand(index, newTag);
    }
}
