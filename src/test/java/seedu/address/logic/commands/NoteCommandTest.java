package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code NoteCommand}.
 */
public class NoteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        // get the person at the first index
        Person targetPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        NoteCommand noteCommand = new NoteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(NoteCommand.MESSAGE_SUCCESS, targetPerson.getName());

        // create expected CommandResult with note editing flags
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true,
                INDEX_FIRST_PERSON);

        // model should remain unchanged after opening note editor
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(noteCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_validIndexSecondPersonUnfilteredList_success() {
        // test with second person
        Person targetPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        NoteCommand noteCommand = new NoteCommand(INDEX_SECOND_PERSON);

        String expectedMessage = String.format(NoteCommand.MESSAGE_SUCCESS, targetPerson.getName());
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true,
                INDEX_SECOND_PERSON);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(noteCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_validIndexThirdPersonUnfilteredList_success() {
        // test with third person
        Person targetPerson = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        NoteCommand noteCommand = new NoteCommand(INDEX_THIRD_PERSON);

        String expectedMessage = String.format(NoteCommand.MESSAGE_SUCCESS, targetPerson.getName());
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true,
                INDEX_THIRD_PERSON);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(noteCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        // index out of bounds
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        NoteCommand noteCommand = new NoteCommand(outOfBoundIndex);

        assertCommandFailure(noteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFarOutOfBounds_throwsCommandException() {
        // index far beyond the list size
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 100);
        NoteCommand noteCommand = new NoteCommand(outOfBoundIndex);

        assertCommandFailure(noteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        // filter the list to show only one person
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person targetPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        NoteCommand noteCommand = new NoteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(NoteCommand.MESSAGE_SUCCESS, targetPerson.getName());
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true,
                INDEX_FIRST_PERSON);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(noteCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        // filter list to show only first person
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        NoteCommand noteCommand = new NoteCommand(outOfBoundIndex);

        assertCommandFailure(noteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_boundaryIndexUnfilteredList_success() {
        // test with last person in the list (boundary case)
        int lastIndex = model.getFilteredPersonList().size();
        Index lastPersonIndex = Index.fromOneBased(lastIndex);
        Person targetPerson = model.getFilteredPersonList().get(lastPersonIndex.getZeroBased());
        NoteCommand noteCommand = new NoteCommand(lastPersonIndex);

        String expectedMessage = String.format(NoteCommand.MESSAGE_SUCCESS, targetPerson.getName());
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true,
                lastPersonIndex);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(noteCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void equals() {
        NoteCommand noteFirstCommand = new NoteCommand(INDEX_FIRST_PERSON);
        NoteCommand noteSecondCommand = new NoteCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(noteFirstCommand.equals(noteFirstCommand));

        // same values -> returns true
        NoteCommand noteFirstCommandCopy = new NoteCommand(INDEX_FIRST_PERSON);
        assertTrue(noteFirstCommand.equals(noteFirstCommandCopy));

        // different types -> returns false
        assertFalse(noteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(noteFirstCommand.equals(null));

        // different person index -> returns false
        assertFalse(noteFirstCommand.equals(noteSecondCommand));
    }

    @Test
    public void equals_sameIndex_returnsTrue() {
        // create two commands with same index value
        Index index = Index.fromOneBased(1);
        NoteCommand command1 = new NoteCommand(index);
        NoteCommand command2 = new NoteCommand(Index.fromOneBased(1));

        // same index value -> returns true
        assertTrue(command1.equals(command2));
    }

    @Test
    public void equals_differentIndex_returnsFalse() {
        // create commands with different index values
        NoteCommand command1 = new NoteCommand(Index.fromOneBased(1));
        NoteCommand command2 = new NoteCommand(Index.fromOneBased(2));
        NoteCommand command3 = new NoteCommand(Index.fromOneBased(100));

        // different index values -> returns false
        assertFalse(command1.equals(command2));
        assertFalse(command1.equals(command3));
        assertFalse(command2.equals(command3));
    }

    @Test
    public void constructor_validIndex_success() {
        // constructor with valid index should not throw exception
        Index validIndex = Index.fromOneBased(1);
        NoteCommand noteCommand = new NoteCommand(validIndex);

        assertEquals(new NoteCommand(validIndex), noteCommand);
    }

    @Test
    public void constructor_differentIndexes_createsDistinctCommands() {
        // different indexes should create different commands
        NoteCommand command1 = new NoteCommand(INDEX_FIRST_PERSON);
        NoteCommand command2 = new NoteCommand(INDEX_SECOND_PERSON);
        assertFalse(command1.equals(command2));
    }
}

