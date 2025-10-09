package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import seedu.address.commons.core.index.Index;
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
    public static final String MESSAGE_SUCCESS = "New tag added: %1$s";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a tag to a person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) TAG (must be a single word)\n"
            + "Example: " + COMMAND_WORD + "1 /t friend /t cs2103t";

    private final Index index;
    private final Set<Tag> tag;


    /**
     * Creates a TagCommand to add the specified {@code Tag}(s) to the person at the given {@code Index}.
     *
     * @param index Index of the person in the filtered person list to add tags to
     * @param tag Set of tags to add
     */
    public TagCommand(Index index, Set<Tag> tag) {
        this.index = index;
        this.tag = tag;
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
        updatedTags.addAll(tag);

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
}
