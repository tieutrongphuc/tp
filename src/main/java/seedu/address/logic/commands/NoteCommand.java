package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

public class NoteCommand extends Command {

    public static final String COMMAND_WORD = "notes";

    public static final String MESSAGE_SUCCESS = "Displayed notes";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
