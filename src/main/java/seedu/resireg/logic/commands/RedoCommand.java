package seedu.resireg.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.resireg.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.resireg.model.Model.PREDICATE_SHOW_ALL_ROOMS;

import seedu.resireg.logic.commands.exceptions.CommandException;
import seedu.resireg.model.Model;
import seedu.resireg.storage.Storage;

/**
 * Reverts the {@code model}'s ResiReg to its previously undone state.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = CommandWordEnum.REDO_COMMAND.toString();

    public static final String MESSAGE_SUCCESS = "Redo success!";
    public static final String MESSAGE_FAILURE = "No more actions to redo!";

    public static final Help HELP = new Help(COMMAND_WORD, "Redo a previous command.");

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);

        if (!model.canRedoResiReg()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.redoResiReg();
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredRoomList(PREDICATE_SHOW_ALL_ROOMS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}