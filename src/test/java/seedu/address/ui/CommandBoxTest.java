package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.TypicalPersons;

/**
 * Contains tests for CommandBox logic methods.
 */
public class CommandBoxTest {

    /**
     * A testable wrapper class that exposes CommandBox private methods for testing.
     */
    private static class TestableCommandBox {
        private final Model model;
        private String currentSuggestion = "";

        TestableCommandBox(Model model) {
            this.model = model;
        }

        /**
         * Checks if tag suggestions should be shown.
         */
        boolean shouldShowTagSuggestions(String text, int caretPosition) {
            if (caretPosition < 2) {
                return false;
            }

            int lastTagPrefix = text.lastIndexOf("t/", caretPosition - 1);
            if (lastTagPrefix == -1) {
                return false;
            }

            String afterPrefix = text.substring(lastTagPrefix + 2, caretPosition);
            return !afterPrefix.contains(" ");
        }

        /**
         * Gets the partial tag being typed.
         */
        String getPartialTag(String text, int caretPosition) {
            int lastTagPrefix = text.lastIndexOf("t/", caretPosition - 1);
            if (lastTagPrefix == -1) {
                return "";
            }

            int tagStart = lastTagPrefix + 2;
            return text.substring(tagStart, caretPosition).trim();
        }

        /**
         * Finds tag suggestion for the given partial tag.
         */
        String findTagSuggestion(String partialTag) {
            Set<String> allTags = model.getAddressBook().getPersonList().stream()
                    .flatMap(person -> person.getTags().stream())
                    .map(tag -> tag.tagName)
                    .collect(Collectors.toSet());

            return allTags.stream()
                    .filter(tag -> tag.toLowerCase().startsWith(partialTag.toLowerCase()))
                    .filter(tag -> !tag.equalsIgnoreCase(partialTag))
                    .sorted()
                    .findFirst()
                    .orElse("");
        }

        /**
         * Calculates the new text after accepting a suggestion.
         */
        String acceptSuggestionText(String currentText, int caretPosition, String suggestion) {
            if (suggestion.isEmpty()) {
                return currentText;
            }

            String[] prefixes = { "t/", "jtt", "rtt" };
            int lastPrefixIndex = -1;
            String matchedPrefix = null;
            for (String prefix : prefixes) {
                int idx = currentText.lastIndexOf(prefix, caretPosition - 1);
                if (idx > lastPrefixIndex) {
                    lastPrefixIndex = idx;
                    matchedPrefix = prefix;
                }
            }

            if (matchedPrefix == null) {
                return currentText;
            }

            int lastTagPrefix = currentText.lastIndexOf(matchedPrefix, caretPosition);
            if (lastTagPrefix == -1) {
                return currentText;
            }

            int tagStart = lastTagPrefix + matchedPrefix.length();
            int tagEnd = currentText.indexOf(' ', tagStart);
            if (tagEnd == -1) {
                tagEnd = currentText.length();
            }

            return currentText.substring(0, tagStart) + suggestion + currentText.substring(tagEnd);
        }

        /**
         * Gets the remaining part of suggestion to display.
         */
        String getRemainingSuggestion(String partialTag, String fullSuggestion) {
            if (fullSuggestion.isEmpty() || partialTag.length() >= fullSuggestion.length()) {
                return "";
            }
            return fullSuggestion.substring(partialTag.length());
        }
    }

    /**
     * A test command executor that tracks execution.
     */
    private static class TestCommandExecutor implements CommandBox.CommandExecutor {
        private String lastExecutedCommand = null;
        private boolean shouldThrowCommandException = false;
        private boolean shouldThrowParseException = false;

        @Override
        public CommandResult execute(String commandText) throws CommandException, ParseException {
            lastExecutedCommand = commandText;
            if (shouldThrowCommandException) {
                throw new CommandException("Test command exception");
            }
            if (shouldThrowParseException) {
                throw new ParseException("Test parse exception");
            }
            return new CommandResult("Test success");
        }

        String getLastExecutedCommand() {
            return lastExecutedCommand;
        }

        void setShouldThrowCommandException(boolean shouldThrow) {
            this.shouldThrowCommandException = shouldThrow;
        }

