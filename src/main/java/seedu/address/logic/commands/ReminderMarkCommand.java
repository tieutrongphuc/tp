package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_UPCOMING_REMINDERS;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.reminder.Reminder;

/**
 * Marks a reminder identified using its displayed index as done.
 */
public class ReminderMarkCommand extends ReminderCommand {

    public static final String SUB_COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Marks the reminder identified by the index"
            + "number(s) used in the displayed reminder list as done.\n"
            + "Parameters: INDEX [INDEX]... (each must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 4 6";

    public static final String MESSAGE_MARK_REMINDER_SUCCESS = "Marked Reminders:\n%1$s";

    private final List<Index> targetIndexes;

    public ReminderMarkCommand(List<Index> targetIndexes) {
        this.targetIndexes = targetIndexes;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Reminder> lastShownList = model.getFilteredReminderList();

        StringBuilder markedReminders = new StringBuilder();

        List<Index> sortedIndexes = new ArrayList<>(targetIndexes);
        sortedIndexes.sort(Comparator.comparing(Index::getZeroBased).reversed());

        for (Index targetIndex : sortedIndexes) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_REMINDER_DISPLAYED_INDEX);
            }
            Reminder reminder = lastShownList.get(targetIndex.getZeroBased());
            reminder.markAsCompleted();
            model.updateFilteredReminderList(PREDICATE_SHOW_UPCOMING_REMINDERS);
            markedReminders.append(Messages.format(reminder)).append("\n");
        }

        return new CommandResult(String.format(MESSAGE_MARK_REMINDER_SUCCESS, markedReminders.toString().trim()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ReminderMarkCommand)) {
            return false;
        }

        ReminderMarkCommand otherReminderMarkCommand = (ReminderMarkCommand) other;
        return targetIndexes.equals(otherReminderMarkCommand.targetIndexes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndexes", targetIndexes)
                .toString();
    }
}
