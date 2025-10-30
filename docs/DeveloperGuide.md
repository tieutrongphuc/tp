---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# AcademeConnect Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

<br>
The UI color palette was taken from the Notion application <br>
--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Note: When a person with reminders is deleted, all associated reminders are automatically deleted to maintain referential integrity.

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` and `Reminder` objects residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object) and all `Reminder` objects (which are contained in a `UniqueReminderList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores the currently 'selected' `Reminder` objects (e.g., upcoming reminders) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Reminder>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user's preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data, user preference data, and reminder data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage`, `UserPrefStorage` and `ReminderStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

--------------------------------------------------------------------------------------------------------------------

## **Implemented Features**

This section describes noteworthy details on how certain implemented features work.

### Tag Autocomplete Feature

#### Implementation

The tag autocomplete feature provides intelligent tag suggestions as users type in the command box, helping them quickly reuse existing tags without having to remember exact tag names.

**Key Components:**
* `CommandBox` - Main UI component that handles user input and manages suggestion display
* `suggestionText` - JavaFX Text node that displays the autocomplete suggestion
* `TAG_PREFIXES` - Array of supported tag prefixes (`t/`)
* `Model` - Provides access to all existing tags in the address book

**How it works:**

The autocomplete system monitors text input in real-time and follows this workflow:

1. **Input Detection**: `CommandBox.handleTextChanged()` is triggered on every keystroke and caret position change
2. **Prefix Recognition**: Checks if the current caret position is immediately after a tag prefix (`t/`)
3. **Partial Tag Extraction**: Extracts the partially-typed tag text between the prefix and the caret
4. **Tag Matching**: Queries all existing tags from the address book and filters for case-insensitive matches
5. **Suggestion Display**: Shows the remaining untyped portion of the best matching tag in blue text
6. **Acceptance**: User presses Tab to accept the suggestion, which completes the tag and moves the caret

**Code Flow Example:**

When a user types `tag 1 t/fri`, the system:
1. Detects `t/` prefix at position before caret
2. Extracts partial tag `"fri"`
3. Finds matching tag `"friend"` in the address book
4. Displays `"end"` in blue after the typed text
5. On Tab press, replaces `"fri"` with `"friend"` and positions caret after it

**Design Considerations:**

* **Suggestion Visibility**: Suggestions only appear when the caret is positioned after a tag prefix and no space has been typed yet. This prevents suggestions from appearing in inappropriate contexts.

* **Tab Key Handling**: The Tab key is consumed to prevent focus traversal (which would move focus away from the command box). This ensures a smooth user experience where Tab always means "accept suggestion".

* **Performance**: Tag queries are performed on every keystroke. For typical use cases (up to 1000 contacts with ~5-10 tags each), this remains performant. The implementation uses Java streams for efficient filtering.

* **Case Insensitivity**: Matching is case-insensitive to maximize user convenience. A user typing `ai` will match both `AI` and `ai`.

<br>
<br>

### Note Feature

#### Implementation

The note feature allows users to open a dedicated note editor for any contact in the address book, enabling them to add
or edit detailed notes. The editor provides a full-screen text area with a 5000-character limit and keyboard-based save
functionality via the Esc key.

**Key Components:**
* `NoteCommand` - Command class that initiates note editing mode for a specified person
* `NoteCommandParser` - Parser that validates and extracts the person index from user input
* `NoteEditView` - JavaFX UI component that displays the note editor with a TextArea
* `CommandResult` - Extended to include `showNoteEdit` flag and `targetPersonIndex` for note editing mode
* `MainWindow` - Manages view switching between person list and note editor, handles Esc key detection and save logic
* `Logic` - Interface extended with `setPersonNote()` method for persisting notes to storage

**How it works:**

The note feature orchestrates multiple components across the UI, Logic, and Model layers following this workflow:

