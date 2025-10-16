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
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for TagCommand.
 */
public class TagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addSingleTagUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("newTag"));

        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        updatedTags.addAll(tagsToAdd);

        Person editedPerson = new PersonBuilder(personToEdit).withTags(
                updatedTags.stream().map(tag -> tag.tagName).toArray(String[]::new)).build();

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addMultipleTagsUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("tag1"));
        tagsToAdd.add(new Tag("tag2"));

        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        updatedTags.addAll(tagsToAdd);

        Person editedPerson = new PersonBuilder(personToEdit).withTags(
                updatedTags.stream().map(tag -> tag.tagName).toArray(String[]::new)).build();

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addDuplicateTag_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> existingTags = personToEdit.getTags();

        // Add a tag that already exists
        Set<Tag> tagsToAdd = new HashSet<>();
        if (!existingTags.isEmpty()) {
            tagsToAdd.add(existingTags.iterator().next());
        } else {
            tagsToAdd.add(new Tag("newTag"));
        }

        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        updatedTags.addAll(tagsToAdd);

        Person editedPerson = new PersonBuilder(personToEdit).withTags(
                updatedTags.stream().map(tag -> tag.tagName).toArray(String[]::new)).build();

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("filteredTag"));

        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        updatedTags.addAll(tagsToAdd);

        Person editedPerson = new PersonBuilder(personToEdit).withTags(
                updatedTags.stream().map(tag -> tag.tagName).toArray(String[]::new)).build();

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("tag"));

        TagCommand tagCommand = new TagCommand(outOfBoundIndex, tagsToAdd);

        assertCommandFailure(tagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
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

        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("tag"));

        TagCommand tagCommand = new TagCommand(outOfBoundIndex, tagsToAdd);

        assertCommandFailure(tagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<Tag> tags1 = new HashSet<>();
        tags1.add(new Tag("tag1"));

        Set<Tag> tags2 = new HashSet<>();
        tags2.add(new Tag("tag2"));

        TagCommand tagFirstCommand = new TagCommand(INDEX_FIRST_PERSON, tags1);
        TagCommand tagSecondCommand = new TagCommand(INDEX_SECOND_PERSON, tags1);
        TagCommand tagDifferentTagCommand = new TagCommand(INDEX_FIRST_PERSON, tags2);

        assertTrue(tagFirstCommand.equals(tagFirstCommand));

        Set<Tag> tags1Copy = new HashSet<>();
        tags1Copy.add(new Tag("tag1"));
        TagCommand tagFirstCommandCopy = new TagCommand(INDEX_FIRST_PERSON, tags1Copy);
        assertTrue(tagFirstCommand.equals(tagFirstCommandCopy));

        assertFalse(tagFirstCommand.equals(1));

        assertFalse(tagFirstCommand.equals(null));

        assertFalse(tagFirstCommand.equals(tagSecondCommand));

        assertFalse(tagFirstCommand.equals(tagDifferentTagCommand));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("tag"));
        TagCommand tagCommand = new TagCommand(index, tags);
        String expected = TagCommand.class.getCanonicalName() + "{index=" + index + ", tags=" + tags + "}";
        assertEquals(expected, tagCommand.toString());
    }
}

