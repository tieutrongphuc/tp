package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes note of an existing person using it's displayed index from the address book.
 */
public class DeleteNoteCommand extends Command {
    public static final String COMMAND_WORD = "deleteNote";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person's note identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_NOTE_SUCCESS = "Deleted note of Person: %1$s";
    public static final String MESSAGE_DELETE_NOTE_FAIL = "No note available to delete of Person: %1$s";

    private final Index targetIndex;

    public DeleteNoteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person targetPerson = lastShownList.get(targetIndex.getZeroBased());
        if (!targetPerson.deleteNote()) {
            return new CommandResult(String.format(MESSAGE_DELETE_NOTE_FAIL, Messages.format(targetPerson)));
        }
        return new CommandResult(String.format(MESSAGE_DELETE_NOTE_SUCCESS, Messages.format(targetPerson)));
    }
}
