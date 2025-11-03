package seedu.address.logic.commands;

/**
 * Represents a command related to reminders.
 * This abstract class provides a common command word for all reminder commands.
 */
public abstract class ReminderCommand extends Command {

    public static final String COMMAND_WORD = "reminder";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": The command must be followed by a valid subcommand: "
            + "add, list, or mark.\n" + "Example: " + COMMAND_WORD + " add 1 d/2026-12-25 m/Discuss next project.";
}
