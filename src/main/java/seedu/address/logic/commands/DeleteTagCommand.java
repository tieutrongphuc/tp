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
 * Deletes one or more tags from a person identified by the index number in the displayed person list.
 */
public class DeleteTagCommand extends Command {

    public static final String COMMAND_WORD = "tagdel";
    public static final String MESSAGE_SUCCESS = "Tag(s) deleted: %1$s";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes one or more tags from a person identified by "
            + "the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) TAG (must be alphanumeric)\n"
            + "Example: " + COMMAND_WORD + " 1 t/friend t/colleague";
    public static final String MESSAGE_TAG_NOT_FOUND = "One or more specified tags do not exist for this person: %1$s";

    private final Index index;
    private final Set<Tag> tagsToDelete;

    /**
     * Creates a DeleteTagCommand to delete the specified {@code Tag}(s) from the person at the given {@code Index}.
     *
     * @param index Index of the person in the filtered person list to delete tags from
     * @param tagsToDelete Set of tags to delete
     */
    public DeleteTagCommand(Index index, Set<Tag> tagsToDelete) {
        requireNonNull(index);
        requireNonNull(tagsToDelete);
        this.index = index;
        this.tagsToDelete = tagsToDelete;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Set<Tag> currentTags = new HashSet<>(personToEdit.getTags());

        // Check if all tags to delete exist
        Set<Tag> nonExistentTags = new HashSet<>();
        for (Tag tag : tagsToDelete) {
            if (!currentTags.contains(tag)) {
                nonExistentTags.add(tag);
            }
        }

        if (!nonExistentTags.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_TAG_NOT_FOUND, nonExistentTags));
        }

        // Remove the tags
        Set<Tag> updatedTags = new HashSet<>(currentTags);
        updatedTags.removeAll(tagsToDelete);

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
        if (!(other instanceof DeleteTagCommand)) {
            return false;
        }

        DeleteTagCommand otherCommand = (DeleteTagCommand) other;
        return index.equals(otherCommand.index)
                && tagsToDelete.equals(otherCommand.tagsToDelete);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("tagsToDelete", tagsToDelete)
                .toString();
    }
}

