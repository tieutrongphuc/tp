---
  layout: default.md
    title: "User Guide"
    pageNav: 3
---

# AcademeConnect User Guide

## Introduction

Purpose: This User Guide explains how to install, run and use AcademeConnect (AC) — a desktop contact manager
optimized for academic researchers who prefer a Command Line Interface (CLI) with an accompanying Graphical User
Interface (GUI). The guide is structured for quick lookup and step-by-step tasks.

**_Target users_**
- Graduate students, researchers, teaching assistants, and academics who manage contact/networks.
- Users who are comfortable with basic command-line operations (cd, java -jar) but prefer concise, repeatable CLI
  commands.

**_Assumptions about users_**
- Basic familiarity with Windows/macOS/Linux file navigation and opening a terminal.
- Java 17 (or newer) installed and available on the PATH.

How to use this guide
- A quick-start section shows installation and the shortest path to run the app.
- Features are organised by task with example commands and expected outputs.
- Use the page navigation at the top of the page to jump between sections. Internal anchors are provided for direct
  linking to commands and examples.

<page-nav-print />

--------------------------------------------------------------------------------------------------------------------


## Quick start

1. Ensure you have Java `17` or above installed on your computer.
    - Windows: Verify with `java -version` in PowerShell or Command Prompt. Expected output: `java version "17.0.x" ...`
    - macOS: follow the Java installation instructions [here]
      (https://se-education.org/guides/tutorials/javaInstallationMac.html).
    - Linux: Verify with `java -version`. Expected output: `java version "17.0.x" ...`

1. Download the latest `.jar` file (release) and save it to the folder you want to use as the _home folder_ for AC.

1. Open a command terminal, `cd` into the folder containing the jar file, and run:
   ```
   java -jar addressbook.jar
   ```
   Expected behaviour: A GUI window opens within a few seconds and the CLI input box is shown at the bottom of the
   window. Sample data may be present on first run.
![img.png](img.png)

1. Type commands into the command box and press Enter. Example:
    - `help` — opens the help window.
    - `list` — shows all contacts.
    - `add n/John Doe p/98765432 e/johnd@example.com a/NUS` — adds John Doe.
    - `exit` — exits the application.

### Tips

<box type="info" seamless><box>
- Use `list` to confirm the index numbers before using index-based commands like `delete`, `edit`, `note`, `viewNote`,
  and `tag`.
- Copy multi-line example commands into a plain-text editor first if you find line-breaks get removed when copying from
  a PDF.
  </box>

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless><box>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are parameters. e.g. `add n/NAME` → `add n/John Doe`.<br>
* Square brackets `[...]` denote optional items.<br>
* An ellipsis `…` after an item indicates it can appear multiple times (including zero).<br>
* Parameters can be in any order.<br>
* Extraneous parameters for no-argument commands (`help`, `list`, `exit`, `clear`) are ignored.<br>
* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple
  lines as space characters surrounding line-breaks may be omitted when copied over to the application.
  </box>


### Adding a person: `add`
Adds a person to the address book. <br>
**Format:** `add n/NAME [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​` <br>

**Examples**:
```
add n/John Doe # Adds a person with only a name
add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01
add n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/+441234567 t/criminal
add n/李明 p/+8613812345678 # Names can contain non-English characters
add n/Dr. Jane Smith-O'Connor # Names can contain special characters
```

**Notes:**
* Only the name field is required; all other fields are optional.
* The name can contain any characters (including special characters and numbers).
* Phone numbers can optionally start with a `+` for international numbers (e.g., `+6591234567`).

**Expected output on success:** `New person added: John Doe`

### Listing all persons : `list`
Shows a list of all persons in the address book.<br>
**Format:** `list` <br>
**Expected output on success:** `Listed all persons`

### Editing a person : `edit`
Edits an existing person in the address book.<br>
**Format:** `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br>

**Examples:**
```
edit 1 p/91234567 e/johndoe@example.com   # Edits the phone number and email address of the 1st person to be `91234567` 
                                          # and `johndoe@example.com` respectively.
                                          
edit 2 n/Betsy Crower t/   # Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.
```

**Notes:**
* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without specifying any tags after it.

### Adding tags to a person : `tag`
Adds one or more tags to an existing person in the address book.<br>
**Format:** `tag INDEX t/TAG [t/MORE_TAGS]…​`<br>

**Examples:**
```
tag 1 t/friend    # Adds the tag `friend` to the 1st person.
tag 2 t/colleague t/cs2103t   # Adds both `colleague` and `cs2103t` tags to the 2nd person.
```

**Notes:**
* Adds tag(s) to the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​
* Tags are added cumulatively - existing tags are preserved.
* Each tag must be a single word (no spaces allowed).
* You can add multiple tags in a single command.

### Adding or updating a note : `note`
Opens a note editor for the person at the given index in the currently displayed list. The editor is a text box overlay
(like a simple notepad) that replaces the person list area; the command box and result display remain. Use the editor
to type or edit the note, then press Esc to toggle back to the command box — toggling from the editor into the command
box saves the note to storage automatically.

**Format:** `note INDEX`<br>

**Examples:**
```
note 2   # opens the note editor for the 2nd person
```

**Expected output on success:** `Opening text editor for Person: John Doe`
![img_1.png](img_1.png)

**Behavior:**
- Running note INDEX opens the note editor pre-populated with the person’s existing note (if any).
- The application is typing-focused: press Esc to toggle typing focus between the command box and the note editor.
    - Pressing Esc while the editor is focused switches focus to the command box and saves the current editor content
      to disk.
    - Pressing Esc while the command box is focused returns focus to the note editor (no save).
- Navigating away (for example, running list) returns the UI to the person list. When you later reopen the same
  person’s note, the editor will show the saved content.

**Notes:**
* Editor supports text wrapping and vertical scrolling.
* Maximum note length: 5000 characters. Attempts to exceed this will be rejected by the editor.
* Opens the note editor of the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

### Viewing a note: `viewNote`
Displays a person's note in the result area (read-only). Refer to 'note' command to edit.

**Format:** `viewNote INDEX`<br>

**Expected output on success:** `Note for John Doe: This is the note content`

**Notes:**
* Displays the note of the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

### Deleting a note: `deleteNote`
Deletes a person's note.

**Format:** `deleteNote INDEX'<br>

**Expected output on success:** `Deleted note of Person: John Doe`

**Expected output on failure:** `No note available to delete of Person: John Doe`

**Notes:**
* Deletes the note of the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​


### Locating persons by name or tag: `find`

Finds persons whose names contain any of the given keywords. <br>
Alternatively, finds persons whose names are assigned any of the given tags.

**Format:** `find KEYWORD [MORE_KEYWORDS]` OR `find t/[TAG_NAME]`

**Examples:**
```
find John # returns `john` and `John Doe`
find alex david # returns `Alex Yeoh`, `David Li`<br>
find t/friends # returns everyone tagged as `friends`
find t/friends t/colleagues # returns everyone tagged as `friends` or `colleagues`
```
![result for 'find alex david'](images/findAlexDavidResult.png)

**Notes:**
* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only search by name or search by tag.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

### Deleting a person : `delete`

Deletes the specified person from the address book.

**Format:** `delete INDEX`

**Examples:**
```
delete 2    # deletes the 2nd person in the address book
```

**Notes:**
* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​


### Clearing all entries : `clear`

Clears all entries from the address book.

**Format:** `clear`

<box type="warning" seamless><box>
**This action is irreversible**
</box>

### Exiting the program : `exit`

Exits the program.

**Format:** `exit`

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

### Saving the data

Data is saved automatically after any command that modifies the address book. Data file location:
`[JAR file location]/data/addressbook.json`.

<box type="warning" seamless><box>
**Caution:** Incorrect manual edits to `addressbook.json` may corrupt the file and cause data loss on next run. Back
up before editing.
</box>

### Editing the data file

AddressBook data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are
welcome to update data directly by editing that data file.

<box type="warning" seamless><box>

**Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty
data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside
the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains
the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only
   the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the
   application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard
   shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy
   is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

| Action          | Format, Examples                                                                                                                                            |
|-----------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Add**         | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd t/friend t/colleague` |
| **Clear**       | `clear`                                                                                                                                                     |
| **Delete**      | `delete INDEX`<br> e.g., `delete 3`                                                                                                                         |
| **Edit**        | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`                                  |
| **Find**        | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`                                                                                                  |
| **List**        | `list`                                                                                                                                                      |
| **Help**        | `help`                                                                                                                                                      |
| **Tag**         | `tag INDEX t/TAG [t/MORE_TAGS]…`<br> e.g., `tag 1 t/friend t/colleague`                                                                                     |
| **Note**        | `note INDEX`                                                                                                                                                |
| **View Note**   | `viewNote INDEX`                                                                                                                                            |
| **Delete Note** | `deleteNote INDEX`                                                                                                                                          |
--------------------------------------------------------------------------------------------------------------------
