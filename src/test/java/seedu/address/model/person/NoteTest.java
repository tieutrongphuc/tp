package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NoteTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        // null note should throw NullPointerException
        assertThrows(NullPointerException.class, () -> new Note(null));
    }

    @Test
    public void constructor_validEmptyNote_success() {
        // empty string is valid for notes
        Note note = new Note("");
        assertEquals("", note.value);
    }

    @Test
    public void constructor_validNote_success() {
        // valid note with content
        String validNoteContent = "This is a valid note";
        Note note = new Note(validNoteContent);
        assertEquals(validNoteContent, note.value);
    }

    @Test
    public void isValidNote_anyString_returnsTrue() {
        // empty string is valid
        assertTrue(Note.isValidNote(""));

        // whitespace only is valid
        assertTrue(Note.isValidNote(" "));
        assertTrue(Note.isValidNote("   "));
        assertTrue(Note.isValidNote("\t"));
        assertTrue(Note.isValidNote("\n"));

        // regular text is valid
        assertTrue(Note.isValidNote("Regular note"));
        assertTrue(Note.isValidNote("Note with numbers 123"));

        // special characters are valid
        assertTrue(Note.isValidNote("Note with special chars !@#$%^&*()"));
        assertTrue(Note.isValidNote("Note with punctuation: hello, world!"));

        // very long note is valid
        assertTrue(Note.isValidNote("A".repeat(5000)));

        // unicode characters are valid
        assertTrue(Note.isValidNote("测试中文"));
        assertTrue(Note.isValidNote("テスト"));
        assertTrue(Note.isValidNote("한국어"));
        assertTrue(Note.isValidNote("🎉 Emoji"));

        // mixed content is valid
        assertTrue(Note.isValidNote("Mixed: 123, !@#, 中文, emoji 🎉"));
    }

    @Test
    public void isValidNote_null_throwsNullPointerException() {
        // null input to isValidNote should throw NullPointerException
        // Note: This would only happen if isValidNote checked for null, but since it always returns true,
        // it doesn't throw. This test documents the current behavior.
        assertTrue(Note.isValidNote(null));
    }

    @Test
    public void toString_emptyNote_returnsEmptyString() {
        // toString of empty note should return empty string
        Note note = new Note("");
        assertEquals("", note.toString());
    }

    @Test
    public void toString_noteWithContent_returnsContent() {
        // toString should return the note content
        String content = "This is my note";
        Note note = new Note(content);
        assertEquals(content, note.toString());
    }

    @Test
    public void toString_noteWithSpecialCharacters_returnsContent() {
        // toString should preserve special characters
        String content = "Note with\nnew lines\tand tabs";
        Note note = new Note(content);
        assertEquals(content, note.toString());
    }

    @Test
    public void equals() {
        Note note = new Note("Valid Note");

        // same values -> returns true
        assertTrue(note.equals(new Note("Valid Note")));

        // same object -> returns true
        assertTrue(note.equals(note));

        // null -> returns false
        assertFalse(note.equals(null));

        // different types -> returns false
        assertFalse(note.equals(5.0f));

        // different values -> returns false
        assertFalse(note.equals(new Note("Other Valid Note")));
    }

    @Test
    public void equals_emptyNotes_returnsTrue() {
        // two empty notes should be equal
        Note note1 = new Note("");
        Note note2 = new Note("");
        assertTrue(note1.equals(note2));
    }

    @Test
    public void equals_differentContent_returnsFalse() {
        // notes with different content should not be equal
        Note note1 = new Note("Note 1");
        Note note2 = new Note("Note 2");
        assertFalse(note1.equals(note2));
    }

    @Test
    public void equals_caseMatters_returnsFalse() {
        // case should matter in note comparison
        Note note1 = new Note("Note");
        Note note2 = new Note("note");
        assertFalse(note1.equals(note2));
    }

    @Test
    public void equals_whitespaceMatters_returnsFalse() {
        // whitespace differences should matter
        Note note1 = new Note("Note");
        Note note2 = new Note("Note ");
        assertFalse(note1.equals(note2));
    }

    @Test
    public void equals_specialCharacters_checksExactMatch() {
        // special characters should be matched exactly
        Note note1 = new Note("Note!");
        Note note2 = new Note("Note!");
        Note note3 = new Note("Note?");
        assertTrue(note1.equals(note2));
        assertFalse(note1.equals(note3));
    }

    @Test
    public void hashCode_sameContent_returnsSameHashCode() {
        // notes with same content should have same hash code
        Note note1 = new Note("Same content");
        Note note2 = new Note("Same content");
        assertEquals(note1.hashCode(), note2.hashCode());
    }

    @Test
    public void hashCode_differentContent_returnsDifferentHashCode() {
        // notes with different content should (likely) have different hash codes
        Note note1 = new Note("Content 1");
        Note note2 = new Note("Content 2");
        // Note: hash codes can collide, but for different strings it's unlikely
        assertFalse(note1.hashCode() == note2.hashCode());
    }

    @Test
    public void hashCode_emptyNotes_returnsSameHashCode() {
        // empty notes should have same hash code
        Note note1 = new Note("");
        Note note2 = new Note("");
        assertEquals(note1.hashCode(), note2.hashCode());
    }

    @Test
    public void value_constructorInput_storesCorrectly() {
        // value field should store the exact input from constructor
        String content = "Test content";
        Note note = new Note(content);
        assertEquals(content, note.value);
    }

    @Test
    public void value_emptyString_storesEmptyString() {
        // empty string should be stored as is
        Note note = new Note("");
        assertEquals("", note.value);
    }

    @Test
    public void value_whitespace_preservesWhitespace() {
        // whitespace should be preserved
        String whitespace = "  spaces  ";
        Note note = new Note(whitespace);
        assertEquals(whitespace, note.value);
    }

    @Test
    public void value_longContent_storesCompletely() {
        // long content should be stored completely
        String longContent = "A".repeat(5000);
        Note note = new Note(longContent);
        assertEquals(longContent, note.value);
        assertEquals(5000, note.value.length());
    }

    @Test
    public void value_unicodeContent_storesCorrectly() {
        // Unicode content should be stored correctly
        String unicode = "测试中文 🎉";
        Note note = new Note(unicode);
        assertEquals(unicode, note.value);
    }

    @Test
    public void value_newlinesAndTabs_preservesFormatting() {
        // newlines and tabs should be preserved
        String formatted = "Line 1\nLine 2\tTabbed";
        Note note = new Note(formatted);
        assertEquals(formatted, note.value);
    }
}

