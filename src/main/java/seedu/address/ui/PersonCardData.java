package seedu.address.ui;

import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Reminder;

/**
 * Encapsulates display data for a PersonCard, including computed reminder information.
 * Assumes the reminder list provided contains only upcoming reminders.
 */
public class PersonCardData {
    private final Person person;
    private final int displayIndex;
    private final int upcomingReminderCount;
    private final String nextReminderText;

    /**
     * Creates a PersonCardData with computed reminder information.
     *
     * @param person The person to display.
     * @param displayIndex The index to display (1-based).
     * @param upcomingReminders The observable list of upcoming reminders (already filtered by date).
     */
    public PersonCardData(Person person, int displayIndex, ObservableList<Reminder> upcomingReminders) {
        this.person = person;
        this.displayIndex = displayIndex;

        long count = upcomingReminders.stream()
                .filter(r -> r.getPerson().equals(person))
                .count();

        this.upcomingReminderCount = (int) count;

        this.nextReminderText = upcomingReminders.stream()
                .filter(r -> r.getPerson().equals(person))
                .findFirst()
                .map(r -> r.getMessage() + " (" + r.getDate() + ")")
                .orElse("");
    }

    public Person getPerson() {
        return person;
    }

    public int getDisplayIndex() {
        return displayIndex;
    }

    public int getUpcomingReminderCount() {
        return upcomingReminderCount;
    }

    public String getNextReminderText() {
        return nextReminderText;
    }

    public boolean hasUpcomingReminders() {
        return upcomingReminderCount > 0;
    }
}
