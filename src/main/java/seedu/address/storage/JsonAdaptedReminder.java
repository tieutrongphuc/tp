package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Date;
import seedu.address.model.reminder.Message;
import seedu.address.model.reminder.Reminder;

/**
 * Jackson-friendly version of {@link Reminder}.
 */
class JsonAdaptedReminder {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Reminder's %s field is missing!";

    private final JsonAdaptedPerson person;
    private final String date;
    private final String message;
    private final boolean isCompleted;

    /**
     * Constructs a {@code JsonAdaptedReminder} with the given reminder details.
     */
    @JsonCreator
    public JsonAdaptedReminder(@JsonProperty("person") JsonAdaptedPerson person,
                               @JsonProperty("date") String date,
                               @JsonProperty("message") String message,
                               @JsonProperty("isCompleted") boolean isCompleted) {
        this.person = person;
        this.date = date;
        this.message = message;
        this.isCompleted = isCompleted;
    }

    /**
     * Converts a given {@code Reminder} into this class for Jackson use.
     */
    public JsonAdaptedReminder(Reminder source) {
        person = new JsonAdaptedPerson(source.getPerson());
        date = source.getDate().toString();
        message = source.getMessage().toString();
        isCompleted = source.isCompleted();
    }

    /**
     * Converts this Jackson-friendly adapted reminder object into the model's {@code Reminder} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted reminder.
     */
    public Reminder toModelType() throws IllegalValueException {
        if (person == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Person.class.getSimpleName()));
        }
        final Person modelPerson = person.toModelType();

        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Date.class.getSimpleName()));
        }
        if (!Date.isValidDate(date)) {
            throw new IllegalValueException(Date.MESSAGE_CONSTRAINTS);
        }
        final Date modelDate = new Date(date);

        if (message == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Message.class.getSimpleName()));
        }
        if (!Message.isValidMessage(message)) {
            throw new IllegalValueException(Message.MESSAGE_CONSTRAINTS);
        }
        final Message modelMessage = new Message(message);

        return new Reminder(modelPerson, modelDate, modelMessage, isCompleted);
    }

}