1. **Command Parsing**: `AddressBookParser` routes "note" commands to `NoteCommandParser`
2. **Index Validation**: Parser extracts and validates the person index using `ParserUtil.parseIndex()`
3. **Command Execution**: `NoteCommand.execute()` retrieves the target person and returns a `CommandResult` with
`showNoteEdit=true` and the `targetPersonIndex`
4. **View Switching**: `MainWindow` detects the `showNoteEdit` flag and calls `showNoteEditView()`
5. **Note Loading**: `NoteEditView.setPerson()` loads the person's existing note (if any) into the TextArea and
positions the caret at the end
6. **User Editing**: User types in the TextArea, with a listener preventing input beyond 5000 characters
7. **Esc Key Handling**: When user presses Esc while in note edit mode:
   - First press: If TextArea is focused, saves the note via `saveCurrentNote()` and shifts focus to command box
   - Second press: If command box is focused, returns focus to TextArea
8. **Note Persistence**: `saveCurrentNote()` retrieves content via `noteEditView.getNoteContent()` and calls
`logic.setPersonNote()` to update the model and storage

**Code Flow Example:**

When a user types `note 1` and edits a note, the system:
1. `AddressBookParser` creates `NoteCommandParser` and calls `parse("1")`
2. Parser extracts index `1` and creates `NoteCommand(Index.fromOneBased(1))`
3. `NoteCommand.execute()` retrieves Person at index 1 from the filtered list
4. Returns `CommandResult` with `showNoteEdit=true` and `targetPersonIndex=1`

[NoteSequenceDiagram.puml](diagrams/NoteSequenceDiagram.puml)

5. `MainWindow.executeCommand()` detects the `showNoteEdit` flag
6. Calls `showNoteEditView(Index.fromOneBased(1))` which:
   - Retrieves the Person from the filtered list
   - Clears the person list panel and adds `NoteEditView` OR Calls `noteEditView.setPerson(person)` to load existing note content
   - Requests focus on the TextArea
   - Sets `isNoteEditMode = true` and updates header to "Notes"

[NoteSequenceDiagram2.puml](diagrams/NoteSequenceDiagram2.puml)

7. User edits the note text (character limit enforced by TextArea listener)
8. When user presses Esc key:
   - `MainWindow.handleEscapeKeyPress()` is triggered by the keyboard event filter
   - Checks if `isNoteEditMode=true` and consumes the event
   - Calls `handleEscapeFromTextArea()` which:
     - Invokes `saveCurrentNote()` to retrieve note content via `noteEditView.getNoteContent()`
     - Creates new `Note` object
     - Calls `logic.setPersonNote(person, note)` which updates model and saves to storage
     - Shifts focus to command box via `focusCommandTextField()`

[NoteSequenceDiagram3.puml](diagrams/NoteSequenceDiagram3.puml)

9. User can press Esc again to return focus to TextArea, or execute any command to exit note edit mode

**Design Considerations:**

* **Esc Key Save Mechanism**: The implementation uses the Esc key to trigger saving, which provides a quick
keyboard-based workflow for CLI-focused users. The two-state behavior (save and shift focus on first press, return 
focus on second press) allows users to quickly switch between editing notes and entering commands without leaving note
edit mode. This is more efficient than requiring users to execute another command to save and exit.

* **Auto-Save vs Manual Save**: Notes are only saved when the user explicitly presses Esc, giving users control over
when their edits are persisted. This prevents unnecessary disk writes on every keystroke while still providing a quick
save mechanism. However, this means users must remember to press Esc before closing the application, as unsaved notes
will be lost.

* **Character Limit Enforcement**: The 5000 character limit is enforced in real-time by a JavaFX listener on the
TextArea's text property. When new text would exceed the limit, the listener reverts to the old value, providing
immediate feedback. This approach prevents users from typing beyond the limit rather than showing an error after the
fact.

* **Caret Positioning**: When opening an existing note, the caret is positioned at the end of the text rather than
the beginning. This allows users to quickly append to existing notes, which is the most common use case.



