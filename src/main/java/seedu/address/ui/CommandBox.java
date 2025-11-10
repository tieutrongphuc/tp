package seedu.address.ui;

import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {
    // the auto-complete suggestion is made with the help of AI
    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;
    private final Model model;
    private String currentSuggestion = "";

    @FXML
    private TextField commandTextField;

    @FXML
    private Text suggestionText;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor} and {@code Model}.
     */
    public CommandBox(CommandExecutor commandExecutor, Model model) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.model = model;

        // Initialize suggestion text styling
        suggestionText.setFill(javafx.scene.paint.Color.web("#4A90E2"));
        suggestionText.setOpacity(1);


        commandTextField.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.TAB) {
                if (!currentSuggestion.isEmpty()) {
                    acceptSuggestion();
                }
                event.consume(); // Always consume Tab to prevent focus traversal
            }
        });

        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> {
            setStyleToDefault();
            handleTextChanged();
        });

        // Update suggestion position when caret moves
        commandTextField.caretPositionProperty().addListener((obs, oldPos, newPos) -> {
            handleTextChanged();
        });
    }

    /**
     * Accepts the current suggestion and updates the text field.
     */
    private void acceptSuggestion() {
        if (currentSuggestion.isEmpty()) {
            return;
        }

        String currentText = commandTextField.getText();
        int caretPosition = commandTextField.getCaretPosition();

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


        int lastTagPrefix = currentText.lastIndexOf(matchedPrefix, caretPosition);
        if (lastTagPrefix == -1) {
            currentSuggestion = "";
            suggestionText.setVisible(false);
            return;
        }

        int tagStart = lastTagPrefix + matchedPrefix.length();
        int tagEnd = currentText.indexOf(' ', tagStart);
        if (tagEnd == -1) {
            tagEnd = currentText.length();
        }

        String newText = currentText.substring(0, tagStart) + currentSuggestion + currentText.substring(tagEnd);

        int newCaretPosition = tagStart + currentSuggestion.length();

        commandTextField.setText(newText);
        commandTextField.positionCaret(newCaretPosition);

        currentSuggestion = "";
        suggestionText.setVisible(false);
    }

    /**
     * Handles text changes and shows tag suggestions if applicable.
     */
    private void handleTextChanged() {
        String text = commandTextField.getText();
        int caretPosition = commandTextField.getCaretPosition();

        if (shouldShowTagSuggestions(text, caretPosition)) {
            String partialTag = getPartialTag(text, caretPosition);
            showTagSuggestion(partialTag, text, caretPosition);
        } else {
            currentSuggestion = "";
            suggestionText.setVisible(false);
        }
    }

    /**
     * Checks if tag suggestions should be shown.
     */
    private boolean shouldShowTagSuggestions(String text, int caretPosition) {
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
    private String getPartialTag(String text, int caretPosition) {
        int lastTagPrefix = text.lastIndexOf("t/", caretPosition - 1);
        if (lastTagPrefix == -1) {
            return "";
        }

        int tagStart = lastTagPrefix + 2;
        return text.substring(tagStart, caretPosition).trim();
    }

    /**
     * Shows tag suggestion as blue overlay text.
     */
    private void showTagSuggestion(String partialTag, String currentText, int caretPosition) {
        // Get all existing tags from the address book
        Set<String> allTags = model.getAddressBook().getPersonList().stream()
                .flatMap(person -> person.getTags().stream())
                .map(tag -> tag.tagName)
                .collect(Collectors.toSet());

        // Find the first matching tag
        String suggestion = allTags.stream()
                .filter(tag -> tag.toLowerCase().startsWith(partialTag.toLowerCase()))
                .filter(tag -> !tag.equalsIgnoreCase(partialTag))
                .sorted()
                .findFirst()
                .orElse("");

        if (!suggestion.isEmpty() && caretPosition == currentText.length()) {
            currentSuggestion = suggestion;

            // Only show the REMAINING part that hasn't been typed yet
            String remainingPart = suggestion.substring(partialTag.length());
            suggestionText.setText(remainingPart);
            commandTextField.applyCss();
            commandTextField.layout();
            Text measureText = new Text(currentText);
            measureText.setFont(commandTextField.getFont());
            double currentTextWidth = measureText.getLayoutBounds().getWidth();

            double textFieldPadding = 12.0;
            suggestionText.setLayoutX(textFieldPadding + currentTextWidth);
            suggestionText.setLayoutY(commandTextField.getHeight() / 2 + 6);
            suggestionText.setFont(commandTextField.getFont());

            suggestionText.setVisible(true);
        } else {
            currentSuggestion = "";
            suggestionText.setVisible(false);
        }
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        currentSuggestion = "";
        suggestionText.setVisible(false);

        String commandText = commandTextField.getText();
        if (commandText.equals("")) {
            return;
        }

        try {
            commandExecutor.execute(commandText);
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

}
