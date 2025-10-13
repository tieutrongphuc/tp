package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

public class NoteCommand extends Command {

    public static final String COMMAND_WORD = "note";

    public static final String MESSAGE_SUCCESS = "Displayed notes";

    private final Index index;
    private final Note note;

    public NoteCommand(Index index, Note note) {
        this.index = index;
        this.note = note;
    }


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
