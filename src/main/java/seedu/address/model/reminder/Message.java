package seedu.address.model.reminder;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Reminder's message in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidMessage(String)}
 */
public class Message {

    public static final String MESSAGE_CONSTRAINTS = "Message should not be blank";

    public final String value;

    /**
     * Constructs a {@code Message}.
     *
     * @param message A valid message.
     */
    public Message(String message) {
        requireNonNull(message);
        checkArgument(isValidMessage(message), MESSAGE_CONSTRAINTS);
        value = message;
    }

    /**
     * Returns true if a given string is a valid message.
     * A valid message is any non-blank string.
     */
    public static boolean isValidMessage(String test) {
        return !test.isBlank();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Message)) {
            return false;
        }

        Message otherMessage = (Message) other;
        return value.equals(otherMessage.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
