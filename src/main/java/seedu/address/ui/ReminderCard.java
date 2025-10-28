package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.reminder.Reminder;

/**
 * A UI component that displays information of a {@code Reminder}.
 */
public class ReminderCard extends UiPart<Region> {

    private static final String FXML = "ReminderListCard.fxml";

    public final Reminder reminder;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label personName;
    @FXML
    private Label date;
    @FXML
    private Label message;

    /**
     * Creates a {@code ReminderCard} with the given {@code Reminder} and index to display.
     */
    public ReminderCard(Reminder reminder, int displayedIndex) {
        super(FXML);
        this.reminder = reminder;
        id.setText(displayedIndex + ". ");
        personName.setText(reminder.getPerson().getName().fullName);
        date.setText(reminder.getDate().toString());
        message.setText(reminder.getMessage().toString());
    }
}
