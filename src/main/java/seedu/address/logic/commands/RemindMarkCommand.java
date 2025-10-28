package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.reminder.Reminder;

/**
 * Marks a reminder as completed.
 */
public class RemindMarkCommand extends ReminderCommand {

    public static final String SUB_COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Marks a reminder as completed.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Reminder marked as completed: %1$s";
    public static final String MESSAGE_ALREADY_COMPLETED = "This reminder is already completed.";

    private final Index index;

    public RemindMarkCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Reminder> lastShownList = model.getFilteredReminderList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_REMINDER_DISPLAYED_INDEX);
        }

        Reminder reminder = lastShownList.get(index.getZeroBased());

        if (reminder.isCompleted()) {
            throw new CommandException(MESSAGE_ALREADY_COMPLETED);
        }

        Reminder completedReminder = reminder.markAsCompleted();
        model.setReminder(reminder, completedReminder);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(completedReminder)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof RemindMarkCommand)) {
            return false;
        }

        RemindMarkCommand otherCommand = (RemindMarkCommand) other;
        return index.equals(otherCommand.index);
    }
}
