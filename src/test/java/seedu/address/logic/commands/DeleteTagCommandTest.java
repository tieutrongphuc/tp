package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for DeleteTagCommand.
 */
public class DeleteTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_deleteSingleTagUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToDelete = new HashSet<>();

        if (!personToEdit.getTags().isEmpty()) {
            tagsToDelete.add(personToEdit.getTags().iterator().next());
        } else {

            Tag temp = new Tag("tempTagToDelete");
            Set<Tag> updatedTagsInitial = new HashSet<>(personToEdit.getTags());
            updatedTagsInitial.add(temp);
            Person withTempTag = new PersonBuilder(personToEdit).withTags(
                    updatedTagsInitial.stream().map(tag -> tag.tagName).toArray(String[]::new)).build();
            model.setPerson(personToEdit, withTempTag);
            personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
            tagsToDelete.add(temp);
        }

        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        updatedTags.removeAll(tagsToDelete);

        Person editedPerson = new PersonBuilder(personToEdit).withTags(
                updatedTags.stream().map(tag -> tag.tagName).toArray(String[]::new)).build();

        DeleteTagCommand deleteCommand = new DeleteTagCommand(INDEX_FIRST_PERSON, tagsToDelete);

        String expectedMessage = String.format(DeleteTagCommand.MESSAGE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteMultipleTagsUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToDelete = new HashSet<>();

        // ensure the person has at least two tags; if not, add temporary tags to the person in the model
        Set<Tag> updatedTagsInitial = new HashSet<>(personToEdit.getTags());
        int counter = 0;
        while (updatedTagsInitial.size() < 2) {
            updatedTagsInitial.add(new Tag("extra" + counter));
            counter++;
        }
        if (!updatedTagsInitial.equals(personToEdit.getTags())) {
            Person withExtraTags = new PersonBuilder(personToEdit).withTags(
                    updatedTagsInitial.stream().map(tag -> tag.tagName).toArray(String[]::new)).build();
            model.setPerson(personToEdit, withExtraTags);
            personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        }

        // pick two tags from the person's current tags
        int picked = 0;
        for (Tag t : personToEdit.getTags()) {
            if (picked < 2) {
                tagsToDelete.add(t);
                picked++;
            }
        }

        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        updatedTags.removeAll(tagsToDelete);

        Person editedPerson = new PersonBuilder(personToEdit).withTags(
                updatedTags.stream().map(tag -> tag.tagName).toArray(String[]::new)).build();

        DeleteTagCommand deleteCommand = new DeleteTagCommand(INDEX_FIRST_PERSON, tagsToDelete);

        String expectedMessage = String.format(DeleteTagCommand.MESSAGE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteNonExistentTag_throwsCommandException() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToDelete = new HashSet<>();
        tagsToDelete.add(new Tag("thisTagDoesNotExist"));

        DeleteTagCommand deleteCommand = new DeleteTagCommand(INDEX_FIRST_PERSON, tagsToDelete);

        assertCommandFailure(deleteCommand, model, String.format(DeleteTagCommand.MESSAGE_TAG_NOT_FOUND, tagsToDelete));
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToDelete = new HashSet<>();
        if (!personToEdit.getTags().isEmpty()) {
            tagsToDelete.add(personToEdit.getTags().iterator().next());
        } else {
            tagsToDelete.add(new Tag("filteredOnly"));
        }

        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        updatedTags.removeAll(tagsToDelete);

        Person editedPerson = new PersonBuilder(personToEdit).withTags(
                updatedTags.stream().map(tag -> tag.tagName).toArray(String[]::new)).build();

        DeleteTagCommand deleteCommand = new DeleteTagCommand(INDEX_FIRST_PERSON, tagsToDelete);

        String expectedMessage = String.format(DeleteTagCommand.MESSAGE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<Tag> tagsToDelete = new HashSet<>();
        tagsToDelete.add(new Tag("tag"));

        DeleteTagCommand deleteCommand = new DeleteTagCommand(outOfBoundIndex, tagsToDelete);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        Set<Tag> tagsToDelete = new HashSet<>();
        tagsToDelete.add(new Tag("tag"));

        DeleteTagCommand deleteCommand = new DeleteTagCommand(outOfBoundIndex, tagsToDelete);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<Tag> tags1 = new HashSet<>();
        tags1.add(new Tag("tag1"));

        Set<Tag> tags2 = new HashSet<>();
        tags2.add(new Tag("tag2"));

        DeleteTagCommand deleteFirstCommand = new DeleteTagCommand(INDEX_FIRST_PERSON, tags1);
        DeleteTagCommand deleteSecondCommand = new DeleteTagCommand(INDEX_SECOND_PERSON, tags1);
        DeleteTagCommand deleteDifferentTagCommand = new DeleteTagCommand(INDEX_FIRST_PERSON, tags2);

        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        Set<Tag> tags1Copy = new HashSet<>();
        tags1Copy.add(new Tag("tag1"));
        DeleteTagCommand deleteFirstCommandCopy = new DeleteTagCommand(INDEX_FIRST_PERSON, tags1Copy);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        assertFalse(deleteFirstCommand.equals(1));

        assertFalse(deleteFirstCommand.equals(null));

        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));

        assertFalse(deleteFirstCommand.equals(deleteDifferentTagCommand));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("tag"));
        DeleteTagCommand deleteCommand = new DeleteTagCommand(index, tags);
        String expected = DeleteTagCommand.class.getCanonicalName() + "{index=" + index + ", tagsToDelete=" + tags + "}";
        assertEquals(expected, deleteCommand.toString());
    }
}
