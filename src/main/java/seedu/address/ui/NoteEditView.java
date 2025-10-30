package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;

/**
 * A UI component for editing notes.
 */
public class NoteEditView extends UiPart<Region> {
    private static final String FXML = "NoteEditView.fxml";
    private static final int MAX_CHARS = 5000;

    @FXML
    private TextArea noteTextArea;

    private Person currentPerson;

    /**
     * Constructs a NoteEditView and sets up the character limit listener.
     */
    public NoteEditView() {
        super(FXML);

        noteTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > MAX_CHARS) {
                noteTextArea.setText(oldValue);
            }
        });
    }

    public void setPerson(Person person) {
        this.currentPerson = person;

        Note personNote = person.getNote();
        if (personNote != null) {
            String noteText = personNote.toString();
            noteTextArea.setText(noteText);
            
            String textAreaContent = noteTextArea.getText();
            int caretPosition = textAreaContent.length();
            noteTextArea.positionCaret(caretPosition);
        } else {
            noteTextArea.clear();
        }
    }

    public String getNoteContent() {
        return noteTextArea.getText();
    }

    public void requestFocus() {
        noteTextArea.requestFocus();
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public boolean isTextAreaFocused() {
        return noteTextArea.isFocused();
    }
}
