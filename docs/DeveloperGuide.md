---
layout: page
title: Developer Guide
---

- Table of Contents
  {:toc}

---

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

---

## Introduction

**ResiReg** is a productivity app designed to help OHS* admin at Residential Colleges (RCs)* in NUS with their daily tasks. **ResiReg** allows admin to allocate rooms to students, manage students records, generate billing and OHS reports, and export CSVs for easy reference and sharing.

**ResiReg** has the following main features:

1. Manage records of students.
2. Manage allocations of students to rooms in the College
3. Generate bills and log payments for RC-related services.
4. Export records of students, rooms or transactions to CSV files for easy reference and sharing.

## Purpose and Audience for this Guide

This Developer Guide specifies the architecture, design, implementation and user cases for **ResiReg**, as well as our considerations behind key design decisions.

It is intended for developers, software testers, open-source contrubitors and any like-minded persons who wish to contribute this project or gain deeper insights about **ResiReg**.

## Setting Up

Refer to the guide [here](./SettingUp.md).

## **Design**

### Architecture

<img src="images/ArchitectureDiagram.png" width="450" />

The **_Architecture Diagram_** given above explains the high-level design of the App. Given below is a quick overview of each component.

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.

</div>

**`Main`** has two classes called [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java). It is responsible for,

- At app launch: Initializes the components in the correct sequence, and connects them up with each other.
- At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

