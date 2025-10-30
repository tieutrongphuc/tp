package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.reminder.Reminder;

/**
 * A utility class containing a list of {@code Reminder} objects to be used in tests.
 */
public class TypicalReminders {

    public static final Reminder ALICE_REMINDER = new ReminderBuilder()
            .withPerson(TypicalPersons.ALICE)
            .withDate("2025-12-01 10:00")
            .withMessage("Follow up on project")
            .build();

    public static final Reminder BENSON_REMINDER = new ReminderBuilder()
            .withPerson(TypicalPersons.BENSON)
            .withDate("2025-11-15 14:30")
            .withMessage("Coffee meeting")
            .build();

    public static final Reminder CARL_REMINDER = new ReminderBuilder()
            .withPerson(TypicalPersons.CARL)
            .withDate("2025-12-15 09:00")
            .withMessage("Review documents")
            .build();

    private TypicalReminders() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with the persons who have reminders and their reminders.
     */
    public static AddressBook getTypicalAddressBookWithReminders() {
        AddressBook ab = new AddressBook();
        // Only add the persons that have reminders
        ab.addPerson(TypicalPersons.ALICE);
        ab.addPerson(TypicalPersons.BENSON);
        ab.addPerson(TypicalPersons.CARL);

        for (Reminder reminder : getTypicalReminders()) {
            ab.addReminder(reminder);
        }
        return ab;
    }

    public static List<Reminder> getTypicalReminders() {
        return new ArrayList<>(Arrays.asList(ALICE_REMINDER, BENSON_REMINDER, CARL_REMINDER));
    }
}
