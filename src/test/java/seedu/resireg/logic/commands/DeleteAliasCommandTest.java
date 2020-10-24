package seedu.resireg.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.resireg.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.resireg.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.resireg.testutil.TypicalCommandWordAliases.FIND_FI;
import static seedu.resireg.testutil.TypicalCommandWordAliases.ROOMS_R;
import static seedu.resireg.testutil.TypicalCommandWordAliases.ROOMS_RO;
import static seedu.resireg.testutil.TypicalCommandWordAliases.STUDENTS_STU;
import static seedu.resireg.testutil.TypicalCommandWordAliases.getTypicalUserPrefs;
import static seedu.resireg.testutil.TypicalStudents.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.resireg.model.Model;
import seedu.resireg.model.ModelManager;
import seedu.resireg.model.alias.CommandWordAlias;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteAliasCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalUserPrefs());

    @Test
    public void execute_validAlias_success() {
        CommandWordAlias aliasToDelete = FIND_FI;
        DeleteAliasCommand deleteCommand = new DeleteAliasCommand(FIND_FI);

        String expectedMessage = String.format(DeleteAliasCommand.MESSAGE_SUCCESS, aliasToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), model.getUserPrefs());
        expectedModel.deleteCommandWordAlias(aliasToDelete);


        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidAlias_throwsCommandException() {
        CommandWordAlias outOfBoundAlias = ROOMS_RO;
        DeleteAliasCommand deleteCommand = new DeleteAliasCommand(ROOMS_RO);

        assertCommandFailure(deleteCommand, model, DeleteAliasCommand.MESSAGE_INVALID_ALIAS);
    }

    @Test
    public void equals() {
        DeleteAliasCommand deleteFirstCommand = new DeleteAliasCommand(ROOMS_R);
        DeleteAliasCommand deleteSecondCommand = new DeleteAliasCommand(STUDENTS_STU);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteAliasCommand deleteFirstCommandCopy = new DeleteAliasCommand(ROOMS_R);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different command word alias -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }
}
