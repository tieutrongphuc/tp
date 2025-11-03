package seedu.address.ui;

import java.awt.Point;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Reminder;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private ReminderListPanel reminderListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;

    private NoteEditView noteEditView;
    private boolean isNoteEditMode = false;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane reminderListPanelPlaceholder;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private Label headerLabel;

    @FXML
    private HBox contactsHeader;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        ObservableList<Person> filteredPersonList = logic.getFilteredPersonList();
        ObservableList<Reminder> filteredReminderList = logic.getFilteredReminderList();
        personListPanel = new PersonListPanel(filteredPersonList, filteredReminderList);
        ObservableList<Node> personListPanelChildren = personListPanelPlaceholder.getChildren();
        Region personListPanelRoot = personListPanel.getRoot();
        personListPanelChildren.add(personListPanelRoot);

        reminderListPanel = new ReminderListPanel(filteredReminderList);
        ObservableList<Node> reminderListPanelChildren = reminderListPanelPlaceholder.getChildren();
        Region reminderListPanelRoot = reminderListPanel.getRoot();
        reminderListPanelChildren.add(reminderListPanelRoot);

        resultDisplay = new ResultDisplay();
        ObservableList<Node> resultDisplayChildren = resultDisplayPlaceholder.getChildren();
        Region resultDisplayRoot = resultDisplay.getRoot();
        resultDisplayChildren.add(resultDisplayRoot);

        Path addressBookFilePath = logic.getAddressBookFilePath();
        StatusBarFooter statusBarFooter = new StatusBarFooter(addressBookFilePath);
        ObservableList<Node> statusbarChildren = statusbarPlaceholder.getChildren();
        Region statusBarFooterRoot = statusBarFooter.getRoot();
        statusbarChildren.add(statusBarFooterRoot);

        Model model = logic.getModel();
        CommandBox commandBox = new CommandBox(this::executeCommand, model);
        ObservableList<Node> commandBoxChildren = commandBoxPlaceholder.getChildren();
        Region commandBoxRoot = commandBox.getRoot();
        commandBoxChildren.add(commandBoxRoot);

        noteEditView = new NoteEditView();

        setupKeyboardShortcuts();
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        double windowHeight = guiSettings.getWindowHeight();
        double windowWidth = guiSettings.getWindowWidth();
        primaryStage.setHeight(windowHeight);
        primaryStage.setWidth(windowWidth);
        Point windowCoordinates = guiSettings.getWindowCoordinates();
        if (windowCoordinates != null) {
            double windowX = windowCoordinates.getX();
            double windowY = windowCoordinates.getY();
            primaryStage.setX(windowX);
            primaryStage.setY(windowY);
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        double stageWidth = primaryStage.getWidth();
        double stageHeight = primaryStage.getHeight();
        int stageX = (int) primaryStage.getX();
        int stageY = (int) primaryStage.getY();
        GuiSettings guiSettings = new GuiSettings(stageWidth, stageHeight, stageX, stageY);
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    public PersonListPanel getPersonListPanel() {
        return personListPanel;
    }

    public ReminderListPanel getReminderListPanel() {
        return reminderListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            String feedbackToUser = commandResult.getFeedbackToUser();
            logger.info("Result: " + feedbackToUser);
            resultDisplay.setFeedbackToUser(feedbackToUser);

            boolean shouldShowHelp = commandResult.isShowHelp();
            if (shouldShowHelp) {
                handleHelp();
            }

            boolean shouldExit = commandResult.isExit();
            if (shouldExit) {
                handleExit();
            }

            boolean shouldShowNoteEdit = commandResult.isShowNoteEdit();
            if (shouldShowNoteEdit) {
                Index targetPersonIndex = commandResult.getTargetPersonIndex();
                showNoteEditView(targetPersonIndex);
            } else if (isNoteEditMode && commandText.trim().equals("list")) {
                showPersonListView();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            String errorMessage = e.getMessage();
            resultDisplay.setFeedbackToUser(errorMessage);
            throw e;
        }
    }

    private void showNoteEditView(Index personIndex) {
        ObservableList<Person> filteredPersonList = logic.getFilteredPersonList();
        int personZeroBasedIndex = personIndex.getZeroBased();
        Person targetPerson = filteredPersonList.get(personZeroBasedIndex);
        contactsHeader.setVisible(false);
        contactsHeader.setManaged(false);
        ObservableList<Node> personListPanelChildren = personListPanelPlaceholder.getChildren();
        personListPanelChildren.clear();
        Region noteEditViewRoot = noteEditView.getRoot();
        personListPanelChildren.add(noteEditViewRoot);
        noteEditView.setPerson(targetPerson);
        noteEditView.requestFocus();
        isNoteEditMode = true;
        headerLabel.setText("Notes");
    }

    /**
     * Shows the person list view.
     */
    private void showPersonListView() {
        Person currentPerson = noteEditView.getCurrentPerson();
        if (isNoteEditMode && currentPerson != null) {
            saveCurrentNote();
        }

        // Show the contacts header when in list view mode
        contactsHeader.setVisible(true);
        contactsHeader.setManaged(true);

        ObservableList<Node> personListPanelChildren = personListPanelPlaceholder.getChildren();
        personListPanelChildren.clear();
        Region personListPanelRoot = personListPanel.getRoot();
        personListPanelChildren.add(personListPanelRoot);
        isNoteEditMode = false;
        headerLabel.setText("Contacts");
    }

    /**
     * Saves the current note content to the person.
     */
    private void saveCurrentNote() {
        Person currentPerson = noteEditView.getCurrentPerson();
        if (currentPerson == null) {
            return;
        }
        try {
            saveNoteForPerson(currentPerson);
        } catch (Exception e) {
            handleNoteSaveError(e);
        }
    }

    /**
     * Saves the note for the specified person.
     */
    private void saveNoteForPerson(Person person) throws CommandException {
        String content = noteEditView.getNoteContent();
        Note note = new Note(content);
        Person personToUpdate = findPersonInFilteredList(person);
        logic.setPersonNote(personToUpdate, note);
        Name personName = personToUpdate.getName();
        resultDisplay.setFeedbackToUser("Note saved for " + personName);
    }

    /**
     * Finds the person in the filtered list that matches the given person.
     * Returns the given person if not found in the list.
     */
    private Person findPersonInFilteredList(Person person) {
        ObservableList<Person> filteredPersonList = logic.getFilteredPersonList();
        for (Person p : filteredPersonList) {
            boolean isSamePerson = p.isSamePerson(person);
            if (isSamePerson) {
                return p;
            }
        }
        return person;
    }

    /**
     * Handles errors that occur during note saving.
     */
    private void handleNoteSaveError(Exception e) {
        String errorMessage = e.getMessage();
        resultDisplay.setFeedbackToUser("Error saving note: " + errorMessage);
    }

    /**
     * Sets up keyboard shortcuts.
     */
    private void setupKeyboardShortcuts() {
        Scene primaryScene = primaryStage.getScene();
        primaryScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            handleEscapeKeyPress(event);
        });
    }

    /**
     * Handles the Escape key press event.
     */
    private void handleEscapeKeyPress(KeyEvent event) {
        KeyCode eventCode = event.getCode();
        boolean isEscapePressed = eventCode == KeyCode.ESCAPE;
        if (!isEscapePressed || !isNoteEditMode) {
            return;
        }
        boolean isTextAreaFocused = noteEditView.isTextAreaFocused();
        if (isTextAreaFocused) {
            handleEscapeFromTextArea();
        } else {
            noteEditView.requestFocus();
        }
        event.consume();
    }

    /**
     * Handles Escape key press when text area is focused.
     * Saves the current note and shifts focus to the command box.
     */
    private void handleEscapeFromTextArea() {
        saveCurrentNote();
        focusCommandTextField();
    }

    /**
     * Focuses on the command text field.
     */
    private void focusCommandTextField() {
        ObservableList<Node> commandBoxChildren = commandBoxPlaceholder.getChildren();
        Node commandBoxRoot = commandBoxChildren.get(0);
        if (!(commandBoxRoot instanceof Region)) {
            return;
        }
        Parent commandBoxParent = (Parent) commandBoxRoot;
        TextField commandTextField = (TextField) commandBoxParent.lookup("#commandTextField");
        if (commandTextField != null) {
            commandTextField.requestFocus();
        }
    }
}

