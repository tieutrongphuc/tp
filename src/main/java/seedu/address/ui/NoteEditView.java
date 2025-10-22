package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * A UI component for editing notes.
 */
public class NoteEditView extends UiPart<Region> {
    private static final String FXML = "NoteEditView.fxml";
    private static final int MAX_CHARS = 5000;

    @FXML
    private TextArea noteTextField;

    private Person currentPerson;

    public NoteEditView() {
        super(FXML);

        noteTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > MAX_CHARS) {
                noteTextField.setText(oldValue);
            }
        });
    }

    public void setPerson(Person person) {
        this.currentPerson = person;

        if (person.getNote() != null) {
            noteTextField.setText(person.getNote().toString());
        } else {
            noteTextField.clear();
        }
    }

    public String getNoteContent() {
        return noteTextField.getText();
    }

    public void requestFocus() {
        noteTextField.requestFocus();
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }
}
