package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Alternatively, lists all persons in address book with any tag matching the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";


    public static final String MESSAGE_USAGE = COMMAND_WORD + ": choose one of searching by name, tag, or note.\n"
            + "Find by name: finds all persons whose names match the specified keywords.\n"
            + "Find by tag: finds all persons whose tags match the specified tags. "
            + "Use 't/' to indicate search by tag.\n"
            + "Find by note: finds all persons whose notes contain the specified keywords. "
            + "Use 'note/' to indicate search by note.\n"
            + "\nParameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: '" + COMMAND_WORD + " alice bob charlie' for find by name\n"
            + "Or: '" + COMMAND_WORD + " t/friends t/colleagues' for find by tag\n"
            + "Or: '" + COMMAND_WORD + " note/expressed interest in AI' for find by note\n"
            + "Searching is case-insensitive.";

    public static final String MESSAGE_MULTIPLE_SEARCH = "Please search by only one field.";

    private final Predicate<Person> predicate;

    public FindCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