        void setShouldThrowParseException(boolean shouldThrow) {
            this.shouldThrowParseException = shouldThrow;
        }
    }

    private Model model;
    private TestableCommandBox testableCommandBox;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        testableCommandBox = new TestableCommandBox(model);
    }

    // ========== shouldShowTagSuggestions Tests ==========

    @Test
    public void shouldShowTagSuggestions_validTagPrefix_returnsTrue() {
        assertTrue(testableCommandBox.shouldShowTagSuggestions("tag 1 t/fri", 11));
    }

    @Test
    public void shouldShowTagSuggestions_noTagPrefix_returnsFalse() {
        assertFalse(testableCommandBox.shouldShowTagSuggestions("tag 1 friend", 13));
    }

    @Test
    public void shouldShowTagSuggestions_caretPositionTooEarly_returnsFalse() {
        assertFalse(testableCommandBox.shouldShowTagSuggestions("t/", 1));
    }

    @Test
    public void shouldShowTagSuggestions_spaceAfterPrefix_returnsFalse() {
        assertFalse(testableCommandBox.shouldShowTagSuggestions("tag 1 t/ friend", 13));
    }

    @Test
    public void shouldShowTagSuggestions_caretInMiddleOfTag_returnsTrue() {
        assertTrue(testableCommandBox.shouldShowTagSuggestions("tag 1 t/friend n/Name", 14));
    }

    @Test
    public void shouldShowTagSuggestions_caretAfterSpace_returnsFalse() {
        assertFalse(testableCommandBox.shouldShowTagSuggestions("tag 1 t/friend n/Name", 15));
    }

    @Test
    public void shouldShowTagSuggestions_multipleSpacesAfterPrefix_returnsFalse() {
        assertFalse(testableCommandBox.shouldShowTagSuggestions("tag 1 t/  friend", 15));
    }

    @Test
    public void shouldShowTagSuggestions_tagPrefixAtEnd_returnsTrue() {
        assertTrue(testableCommandBox.shouldShowTagSuggestions("tag 1 t/f", 9));
    }

    @Test
    public void shouldShowTagSuggestions_justTagPrefix_returnsTrue() {
        assertTrue(testableCommandBox.shouldShowTagSuggestions("tag 1 t/", 8));
    }

    @Test
    public void shouldShowTagSuggestions_emptyString_returnsFalse() {
        assertFalse(testableCommandBox.shouldShowTagSuggestions("", 0));
    }

    @Test
    public void shouldShowTagSuggestions_caretAtStart_returnsFalse() {
        assertFalse(testableCommandBox.shouldShowTagSuggestions("tag 1 t/friend", 0));
    }

    // ========== getPartialTag Tests ==========

    @Test
    public void getPartialTag_validTagPrefix_returnsPartialTag() {
        assertEquals("fri", testableCommandBox.getPartialTag("tag 1 t/fri", 11));
    }

    @Test
    public void getPartialTag_noTagPrefix_returnsEmpty() {
        assertEquals("", testableCommandBox.getPartialTag("tag 1 friend", 13));
    }

    @Test
    public void getPartialTag_emptyAfterPrefix_returnsEmpty() {
        assertEquals("", testableCommandBox.getPartialTag("tag 1 t/", 8));
    }

    @Test
    public void getPartialTag_multipleTagPrefixes_returnsLastPartialTag() {
        assertEquals("fam", testableCommandBox.getPartialTag("tag 1 t/friend t/fam", 20));
    }

    @Test
    public void getPartialTag_withWhitespace_returnsTrimmedTag() {
        assertEquals("fri", testableCommandBox.getPartialTag("tag 1 t/ fri", 12));
    }

    @Test
    public void getPartialTag_caretAtStartOfTag_returnsEmpty() {
        assertEquals("", testableCommandBox.getPartialTag("tag 1 t/", 8));
    }

    @Test
    public void getPartialTag_complexCommand_returnsCorrectPartialTag() {
        assertEquals("frie", testableCommandBox.getPartialTag("tag 1 n/John t/frie", 19));
    }

    @Test
    public void getPartialTag_partialTagInMiddle_returnsCorrectPartial() {
        assertEquals("fr", testableCommandBox.getPartialTag("tag 1 t/friend", 10));
    }

    @Test
    public void getPartialTag_multipleWhitespaces_trimsProperly() {
        assertEquals("test", testableCommandBox.getPartialTag("tag 1 t/  test", 14));
    }

    // ========== findTagSuggestion Tests ==========

    @Test
    public void findTagSuggestion_matchingTag_returnsSuggestion() {
        String suggestion = testableCommandBox.findTagSuggestion("fri");
        assertEquals("friends", suggestion);
    }

    @Test
    public void findTagSuggestion_noMatchingTag_returnsEmpty() {
        String suggestion = testableCommandBox.findTagSuggestion("xyz");
        assertEquals("", suggestion);
    }

    @Test
    public void findTagSuggestion_emptyPartial_returnsFirstTag() {
        String suggestion = testableCommandBox.findTagSuggestion("");
        assertFalse(suggestion.isEmpty());
    }

    @Test
    public void findTagSuggestion_exactMatch_returnsEmpty() {
        String suggestion = testableCommandBox.findTagSuggestion("friends");
        assertEquals("", suggestion);
    }

    @Test
    public void findTagSuggestion_caseInsensitive_returnsSuggestion() {
        String suggestion = testableCommandBox.findTagSuggestion("FRI");
        assertEquals("friends", suggestion);
    }

    // ========== acceptSuggestionText Tests ==========

    @Test
    public void acceptSuggestionText_validSuggestion_replacesPartialTag() {
        String result = testableCommandBox.acceptSuggestionText("tag 1 t/fri", 11, "friends");
        assertEquals("tag 1 t/friends", result);
    }

    @Test
    public void acceptSuggestionText_emptySuggestion_returnsOriginal() {
        String result = testableCommandBox.acceptSuggestionText("tag 1 t/fri", 11, "");
        assertEquals("tag 1 t/fri", result);
    }

    @Test
    public void acceptSuggestionText_withTextAfter_replacesCorrectly() {
        String result = testableCommandBox.acceptSuggestionText("tag 1 t/fri n/Name", 11, "friends");
        assertEquals("tag 1 t/friends n/Name", result);
    }

    @Test
    public void acceptSuggestionText_multipleTagPrefixes_replacesLast() {
        String result = testableCommandBox.acceptSuggestionText("tag 1 t/family t/fri", 20, "friends");
        assertEquals("tag 1 t/family t/friends", result);
    }

    @Test
    public void acceptSuggestionText_noPrefix_returnsOriginal() {
        String result = testableCommandBox.acceptSuggestionText("tag 1 friend", 12, "friends");
        assertEquals("tag 1 friend", result);
    }

    @Test
    public void acceptSuggestionText_emptyPartialTag_insertsAtPrefix() {
        String result = testableCommandBox.acceptSuggestionText("tag 1 t/", 8, "friends");
        assertEquals("tag 1 t/friends", result);
    }

    // ========== getRemainingSuggestion Tests ==========

    @Test
    public void getRemainingSuggestion_validPartial_returnsRemaining() {
        String result = testableCommandBox.getRemainingSuggestion("fri", "friends");
        assertEquals("ends", result);
    }

    @Test
    public void getRemainingSuggestion_emptyPartial_returnsFullSuggestion() {
        String result = testableCommandBox.getRemainingSuggestion("", "friends");
        assertEquals("friends", result);
    }

    @Test
    public void getRemainingSuggestion_emptySuggestion_returnsEmpty() {
        String result = testableCommandBox.getRemainingSuggestion("fri", "");
        assertEquals("", result);
    }

    @Test
    public void getRemainingSuggestion_partialLongerThanSuggestion_returnsEmpty() {
        String result = testableCommandBox.getRemainingSuggestion("friends", "fri");
        assertEquals("", result);
    }

    @Test
    public void getRemainingSuggestion_exactMatch_returnsEmpty() {
        String result = testableCommandBox.getRemainingSuggestion("friends", "friends");
        assertEquals("", result);
    }

    @Test
    public void getRemainingSuggestion_singleCharRemaining_returnsChar() {
        String result = testableCommandBox.getRemainingSuggestion("friend", "friends");
        assertEquals("s", result);
    }
}
