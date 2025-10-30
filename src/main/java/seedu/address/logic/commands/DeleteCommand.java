package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Reminder;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number/numbers used in the displayed person list.\n"
            + "Parameters: INDEX [INDEX]... (each must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1\n"
            + "Example: " + COMMAND_WORD + " 1 4 6";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Persons:\n%1$s";
    public static final String MESSAGE_DELETE_REMINDERS_SUCCESS = "%1$d associated reminder(s) also deleted.";

    private final List<Index> targetIndexes;

    public DeleteCommand(List<Index> targetIndexes) {
        this.targetIndexes = targetIndexes;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        StringBuilder deletedPersons = new StringBuilder();

        List<Index> sortedIndexes = new ArrayList<>(targetIndexes);
        sortedIndexes.sort(Comparator.comparing(Index::getZeroBased).reversed());
        int totalRemindersDeleted = 0;
        for (Index targetIndex : sortedIndexes) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
            List<Reminder> remindersToDelete = model.getRemindersByPerson(personToDelete);
            totalRemindersDeleted = remindersToDelete.size();
            for (Reminder reminder : remindersToDelete) {
                model.deleteReminder(reminder);
            }

            model.deletePerson(personToDelete);
            deletedPersons.append(Messages.format(personToDelete)).append("\n");
        }
        String resultMessage = String.format(MESSAGE_DELETE_PERSON_SUCCESS, deletedPersons.toString().trim());
        if (totalRemindersDeleted > 0) {
            resultMessage += "\n" + String.format(MESSAGE_DELETE_REMINDERS_SUCCESS, totalRemindersDeleted);
        }
        return new CommandResult(resultMessage);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndexes.equals(otherDeleteCommand.targetIndexes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndexes", targetIndexes)
                .toString();
    }
}
