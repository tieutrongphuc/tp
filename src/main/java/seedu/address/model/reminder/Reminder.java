package seedu.address.model.reminder;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Represents a Reminder in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Reminder {

    private final Person person;
    private final Date date;
    private final Message message;
    private final boolean isCompleted;

    /**
     * Every field must be present and not null.
     */
    public Reminder(Person person, Date date, Message message) {
        this(person, date, message, false);
    }

    /**
     * Constructor with completion status
     */
    public Reminder(Person person, Date date, Message message, boolean isCompleted) {
        requireNonNull(person);
        requireNonNull(date);
        requireNonNull(message);
        this.person = person;
        this.date = date;
        this.message = message;
        this.isCompleted = isCompleted;
    }

    /**
     * Returns the person associated with this reminder.
     * @return the person associated with this reminder.
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Returns the due date of this reminder.
     * @return the due date of this reminder.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the message of this reminder.
     * @return the message of this reminder.
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Returns the completion status of this reminder.
     * @return the completion status of this reminder.
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Returns a new Reminder with the same details but marked as completed.
     * @return a new Reminder marked as completed.
     */
    public Reminder markAsCompleted() {
        return new Reminder(this.person, this.date, this.message, true);
    }

    /**
     * Returns a new Reminder with the same details but marked as not completed.
     * @return a new Reminder marked as not completed.
     */
    public Reminder markAsNotCompleted() {
        return new Reminder(this.person, this.date, this.message, false);
    }

    public boolean isUpcoming() {
        return !isCompleted && this.date.isUpcoming();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Reminder)) {
            return false;
        }
        Reminder otherReminder = (Reminder) other;
        return person.equals(otherReminder.person)
                && date.equals(otherReminder.date)
                && message.equals(otherReminder.message)
                && isCompleted == otherReminder.isCompleted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, date, message, isCompleted);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("person", person)
                .add("dueDate", date)
                .add("message", message)
                .toString();
    }
}