--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* need to manage a significant network of professional contacts in their research field
* want to track detailed information about fellow researchers, collaborators, and potential research partners
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* are reasonably comfortable using CLI apps
* need to organize contacts by research interests, publications, and professional relationships

**Value proposition**: AcademeConnect solves the problem of managing a fragmented research network by providing a 
centralized hub to organize contacts, track professional relationships, and find collaborators faster than a typical 
mouse/GUI driven app. It helps researchers effortlessly manage their academic workflow by maintaining key details about 
each contact including research specialties, conference interactions, and collaboration history.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                            | I want to …​                                              | So that I can…​                                                           |
|----------|------------------------------------|-----------------------------------------------------------|---------------------------------------------------------------------------|
| `* * *`  | user                               | use the CLI to add a contact                              | keep track of contacts in the list and their associated details           |
| `* * *`  | user                               | delete a contact                                          | keep the contacts list updated and clutter-free                           |
| `* * *`  | user                               | view a list of contacts                                   | quickly see the most important information about each contact at a glance |
| `* * *`  | user                               | edit a contact in place                                   | update information about my network efficiently                           |
| `* * *`  | user with a large list of contacts | quickly search for a specific person by name              | access their information without scrolling through my entire list         |
| `* *`    | user                               | add custom tags to a contact's profile                    | categorize them by their specific research interests or specialties       |
| `* *`    | user                               | add detailed notes about my interactions with a contact   | remember the context of our conversations and refer to them later         |
| `* *`    | user looking for a collaborator    | search for contacts based on their research interest tags | easily find people with relevant expertise                                |
| `* *`    | new user                           | follow a guided tour of the basic features                | quickly learn how to use the app without having to read a manual          |
| `* *`    | experienced user                   | use shortcut commands                                     | perform common tasks like adding a contact or searching more efficiently  |
| `* *`    | researcher                         | link contacts to specific publications                    | track who collaborated on which research                                  |
| `*`      | user with a strong network         | visualize the connections between my contacts             | see who knows whom                                                        |
| `*`      | long-term user                     | archive contacts that are no longer active                | keep my primary contact list clutter-free and relevant                    |
| `*`      | user with a large list of contacts | set reminders for conversational follow-ups               | not lose touch with important contacts                                    |
| `*`      | user                               | get notified about any contact's recent activity          | stay up to date with my network's contributions                           |
| `*`      | user looking for collaborators     | receive suggestions to collaborate with new researchers   | make it easier to network with unknown researchers                        |
| `*`      | researcher                         | connect contacts to grant applications                    | see their roles in funding projects                                       |
| `*`      | researcher                         | log which conferences I attended with a contact           | remember where we met                                                     |
| `*`      | researcher                         | view a timeline of interactions with a contact            | recall the history of our collaboration                                   |

### Use cases

