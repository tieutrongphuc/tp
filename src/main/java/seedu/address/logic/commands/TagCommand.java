package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Adds one or more tags to a person identified by the index number in the displayed person list.
 * Creates a new Person instance with the updated set of tags, preserving immutability.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";
    public static final String MESSAGE_SUCCESS = "Tags updated: %1$s";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds one or more tag to a person identified by "
            + "the index number used in the displayed person list.\n"
            + "Use 'rtt' to tag by research topics and 'jtt' to tag by job title.\n"
            + "Parameters: INDEX (must be a positive integer) TAG \n"
            + "Example: " + COMMAND_WORD + " 1 t/friend rtt/Machine Learning jtt/Lead Researcher";

    private final Index index;
    private final Set<Tag> tags;

    /**
     * Creates a TagCommand to add the specified {@code Tag}(s) to the person at the given {@code Index}.
     *
     * @param index Index of the person in the filtered person list to add tags to
     * @param tags Set of tags to add
     */
    public TagCommand(Index index, Set<Tag> tags) {
        this.index = index;
        this.tags = tags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        updatedTags.addAll(tags);

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                updatedTags
        );

        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(editedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagCommand)) {
            return false;
        }

        TagCommand otherTagCommand = (TagCommand) other;
        return index.equals(otherTagCommand.index)
                && tags.equals(otherTagCommand.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("tags", tags)
                .toString();
    }
}
