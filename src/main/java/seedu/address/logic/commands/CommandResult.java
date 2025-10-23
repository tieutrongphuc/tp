package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** Note editing mode should be shown to the user. */
    private final boolean showNoteEdit;

    /** Index of the person whose note is being editted. */
    private final Index targetPersonIndex;

    /** The application should exit. */
    private final boolean exit;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.showNoteEdit = false;
        this.targetPersonIndex = null;
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields, meant for note editing mode.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, boolean showNoteEdit, Index
            targetPersonIndex) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.showNoteEdit = showNoteEdit;
        this.targetPersonIndex = targetPersonIndex;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean isShowNoteEdit() {
        return showNoteEdit;
    }

    public Index getTargetPersonIndex() {
        return targetPersonIndex;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit
                && showNoteEdit == otherCommandResult.showNoteEdit
                && targetPersonIndex == otherCommandResult.targetPersonIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, showNoteEdit, targetPersonIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .add("showNoteEdit", showNoteEdit)
                .add("targetPersonIndex", targetPersonIndex)
                .toString();
    }

}
