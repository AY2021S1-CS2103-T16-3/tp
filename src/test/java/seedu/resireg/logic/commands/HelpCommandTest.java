package seedu.resireg.logic.commands;

import static seedu.resireg.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.resireg.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.resireg.model.Model;
import seedu.resireg.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void executeWithNoCommand_success() {
        HelpCommand helpCommand = new HelpCommand("");
        assertCommandSuccess(helpCommand, model, HelpCommand.MESSAGE_GENERAL_HELP, expectedModel);

        HelpCommand helpCommandWithSpace = new HelpCommand(" ");
        assertCommandSuccess(helpCommandWithSpace, model, HelpCommand.MESSAGE_GENERAL_HELP, expectedModel);

    }

    @Test
    public void executeWithValidCommand_success() {
        HelpCommand helpCommand = new HelpCommand(AddCommand.COMMAND_WORD);
        assertCommandSuccess(helpCommand, model, AddCommand.HELP.getFullMessage(), expectedModel);

        HelpCommand helpCommandWithSpace = new HelpCommand(AddCommand.COMMAND_WORD + " ");
        assertCommandSuccess(helpCommandWithSpace, model, AddCommand.HELP.getFullMessage(), expectedModel);

        HelpCommand helpCommandForHelp = new HelpCommand(HelpCommand.COMMAND_WORD);
        assertCommandSuccess(helpCommandForHelp, model, HelpCommand.HELP.getFullMessage(), expectedModel);
    }

    @Test
    public void executeWithInvalidCommand_failure() {
        String invalidCommand = "nonsenseCommand";
        HelpCommand helpCommand = new HelpCommand(invalidCommand);
        assertCommandFailure(helpCommand, model, String.format(HelpCommand.MESSAGE_UNKNOWN_COMMAND, invalidCommand));
    }

}
