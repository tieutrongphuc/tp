package seedu.address.testutil;

import seedu.address.model.person.Person;
import seedu.address.model.reminder.Date;
import seedu.address.model.reminder.Message;
import seedu.address.model.reminder.Reminder;

/**
 * A utility class to help with building Reminder objects.
 */
public class ReminderBuilder {

    public static final String DEFAULT_DATE = "2025-12-01 10:00";
    public static final String DEFAULT_MESSAGE = "Default reminder message";

    private Person person;
    private Date date;
    private Message message;
    private boolean isCompleted;

    /**
     * Creates a {@code ReminderBuilder} with the default details.
     */
    public ReminderBuilder() {
        person = new PersonBuilder().build();
        date = new Date(DEFAULT_DATE);
        message = new Message(DEFAULT_MESSAGE);
        isCompleted = false;
    }

    /**
     * Initializes the ReminderBuilder with the data of {@code reminderToCopy}.
     */
    public ReminderBuilder(Reminder reminderToCopy) {
        person = reminderToCopy.getPerson();
        date = reminderToCopy.getDate();
        message = reminderToCopy.getMessage();
        isCompleted = reminderToCopy.isCompleted();
    }

    /**
     * Sets the {@code Person} of the {@code Reminder} that we are building.
     */
    public ReminderBuilder withPerson(Person person) {
        this.person = person;
        return this;
    }

    /**
     * Sets the {@code Date} of the {@code Reminder} that we are building.
     */
    public ReminderBuilder withDate(String date) {
        this.date = new Date(date);
        return this;
    }

    /**
     * Sets the {@code Message} of the {@code Reminder} that we are building.
     */
    public ReminderBuilder withMessage(String message) {
        this.message = new Message(message);
        return this;
    }

    /**
     * Sets the {@code isCompleted} status of the {@code Reminder} that we are building.
     */
    public ReminderBuilder withCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
        return this;
    }

    public Reminder build() {
        return new Reminder(person, date, message, isCompleted);
    }
}