(For all use cases below, the **System** is the `AcademeConnect` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Add a contact with research interest tags**

**MSS**

1. User requests to add a new contact with basic details.
2. AcademeConnect prompts for research interest tags.
3. User enters one or more research interest tags.
4. AcademeConnect adds the contact with the specified tags and displays a success message.

    Use case ends.

**Extensions**

* 1a. User enters invalid or incomplete contact details
    * 1a1. AcademeConnect shows an error message indicating which field is invalid.
    * 1a2. User enters corrected details. Steps 1a1-1a2 are repeated until the data entered is correct.

      Use case resumes from step 2.

* 3a. User enters no tags
    * 3a1. AcademeConnect adds the contact without tags.
  
      Use case ends.
  
* *a. At any time, User chooses to cancel adding the contact
    * *a1. AcademeConnect discards the input and returns to the main screen.

      Use case ends.


**Use case: UC02 - Search for potential collaborators by research interest**

**MSS**

1. User requests to search for contacts by research interest tag.
2. AcademeConnect requests for the specific research interest tag.
3. User enters the research interest tag.
4. AcademeConnect displays a list of contacts with matching research interest tags.

   Use case ends.

**Extensions**

* 3a. The entered tag does not match any existing contacts
    * 3a1. AcademeConnect displays a message indicating no matches found.

      Use case ends.
  
* 3b. User enters multiple tags.
    * 3b1. AcademeConnect displays contacts matching any of the entered tags.
  
      Use case resumes from step 4.

* 4a. User requests to view detailed information about a specific contact from the results.
    * 4a1. AcademeConnect displays the full contact details including all tags and notes.
  
      Use case ends.

**Use case: UC03 - Delete a contact**

**MSS**

1. User requests to list contacts.
2. AcademeConnect shows a list of contacts.
3. User requests to delete a specific contact in the list.
4. AcademeConnect deletes the contact.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.
    * 3a1. AcademeConnect shows an error message.

      Use case resumes at step 2.

**Use case: UC04 - Add or edit notes for a contact**

**MSS**

1. User requests to list contacts.
2. AcademeConnect shows a list of contacts.
3. User requests to open the note editor for a specific contact.
4. AcademeConnect displays the note editor with any existing note content.
5. User types or edits the note content in the text area.
6. User presses Esc key to save the note.
7. AcademeConnect saves the note and shifts focus to the command box.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.
    * 3a1. AcademeConnect shows an error message.

      Use case resumes at step 2.

* 5a. User types more than 5000 characters.
    * 5a1. AcademeConnect prevents additional characters from being entered.
    * 5a2. User edits the content to stay within the limit.

      Use case resumes at step 6.

* 6a. User presses Esc key again while command box is focused.
    * 6a1. AcademeConnect returns focus to the note text area.
    * 6a2. User continues editing the note.

      Use case resumes at step 5.


**Use case: UC05 - View notes for a contact**

**MSS**

1. User requests to list contacts.
2. AcademeConnect shows a list of contacts.
3. User requests to open the note editor for a specific contact.
4. AcademeConnect displays the note editor with the existing note content for that contact.
5. User reads the note content.
6. User presses Esc key to return focus to command box.
7. User executes a command to exit note edit mode.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.
    * 3a1. AcademeConnect shows an error message.

      Use case resumes at step 2.

* 4a. The contact has no existing note.
    * 4a1. AcademeConnect displays an empty note editor.

      Use case ends.


### Non-Functional Requirements

1. Should work on any mainstream OS as long as it has Java 17 or above installed.
2. Should be able to hold up to 1000 contacts without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. The system should not lose any contact data during normal operation. All changes should be automatically saved to prevent data loss.
5. New users should be able to add their first contact within 5 minutes of opening the application.
6. User documentation should be comprehensive enough for a novice computer user to understand all basic features.
7. The command-line interface should support standard keyboard shortcuts and provide clear error messages for all user actions.
8. Contact information should be stored locally on the user's machine. The system should not transmit any contact data to external servers without explicit user consent.


*{More to be added}*

### Glossary

* **Academic Researcher**: A person engaged in scholarly research, typically affiliated with a university or research 
institution, who needs to maintain professional contacts in their field.
* **CLI (Command Line Interface)**: A text-based interface where users interact with the application by typing commands rather than using a mouse or graphical elements.
* **Contact**: An entry in AcademeConnect representing a fellow researcher or professional connection, containing details such as name, affiliation, research interests, and interaction notes.
* **Research Interest Tag**: A keyword or label (e.g., #AI ethics, #quantum computing) assigned to a contact to categorize their area of research specialization, enabling quick filtering and search.
* **MSS (Main Success Scenario)**: The most common path of execution in a use case, describing the typical flow of events when everything proceeds without errors.
* **Extensions**: Alternative paths in a use case that handle errors, special conditions, or optional flows that deviate from the main success scenario.
* **Archived Contact**: A contact that is no longer active in the user's primary network but is retained in the system for historical reference.
* **Custom List**: A user-defined grouping of contacts (e.g., "AI collaborators", "Grant reviewers") for flexible organization beyond research interest tags.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