- [**`UI`**](#ui-component): The UI of the App.
- [**`Logic`**](#logic-component): The command executor.
- [**`Model`**](#model-component): Holds the data of the App in memory.
- [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

Each of the four components,

- defines its _API_ in an `interface` with the same name as the Component.
- exposes its functionality using a concrete `{Component Name}Manager` class (which implements the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component (see the class diagram given below) defines its API in the `Logic.java` interface and exposes its functionality using the `LogicManager.java` class which implements the `Logic` interface.

![Class Diagram of the Logic Component](images/LogicClassDiagram.png)

**How the architecture components interact with each other**

The _Sequence Diagram_ below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

The sections below give more details of each component.

### UI component

![Structure of the UI Component](images/UiClassDiagram.png)

**API** :
[`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class.

The `UI` component uses JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

- Executes user commands using the `Logic` component.
- Listens for changes to `Model` data so that the UI can be updated with the modified data.

### Logic component

![Structure of the Logic Component](images/LogicClassDiagram.png)

**API** :
[`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

1. `Logic` uses the `ResiRegParser` class to parse the user command.
1. This results in a `Command` object which is executed by the `LogicManager`.
1. The command execution can affect the `Model` (e.g. adding a person).
1. The result of the command execution is encapsulated as a `CommandResult` object which is passed back to the `Ui`.
1. In addition, the `CommandResult` object can also instruct the `Ui` to perform certain actions, such as displaying help to the user.

Given below is the Sequence Diagram for interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

### Model component

![Structure of the Model Component](images/ModelClassDiagram.png)

**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

The `Model`,

- stores a `UserPref` object that represents the user’s preferences.
- stores the residence regulation data.
- exposes an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
- does not depend on any of the other three components.

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `ResiReg`, which `Person` references. This allows `ResiReg` to only require one `Tag` object per unique `Tag`, instead of each `Person` needing their own `Tag` object.<br>
![BetterModelClassDiagram](images/BetterModelClassDiagram.png)

</div>

### Storage component

![Structure of the Storage Component](images/StorageClassDiagram.png)

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

The `Storage` component,

- can save `UserPref` objects in json format and read it back.
- can save the residence regulation data in json format and read it back.

### Common classes

Classes used by multiple components are in the `seedu.resireg.commons` package.

---

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedResiReg`. It extends `ResiReg` with an undo/redo history, stored internally as an `resiRegStateList` and `currentStatePointer`. Additionally, it implements the following operations:

- `VersionedResiReg#commit()` — Saves the current residence regulation state in its history.
- `VersionedResiReg#undo()` — Restores the previous residence regulation state from its history.
- `VersionedResiReg#redo()` — Restores a previously undone residence regulation state from its history.

These operations are exposed in the `Model` interface as `Model#commitResiReg()`, `Model#undoResiReg()` and `Model#redoResiReg()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedResiReg` will be initialized with the initial residence regulation state, and the `currentStatePointer` pointing to that single residence regulation state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the residence regulation. The `delete` command calls `Model#commitResiReg()`, causing the modified state of the residence regulation after the `delete 5` command executes to be saved in the `resiRegStateList`, and the `currentStatePointer` is shifted to the newly inserted residence regulation state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitResiReg()`, causing another modified residence regulation state to be saved into the `resiRegStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitResiReg()`, so the residence regulation state will not be saved into the `resiRegStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoResiReg()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous residence regulation state, and restores the residence regulation to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial ResiReg state, then there are no previous ResiReg states to restore. The `undo` command uses `Model#canUndoResiReg()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoResiReg()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the residence regulation to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `resiRegStateList.size() - 1`, pointing to the latest residence regulation state, then there are no undone ResiReg states to restore. The `redo` command uses `Model#canRedoResiReg()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the residence regulation, such as `list`, will usually not call `Model#commitResiReg()`, `Model#undoResiReg()` or `Model#redoResiReg()`. Thus, the `resiRegStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitResiReg()`. Since the `currentStatePointer` is not pointing at the end of the `resiRegStateList`, all residence regulation states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

![CommitActivityDiagram](images/CommitActivityDiagram.png)

#### Design consideration:

##### Aspect: How undo & redo executes

- **Alternative 1 (current choice):** Saves the entire ResiReg.

  - Pros: Easy to implement.
  - Cons: May have performance issues in terms of memory usage.

- **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  - Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  - Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

---

## **Documentation, logging, testing, configuration, dev-ops**

- [Documentation guide](Documentation.md)
- [Testing guide](Testing.md)
- [Logging guide](Logging.md)
- [Configuration guide](Configuration.md)
- [DevOps guide](DevOps.md)

---

## **Appendix: Requirements**

### Product scope

**Target user profile**:

- an OHS admin at a Residential College\* (in NUS)
- has a need to manage a large number of students and rooms (>800)
- dissatisfied with current MS Excel and paper-based workflow
- prefer desktop apps over other types
- can type fast
- prefers typing to mouse interactions
- is reasonably comfortable using CLI apps

**Value proposition**: manage students, room allocations and billing faster than a typical GUI app.

### User stories

Priorities: High (must have) - `☆ ☆ ☆`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a…                    | I can…                                                   | So that I can…                                                                                            |
| -------- | ------------------------ | -------------------------------------------------------- | --------------------------------------------------------------------------------------------------------- |
| \* \*    | Meticulous OHS admin     | have automatic backups of my data                        | rest knowing my data will not be accidentally erased.                                                     |
| \* \*    | OHS admin                | generate audit reports for financial data                | comply with internal audits of the Residential College.                                                   |
| \* \*    | OHS admin                | export records to csv files                              | easily create mailing lists or send relevant data to other admin.                                         |
| \* \* \* | New/Confused User        | check the syntax for a command                           | do a task even if I am unsure of the command usage.                                                       |
| \* \* \* | First Time User          | ask for help                                             | quickly and easily learn how to use the application in one place.                                         |
| \* \* \* | OHS Admin                | view a list of all students                              | check which students are in the system and access their particulars.                                      |
| \* \*    | As as skeptical GUI user | create aliases to other commands                         | perform my common actions while typing less.                                                              |
| \*       | OHS admin                | find a room by searching for the room number             | get the details of a specific room, without getting cluttered by other information.                       |
| \* \*    | OHS Admin                | view a list of rooms filtered by a particular type       | select the rooms that needs to be upgraded, for example.                                                  |
| \* \*    | OHS admin                | delete a bill                                            | remove a erroneously added bill.                                                                          |
| \* \*    | OHS admin                | mark a bill as paid                                      | easily keep track of the remaining amount a student has to pay to OHS.                                    |
| \* \*    | OHS admin                | view a list of all students with outstanding bills       | remind students of outstanding payments.                                                                  |
| \* \*    | skeptical GUI user       | have autocompletions for a command                       | quickly and efficiently complete an operation.                                                            |
| \* \*    | skeptical GUI user       | redo the previous command using a keyboard shortcut      | do the same task without typing again, e.g. if two students wish to pay the same bill.                    |
| \* \*    | Busy OHS Admin           | find a student by partial searching for their first name | type quickly without worrying about typos.                                                                |
| \* \*    | Skeptical GUI user       | undo my last command                                     | fix any change that I made erroneously.                                                                   |
| \* \*    | skeptical GUI user       | view previous commands using a keyboard shortcut         | check if I made an error in adding or deleting records.                                                   |
| \* \*    | OHS admin                | edit the bill amount                                     | ensure that changes in the billing amounts due to changes in university policies can be reflected.        |
| \* \*    | OHS admin                | view all outstanding bills for a student                 | inform the student of his/her due bills.                                                                  |
| \* \*    | OHS admin                | update a Semester name                                   | correct typos in the semester name.                                                                       |
| \* \*    | OHS admin                | add a bill for a student                                 | keep track of a student's bills and finances.                                                             |
| \* \* \* | OHS admin                | view a list of vacant rooms                              | start assigning rooms to students before the semester starts.                                             |
| \* \*    | OHS admin                | archive the current Semester's data                      | keep the data for auditing purposes, but not hvae it distract me while dealing with the current semester. |
| \* \* \* | OHS admin                | view a room allocation for a student                     | check and inform a student of their room allocation during check in.                                      |
| \* \* \* | OHS admin                | allocate a room to a student                             | allocate a student to a room before the semester starts.                                                  |
| \* \* \* | OHS admin                | delete a room allocation for a student                   | update vacancies when a student applies to leave their room.                                              |
| \* \*    | OHS admin                | add a remark to a bill                                   | record any exceptional details about the bill (e.g. cash-only payment).                                   |
| \* \*    | OHS admin                | add a new semester                                       | make sure all new bills and allocations are made in the context of the semester.                          |
| \* \* \* | OHS admin                | view a list of all allocated rooms                       | check which students stay in which rooms.                                                                 |
| \* \* \* | OHS admin                | edit a room allocation for a student                     | change a student's room allocation and update the room vacancies.                                         |
| \*       | OHS admin                | edit a room's type                                       | log upgrades like the installation of an aircon.                                                          |
| \* \*    | OHS admin                | export all of the current data to a data-file            | hand over my duties to another admin.                                                                     |
| \*       | OHS admin                | edit a room's semesterly fees                            | update room charges when costs increase (e.g. from $1000 to $1500)                                        |
| \* \*    | OHS admin                | import data from a data file                             | continue work from where my predecessor left off.                                                         |
| \* \* \* | OHS admin                | add a student to ResiReg                                 | perform admin duties related to the student.                                                              |
| \* \* \* | OHS admin                | edit the details of an existing student                  | easily correct any typos and update the student details in response to changes (e.g. faculty).            |
| \* \* \* | OHS admin                | delete a student                                         | so that I can remove an erroneously added student.                                                        |

_{More to be added}_

### Use cases

(For all use cases below, the **System** is `ResiReg` and the **Actor** an `OHS admin`, unless specified otherwise)

#### Use case: UC01 - Add a student

**MSS**

1. OHS admin requests to add a student and supplies student details.
1. ResiReg adds the student and saves the changes.

Use case ends.

**Extensions**

- 1a. Student details are missing or invalid, or there is already a student with the same matriculation number.
    - ResiReg shows an error message.

      Use case starts over.

#### Use case: UC02 - Delete a student

**MSS**

1. OHS admin requests to list students.
1. ResiReg shows a list of students.
1. OHS admin requests to delete a specified student from the list.
1. ResiReg deletes the specified student and saves the changes.

Use case ends.

**Extensions**

- 1a. The list of students is empty.
  
  Use case ends.
  
- 3a. The specified student does not exist.
  - ResiReg shows an error message.
  
    Use case resumes at step 2.

#### Use case: UC03 - Edit a student

**MSS**

1. OHS admin requests to list students.
1. ResiReg shows a list of students.
1. OHS admin requests to edit a specific student from the list and supplies details to edit.
1. ResiReg edits the student and saves the changes.

Use case ends.

**Extensions**

- 1a. The list of students is empty.

  Use case ends.
  
- 3a. The specified student does not exist or the supplied details are invalid.
  - ResiReg shows an error message.
  
    Use case resumes at step 2.

#### Use case: UC04 - Allocate a room to a student

**MSS**

1. OHS admin requests to list students without a room allocation and list vacant rooms.
1. ResiReg shows a list of students without a room allocation and a list of vacant rooms.
1. OHS admin requests to allocate a particular student to a particular room.
1. ResiReg adds the room allocation and saves the changes.

Use case ends.

**Extensions**

- 3a. Student belongs to an existing room allocation, room belongs to an existing room allocation, room does not exist or student does not exist.
    - ResiReg shows an error message.

    Use case resumes at step 2.

#### Use case: UC05 - Delete a room allocation for a student

**MSS**

1. OHS admin requests to list room allocations.
1. ResiReg shows a list of room allocations.
1. OHS admin requests to delete a specific room allocation.
1. ResiReg removes the room allocation and saves the changes. The room and student are not modified.
 
 Use case ends.

**Extensions**

- 1a. The list of room allocations is empty.

    Use case ends.

- 3a. Room allocation does not exist.
    - ResiReg shows an error message.

      Use case resumes at step 2.
      
#### Use case: UC06 - Edit an existing room allocation

**MSS**

1. OHS admin requests to list room allocations.
1. ResiReg shows a list of room allocations.
1. OHS admin requests to edit a specific room allocation from the list and supplies details to update.
1. ResiReg updates the room allocation and saves the changes.

Use case ends.

**Extensions**

- 1a. The list of room allocations is empty.

  Use case ends.

- 3a. Room allocation does not exist or details supplied are invalid.
    - ResiReg shows an error message.

      Use case resumes at step 2.

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. Should be simple to pick up for OHS admin (to incentivize them to migrate from Excel+paper-based workflow).
3. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
4. Should automatically backup frequently to avoid loss of sensitive data.
5. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
6. The codebase should be maintainable so that developers can easily add new features to the project

### Glossary

- **Mainstream OS**: Windows, Linux, Unix, OS-X
- **OHS**: Office of Housing Services
- **OHS Admin**: An employee of the OHS who works at a Residential College
- **Residential College**: A university residence for students that offers a 2-year program.
- **Check-in**: Exercise conducted at the beginning of the semester (in Week 0), where a student is informed of his room allocation.
- **Outstanding bill**: A bill due to be paid by a student.

---

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

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
