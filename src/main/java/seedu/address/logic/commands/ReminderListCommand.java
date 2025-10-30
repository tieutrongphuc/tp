package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_UPCOMING_REMINDERS;

import seedu.address.model.Model;

/**
 * Lists all reminders in the address book to the user.
 */
public class ReminderListCommand extends ReminderCommand {

    public static final String SUB_COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all reminders";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredReminderList(PREDICATE_SHOW_UPCOMING_REMINDERS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
