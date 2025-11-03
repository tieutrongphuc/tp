package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;


/**
 * View a note of an existing person in the address book.
 */
public class ViewNoteCommand extends Command {

    public static final String COMMAND_WORD = "viewNote";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Views the note of the person identified "
            + "by the index number used in the displayed person list. \n"
            + "Parameters: INDEX (must be a positive integer) \n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Viewing note of Person: %1$s";

    private final Index index;

    public ViewNoteCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person targetPerson = lastShownList.get(index.getZeroBased());
        Note note = targetPerson.getNote();
        String noteContent = note.value.isEmpty() ? "(No note)" : note.value;
        String message = String.format("Note for %1$s: %2$s", targetPerson.getName(), noteContent);

        return new CommandResult(message);
    }
}
