package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private Label reminderCount;
    @FXML
    private Label nextReminder;

    /**
     * Creates a {@code PersonCard} with the given {@code PersonCardData}.
     */
    public PersonCard(PersonCardData cardData) {
        super(FXML);
        this.person = cardData.getPerson();

        id.setText(cardData.getDisplayIndex() + ". ");
        name.setText(person.getName().fullName);

        // Prevent the index label from being truncated when name is too long
        id.setMinWidth(Region.USE_PREF_SIZE);
        id.setMaxWidth(Region.USE_PREF_SIZE);

        // Allow the name label to grow and take remaining space
        HBox.setHgrow(name, Priority.ALWAYS);
        name.setMaxWidth(Double.MAX_VALUE);

        String phoneValue = person.getPhone().value;
        if (phoneValue != null && !phoneValue.isEmpty()) {
            phone.setText("      📞  " + phoneValue);
        } else {
            phone.setVisible(false);
            phone.setManaged(false);
        }

        String addressValue = person.getAddress().value;
        if (addressValue != null && !addressValue.isEmpty()) {
            address.setText("      🏠  " + addressValue);
        } else {
            address.setVisible(false);
            address.setManaged(false);
        }

        String emailValue = person.getEmail().value;
        if (emailValue != null && !emailValue.isEmpty()) {
            email.setText("      \uD83D\uDCE7  " + emailValue);
        } else {
            email.setVisible(false);
            email.setManaged(false);
        }

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> {
                    Label tagLabel = new Label(tag.tagName);
                    tagLabel.setStyle("-fx-background-color: " + tag.getTagColour() + ";");
                    tags.getChildren().add(tagLabel);
                });

        // Display reminder information
        int count = cardData.getUpcomingReminderCount();
        if (count > 0) {
            reminderCount.setText("      🔔  " + count + (count == 1 ? " upcoming reminder" : " upcoming reminders"));
            reminderCount.setVisible(true);
            reminderCount.setManaged(true);
        } else {
            reminderCount.setText("");
            reminderCount.setVisible(false);
            reminderCount.setManaged(false);
        }

        String next = cardData.getNextReminderText();
        if (!next.isEmpty()) {
            nextReminder.setText("      ⏰  " + "Next: " + next);
            nextReminder.setVisible(true);
            nextReminder.setManaged(true);
        } else {
            nextReminder.setText("");
            nextReminder.setVisible(false);
            nextReminder.setManaged(false);
        }
    }
}
