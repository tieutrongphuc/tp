package seedu.address.model.reminder;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.reminder.exceptions.DuplicateReminderException;
import seedu.address.model.reminder.exceptions.ReminderNotFoundException;

/**
 * A list of reminders that enforces uniqueness between its elements and does not allow nulls.
 * A reminder is considered unique by comparing using {@code Reminder#equals(Object)}. As such, adding, updating,
 * and removing of reminders uses Reminder#equals(Object) for equality checks.
 *
 * The list is automatically sorted by date in ascending order.
 *
 * Supports a minimal set of list operations.
 *
 * @see Reminder#equals(Object)
 */
public class UniqueReminderList implements Iterable<Reminder> {

    // Comparator for sorting reminders by date in ascending order
    private static final Comparator<Reminder> DATE_COMPARATOR =
            Comparator.comparing(Reminder::getDate);

    private final ObservableList<Reminder> internalList = FXCollections.observableArrayList();
    private final ObservableList<Reminder> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);


    /**
     * Returns true if the list contains an equivalent reminder as the given argument.
     */
    public boolean contains(Reminder toCheck) {
        requireNonNull(toCheck);
        // Reminder#equals(Object) is used for checking uniqueness.
        return internalList.stream().anyMatch(toCheck::equals);
    }

    /**
     * Adds a reminder to the list.
     * The reminder must not already exist in the list.
     * The list will be automatically sorted by date after addition.
     */
    public void add(Reminder toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateReminderException();
        }
        internalList.add(toAdd);
        sortReminders();
    }

    /**
     * Replaces the reminder {@code target} in the list with {@code editedReminder}.
     * {@code target} must exist in the list.
     * The identity of {@code editedReminder} must not be the same as another existing reminder in the list.
     * The list will be automatically sorted by date after replacement.
     */
    public void setReminder(Reminder target, Reminder editedReminder) {
        requireAllNonNull(target, editedReminder);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ReminderNotFoundException();
        }

        if (!target.equals(editedReminder) && contains(editedReminder)) {
            throw new DuplicateReminderException();
        }

        internalList.set(index, editedReminder);
        sortReminders();
    }

    /**
     * Removes the equivalent reminder from the list.
     * The reminder must exist in the list.
     */
    public void remove(Reminder toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new ReminderNotFoundException();
        }
    }

    public void setReminders(UniqueReminderList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
        sortReminders();
    }

    /**
     * Replaces the contents of this list with {@code reminders}.
     * {@code reminders} must not contain duplicate reminders.
     * The list will be automatically sorted by date.
     */
    public void setReminders(List<Reminder> reminders) {
        requireAllNonNull(reminders);
        if (!remindersAreUnique(reminders)) {
            throw new DuplicateReminderException();
        }

        internalList.setAll(reminders);
        sortReminders();
    }

    /**
     * Sorts the internal list by date in ascending order.
     */
    private void sortReminders() {
        FXCollections.sort(internalList, DATE_COMPARATOR);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Reminder> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Reminder> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueReminderList)) {
            return false;
        }

        UniqueReminderList otherUniqueReminderList = (UniqueReminderList) other;
        return internalList.equals(otherUniqueReminderList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code reminders} contains only unique reminders.
     */
    private boolean remindersAreUnique(List<Reminder> reminders) {
        for (int i = 0; i < reminders.size() - 1; i++) {
            for (int j = i + 1; j < reminders.size(); j++) {
                if (reminders.get(i).equals(reminders.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
