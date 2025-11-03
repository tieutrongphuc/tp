package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Reminder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.ReminderBuilder;

public class PersonCardTest extends ApplicationTest {

    private Person personWithAllDetails;
    private Person personWithMissingDetails;
    private ObservableList<Reminder> singleReminderList;
    private ObservableList<Reminder> multiReminderList;
    private ObservableList<Reminder> emptyReminderList;
    private Reminder reminder1;
    private Reminder reminder2;

    @BeforeEach
    public void setUp() {
        personWithAllDetails = new PersonBuilder(ALICE).withTags("friend", "colleague").build();
        personWithMissingDetails = new PersonBuilder().withName("Bob")
                .withPhone(null).withEmail(null).withAddress(null).build();

        reminder1 = new ReminderBuilder().withPerson(personWithAllDetails).withMessage("Review Chapter 1")
                .withDate("2026-12-25 10:00").build();
        reminder2 = new ReminderBuilder().withPerson(personWithAllDetails).withMessage("Submit report")
                .withDate("2026-12-30 18:00").build();

        singleReminderList = FXCollections.observableArrayList(reminder1);
        multiReminderList = FXCollections.observableArrayList(reminder1, reminder2);
        emptyReminderList = FXCollections.observableArrayList();
    }

    @Test
    public void display_personWithAllDetails_showsAllDetails() {
        PersonCardData cardData = new PersonCardData(personWithAllDetails, 1, multiReminderList);
        PersonCard personCard = new PersonCard(cardData);

        // Basic info
        assertEquals("1. ", ((Label) personCard.getRoot().lookup("#id")).getText());
        assertEquals("Alice Pauline", ((Label) personCard.getRoot().lookup("#name")).getText());

        // Contact info
        assertTrue(personCard.getRoot().lookup("#phone").isVisible());
        assertTrue(personCard.getRoot().lookup("#address").isVisible());
        assertTrue(personCard.getRoot().lookup("#email").isVisible());

        // Tags
        assertEquals(2, ((FlowPane) personCard.getRoot().lookup("#tags")).getChildren().size());

        // Reminder info (plural)
        Label reminderCountLabel = (Label) personCard.getRoot().lookup("#reminderCount");
        assertTrue(reminderCountLabel.isVisible());
        assertEquals("      🔔  2 upcoming reminders", reminderCountLabel.getText());

        Label nextReminderLabel = (Label) personCard.getRoot().lookup("#nextReminder");
        assertTrue(nextReminderLabel.isVisible());
        assertEquals("      ⏰  Next: Review Chapter 1 (2026-12-25 10:00)", nextReminderLabel.getText());
    }

    @Test
    public void display_personWithMissingDetails_hidesLabels() {
        PersonCardData cardData = new PersonCardData(personWithMissingDetails, 2, emptyReminderList);
        PersonCard personCard = new PersonCard(cardData);

        // Basic info
        assertEquals("2. ", ((Label) personCard.getRoot().lookup("#id")).getText());
        assertEquals("Bob", ((Label) personCard.getRoot().lookup("#name")).getText());

        // Contact info should be hidden
        assertFalse(personCard.getRoot().lookup("#phone").isVisible());
        assertFalse(personCard.getRoot().lookup("#address").isVisible());
        assertFalse(personCard.getRoot().lookup("#email").isVisible());

        // Tags pane should be empty
        assertEquals(0, ((FlowPane) personCard.getRoot().lookup("#tags")).getChildren().size());

        // Reminder info should be hidden
        assertFalse(personCard.getRoot().lookup("#reminderCount").isVisible());
        assertFalse(personCard.getRoot().lookup("#nextReminder").isVisible());
    }

    @Test
    public void display_singleUpcomingReminder_showsSingularText() {
        PersonCardData cardData = new PersonCardData(personWithAllDetails, 1, singleReminderList);
        PersonCard personCard = new PersonCard(cardData);

        Label reminderCountLabel = (Label) personCard.getRoot().lookup("#reminderCount");
        assertTrue(reminderCountLabel.isVisible());
        assertEquals("      🔔  1 upcoming reminder", reminderCountLabel.getText());
    }
}
