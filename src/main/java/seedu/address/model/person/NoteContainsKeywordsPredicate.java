package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class NoteContainsKeywordsPredicate implements Predicate<Person> {
    private final String keywords;

    public NoteContainsKeywordsPredicate(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        String check = person.getNote().toString();
        return check.toLowerCase().contains(keywords.toLowerCase());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NoteContainsKeywordsPredicate)) {
            return false;
        }

        NoteContainsKeywordsPredicate otherNoteContainsKeywordsPredicate = (NoteContainsKeywordsPredicate) other;
        return keywords.equals(otherNoteContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
