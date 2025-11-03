package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.reminder.Date;
import seedu.address.model.reminder.Message;
import seedu.address.model.reminder.Reminder;
import seedu.address.testutil.ReminderBuilder;

public class ReminderAddCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nulls_throwsNullPointerException() {
        Date validDate = new ReminderBuilder().build().getDate();
        Message validMessage = new ReminderBuilder().build().getMessage();
        Index validIndex = INDEX_FIRST_PERSON;
        Name validName = ALICE.getName();

        // Null index
        assertThrows(NullPointerException.class, () -> new ReminderAddCommand((Index) null, validDate, validMessage));

        // Null name
        assertThrows(NullPointerException.class, () -> new ReminderAddCommand((Name) null, validDate, validMessage));

        // Null date
        assertThrows(NullPointerException.class, () -> new ReminderAddCommand(validIndex, null, validMessage));
        assertThrows(NullPointerException.class, () -> new ReminderAddCommand(validName, null, validMessage));

        // Null message
        assertThrows(NullPointerException.class, () -> new ReminderAddCommand(validIndex, validDate, null));
        assertThrows(NullPointerException.class, () -> new ReminderAddCommand(validName, validDate, null));
    }

    @Test
    public void execute_addReminderByIndex_success() {
        Reminder reminderToAdd = new ReminderBuilder().withPerson(ALICE).build();
        ReminderAddCommand reminderAddCommand = new ReminderAddCommand(
                INDEX_FIRST_PERSON, reminderToAdd.getDate(), reminderToAdd.getMessage());

        String expectedMessage = String.format(ReminderAddCommand.MESSAGE_SUCCESS, Messages.format(reminderToAdd));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.addReminder(reminderToAdd);

        assertCommandSuccess(reminderAddCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addReminderByName_success() {
        Reminder reminderToAdd = new ReminderBuilder().withPerson(BENSON).build();
        ReminderAddCommand reminderAddCommand = new ReminderAddCommand(
                BENSON.getName(), reminderToAdd.getDate(), reminderToAdd.getMessage());

        String expectedMessage = String.format(ReminderAddCommand.MESSAGE_SUCCESS, Messages.format(reminderToAdd));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.addReminder(reminderToAdd);

        assertCommandSuccess(reminderAddCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addReminderWithPastDate_successWithWarning() {
        Date pastDate = new Date("2020-01-01 10:00");
        Reminder reminderWithPastDate = new ReminderBuilder().withPerson(ALICE).withDate("2020-01-01 10:00").build();

        ReminderAddCommand reminderAddCommand = new ReminderAddCommand(
                INDEX_FIRST_PERSON, pastDate, reminderWithPastDate.getMessage());

        String expectedMessage = String.format(ReminderAddCommand.MESSAGE_SUCCESS + "\n"
                + ReminderAddCommand.MESSAGE_WARNING_PAST_DATE,
                Messages.format(reminderWithPastDate));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.addReminder(reminderWithPastDate);

        assertCommandSuccess(reminderAddCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateReminder_throwsCommandException() {
        Reminder reminderInList = new ReminderBuilder().withPerson(ALICE).build();
        Model modelWithReminder = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        modelWithReminder.addReminder(reminderInList);

        // Attempt to add the same reminder by index
        ReminderAddCommand reminderAddCommandByIndex = new ReminderAddCommand(
                INDEX_FIRST_PERSON, reminderInList.getDate(), reminderInList.getMessage());
        assertCommandFailure(reminderAddCommandByIndex, modelWithReminder,
            ReminderAddCommand.MESSAGE_DUPLICATE_REMINDER);

        // Attempt to add the same reminder by name
        ReminderAddCommand reminderAddCommandByName = new ReminderAddCommand(
                ALICE.getName(), reminderInList.getDate(), reminderInList.getMessage());
        assertCommandFailure(reminderAddCommandByName, modelWithReminder,
            ReminderAddCommand.MESSAGE_DUPLICATE_REMINDER);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Reminder validReminder = new ReminderBuilder().build();
        ReminderAddCommand reminderAddCommand = new ReminderAddCommand(
                outOfBoundIndex, validReminder.getDate(), validReminder.getMessage());

        assertCommandFailure(reminderAddCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_personByNameNotFound_throwsCommandException() {
        Name nonExistentName = new Name("Non Existent Person");
        Reminder validReminder = new ReminderBuilder().build();
        ReminderAddCommand reminderAddCommand = new ReminderAddCommand(
                nonExistentName, validReminder.getDate(), validReminder.getMessage());

        assertCommandFailure(reminderAddCommand, model, ReminderAddCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void equals() {
        Reminder reminderForAlice = new ReminderBuilder().withPerson(ALICE).build();
        Reminder reminderForBenson = new ReminderBuilder().withPerson(BENSON).build();

        // Index-based commands
        ReminderAddCommand addByIndexAlice = new ReminderAddCommand(
                INDEX_FIRST_PERSON, reminderForAlice.getDate(), reminderForAlice.getMessage());
        ReminderAddCommand addByIndexAliceCopy = new ReminderAddCommand(
                INDEX_FIRST_PERSON, reminderForAlice.getDate(), reminderForAlice.getMessage());
        ReminderAddCommand addByIndexBenson = new ReminderAddCommand(
                INDEX_SECOND_PERSON, reminderForBenson.getDate(), reminderForBenson.getMessage());

        // Name-based commands
        ReminderAddCommand addByNameAlice = new ReminderAddCommand(
                ALICE.getName(), reminderForAlice.getDate(), reminderForAlice.getMessage());
        ReminderAddCommand addByNameAliceCopy = new ReminderAddCommand(
                ALICE.getName(), reminderForAlice.getDate(), reminderForAlice.getMessage());
        ReminderAddCommand addByNameBenson = new ReminderAddCommand(
                BENSON.getName(), reminderForBenson.getDate(), reminderForBenson.getMessage());

        // same object -> returns true
        assertTrue(addByIndexAlice.equals(addByIndexAlice));
        assertTrue(addByNameAlice.equals(addByNameAlice));

        // same values -> returns true
        assertTrue(addByIndexAlice.equals(addByIndexAliceCopy));
        assertTrue(addByNameAlice.equals(addByNameAliceCopy));

        // different types -> returns false
        assertFalse(addByIndexAlice.equals(1));

        // null -> returns false
        assertFalse(addByIndexAlice.equals(null));

        // different person (index-based) -> returns false
        assertFalse(addByIndexAlice.equals(addByIndexBenson));

        // different person (name-based) -> returns false
        assertFalse(addByNameAlice.equals(addByNameBenson));

        // different command type (index vs name) -> returns false
        assertFalse(addByIndexAlice.equals(addByNameAlice));
    }

    @Test
    public void toStringMethod() {
        // Test for index-based command
        Index index = Index.fromOneBased(1);
        Reminder reminder = new ReminderBuilder().build();
        ReminderAddCommand indexBasedCommand = new ReminderAddCommand(index, reminder.getDate(), reminder.getMessage());
        String toStringResult = indexBasedCommand.toString();

        assertTrue(toStringResult.contains(ReminderAddCommand.class.getSimpleName()));
        assertTrue(toStringResult.contains("targetIndex=" + index));
        assertTrue(toStringResult.contains("date=" + reminder.getDate()));
        assertTrue(toStringResult.contains("message=" + reminder.getMessage()));
        assertTrue(toStringResult.contains("isIndexBased=true"));

        // Test for name-based command
        Name name = new Name("Test Name");
        ReminderAddCommand nameBasedCommand = new ReminderAddCommand(name, reminder.getDate(), reminder.getMessage());
        String nameToStringResult = nameBasedCommand.toString();

        assertTrue(nameToStringResult.contains(ReminderAddCommand.class.getSimpleName()));
        assertTrue(nameToStringResult.contains("personName=" + name));
        assertTrue(nameToStringResult.contains("date=" + reminder.getDate()));
        assertTrue(nameToStringResult.contains("message=" + reminder.getMessage()));
        assertTrue(nameToStringResult.contains("isIndexBased=false"));
    }
}
