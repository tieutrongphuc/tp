package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Contains tests for {@code NoteEditView}.
 */
public class NoteEditViewTest {

    private static final int TIMEOUT_SECONDS = 5;

    @BeforeAll
    public static void initToolkit() {
        // Initialize JavaFX toolkit for testing
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Platform already initialized
        }
    }

    /**
     * Helper method to create a Person with a note.
     */
    private Person createPersonWithNote(String name, String noteContent) {
        return new Person(
            new Name(name),
            new Phone("12345678"),
            new Email("test@example.com"),
            new Address("123 Test Street"),
            new Note(noteContent),
            new HashSet<>()
        );
    }

    /**
     * Helper method to create a Person without a note (empty note).
     */
    private Person createPersonWithoutNote(String name) {
        return new Person(
            new Name(name),
            new Phone("12345678"),
            new Email("test@example.com"),
            new Address("123 Test Street"),
            new HashSet<>()
        );
    }

    /**
     * Helper method to run tests on JavaFX thread and wait for completion.
     */
    private void runAndWait(Runnable runnable) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                runnable.run();
            } finally {
                latch.countDown();
            }
        });
        assertTrue(latch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
    }

    @Test
    public void constructor_success() throws InterruptedException {
        runAndWait(() -> {
            NoteEditView noteEditView = new NoteEditView();
            assertNotNull(noteEditView);
        });
    }

    @Test
    public void setPerson_personWithNote_setsNoteTextAndCurrentPerson() throws InterruptedException {
        runAndWait(() -> {
            NoteEditView noteEditView = new NoteEditView();
            String noteContent = "This is a test note";
            Person person = createPersonWithNote("Alice", noteContent);

            noteEditView.setPerson(person);

            // verify note content is set
            assertEquals(noteContent, noteEditView.getNoteContent());
            // verify current person is set
            assertEquals(person, noteEditView.getCurrentPerson());
        });
    }

    @Test
    public void setPerson_personWithEmptyNote_clearsNoteText() throws InterruptedException {
        runAndWait(() -> {
            NoteEditView noteEditView = new NoteEditView();
            Person person = createPersonWithNote("Bob", "");

            noteEditView.setPerson(person);

            // empty note should result in empty text area
            assertEquals("", noteEditView.getNoteContent());
            assertEquals(person, noteEditView.getCurrentPerson());
        });
    }

    @Test
    public void setPerson_personWithNullNote_clearsNoteText() throws InterruptedException {
        runAndWait(() -> {
            NoteEditView noteEditView = new NoteEditView();
            Person person = createPersonWithoutNote("Charlie");

            noteEditView.setPerson(person);

            // null note should clear the text area
            String content = noteEditView.getNoteContent();
            assertTrue(content == null || content.isEmpty());
            assertEquals(person, noteEditView.getCurrentPerson());
        });
    }

    @Test
    public void setPerson_multiplePersonsSequentially_updatesCorrectly() throws InterruptedException {
        runAndWait(() -> {
            NoteEditView noteEditView = new NoteEditView();

            // set first person with note
            Person person1 = createPersonWithNote("Alice", "Note for Alice");
            noteEditView.setPerson(person1);
            assertEquals("Note for Alice", noteEditView.getNoteContent());
            assertEquals(person1, noteEditView.getCurrentPerson());

            // set second person with different note
            Person person2 = createPersonWithNote("Bob", "Note for Bob");
            noteEditView.setPerson(person2);
            assertEquals("Note for Bob", noteEditView.getNoteContent());
            assertEquals(person2, noteEditView.getCurrentPerson());

            // set third person with empty note
            Person person3 = createPersonWithNote("Charlie", "");
            noteEditView.setPerson(person3);
            assertEquals("", noteEditView.getNoteContent());
            assertEquals(person3, noteEditView.getCurrentPerson());
        });
    }

    @Test
    public void setPerson_longNote_setsNoteText() throws InterruptedException {
        runAndWait(() -> {
            NoteEditView noteEditView = new NoteEditView();
            // create a note close to the 5000 character limit
            String longNote = "A".repeat(4999);
            Person person = createPersonWithNote("David", longNote);

            noteEditView.setPerson(person);

            assertEquals(longNote, noteEditView.getNoteContent());
        });
    }

    @Test
    public void setPerson_noteAtMaxCharLimit_setsNoteText() throws InterruptedException {
        runAndWait(() -> {
            NoteEditView noteEditView = new NoteEditView();
            // exactly at the 5000 character limit
            String maxNote = "B".repeat(5000);
            Person person = createPersonWithNote("Eve", maxNote);

            noteEditView.setPerson(person);

            assertEquals(maxNote, noteEditView.getNoteContent());
        });
    }

    @Test
    public void setPerson_noteWithSpecialCharacters_setsNoteText() throws InterruptedException {
        runAndWait(() -> {
            NoteEditView noteEditView = new NoteEditView();
            // note with various special characters
            String specialNote = "Test note with\nnew lines\tand tabs\r\nand special chars: !@#$%^&*()";
            Person person = createPersonWithNote("Frank", specialNote);

            noteEditView.setPerson(person);

            assertEquals(specialNote, noteEditView.getNoteContent());
        });
    }

    @Test
    public void setPerson_noteWithUnicodeCharacters_setsNoteText() throws InterruptedException {
        runAndWait(() -> {
            NoteEditView noteEditView = new NoteEditView();
            // note with unicode characters
            String unicodeNote = "测试中文 テスト 한국어 🎉 Emoji";
            Person person = createPersonWithNote("Grace", unicodeNote);

            noteEditView.setPerson(person);

            assertEquals(unicodeNote, noteEditView.getNoteContent());
        });
    }

    @Test
    public void getCurrentPerson_noPersonSet_returnsNull() throws InterruptedException {
        runAndWait(() -> {
            NoteEditView noteEditView = new NoteEditView();

            // no person set initially
            assertNull(noteEditView.getCurrentPerson());
        });
    }

    @Test
    public void getCurrentPerson_personSet_returnsSamePerson() throws InterruptedException {
        runAndWait(() -> {
            NoteEditView noteEditView = new NoteEditView();
            Person person = createPersonWithNote("Henry", "Test note");

            noteEditView.setPerson(person);

            // should return the exact same person object
            Person retrievedPerson = noteEditView.getCurrentPerson();
            assertEquals(person, retrievedPerson);
            assertTrue(person == retrievedPerson); // same reference
        });
    }

    @Test
    public void getNoteContent_emptyTextArea_returnsEmptyString() throws InterruptedException {
        runAndWait(() -> {
            NoteEditView noteEditView = new NoteEditView();

            // initially empty text area
            String content = noteEditView.getNoteContent();
            assertTrue(content == null || content.isEmpty());
        });
    }

    @Test
    public void getNoteContent_afterSetPerson_returnsCorrectContent() throws InterruptedException {
        runAndWait(() -> {
            NoteEditView noteEditView = new NoteEditView();
            String expectedContent = "Expected note content";
            Person person = createPersonWithNote("Ivy", expectedContent);

            noteEditView.setPerson(person);

            assertEquals(expectedContent, noteEditView.getNoteContent());
        });
    }
}

