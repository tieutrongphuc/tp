package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MESSAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Date;
import seedu.address.model.reminder.Message;
import seedu.address.model.reminder.Reminder;

/**
 * Adds a reminder to the address book.
 */
public class ReminderAddCommand extends ReminderCommand {

    public static final String SUB_COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD + ":"
            + ": Adds a reminder to the address book.\n"
            + "Format 1 (with index): "
            + COMMAND_WORD + " INDEX "
            + PREFIX_DATE + "DATE "
            + PREFIX_MESSAGE + "MESSAGE\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DATE + "2025-12-31 "
            + PREFIX_MESSAGE + "Discuss project updates\n"
            + "Format 2 (with name): "
            + COMMAND_WORD + " "
            + PREFIX_NAME + "NAME "
            + PREFIX_DATE + "DATE "
            + PREFIX_MESSAGE + "MESSAGE\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_DATE + "2025-12-31 "
            + PREFIX_MESSAGE + "Discuss project updates";

    public static final String MESSAGE_SUCCESS = "New reminder added: %1$s";
    public static final String MESSAGE_DUPLICATE_REMINDER = "This reminder already exists in the address book";
    public static final String MESSAGE_PERSON_NOT_FOUND = "The specified person does not exist in the address book";

    private final Name personName;
    private final Index targetIndex;
    private final Date date;
    private final Message message;
    private final boolean isIndexBased;

    /**
     * Creates a ReminderAddCommand with a person's name.
     *
     * @param personName Name of the person for the reminder
     * @param date Date of the reminder
     * @param message Message of the reminder
     */
    public ReminderAddCommand(Name personName, Date date, Message message) {
        requireNonNull(personName);
        requireNonNull(date);
        requireNonNull(message);

        this.personName = personName;
        this.targetIndex = null;
        this.date = date;
        this.message = message;
        this.isIndexBased = false;
    }

    /**
     * Creates a ReminderAddCommand with a person's index.
     *
     * @param targetIndex Index of the person in the filtered person list
     * @param date Date of the reminder
     * @param message Message of the reminder
     */
    public ReminderAddCommand(Index targetIndex, Date date, Message message) {
        requireNonNull(targetIndex);
        requireNonNull(date);
        requireNonNull(message);

        this.targetIndex = targetIndex;
        this.personName = null;
        this.date = date;
        this.message = message;
        this.isIndexBased = true;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person targetPerson;
        List<Person> lastShownList = model.getFilteredPersonList();

        if (isIndexBased) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            targetPerson = lastShownList.get(targetIndex.getZeroBased());
        } else {
            targetPerson = lastShownList.stream()
                    .filter(person -> person.getName().equals(personName))
                    .findFirst()
                    .orElseThrow(() -> new CommandException(MESSAGE_PERSON_NOT_FOUND));
        }

        Reminder reminderToAdd = new Reminder(targetPerson, date, message);

        if (model.hasReminder(reminderToAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_REMINDER);
        }

        model.addReminder(reminderToAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(reminderToAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ReminderAddCommand)) {
            return false;
        }

        ReminderAddCommand otherCommand = (ReminderAddCommand) other;

        if (isIndexBased != otherCommand.isIndexBased) {
            return false;
        }

        if (isIndexBased) {
            return targetIndex.equals(otherCommand.targetIndex)
                    && date.equals(otherCommand.date)
                    && message.equals(otherCommand.message);
        } else {
            return personName.equals(otherCommand.personName)
                    && date.equals(otherCommand.date)
                    && message.equals(otherCommand.message);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personName", personName)
                .add("targetIndex", targetIndex)
                .add("date", date)
                .add("message", message)
                .add("isIndexBased", isIndexBased)
                .toString();
    }
}
