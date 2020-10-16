package seedu.resireg.model;

import java.util.ArrayList;
import java.util.List;

import seedu.resireg.model.exceptions.NoRedoableStateException;
import seedu.resireg.model.exceptions.NoUndoableStateException;

/**
 * {@code AddressBook} that keeps track of its own history.
 */
public class VersionedResiReg extends AddressBook {

    private final List<ReadOnlyAddressBook> resiRegStateList;
    private int currentStatePtr;

    /**
     * Creates a {@code VersionedResiReg} with the given {@code ReadOnlyAddressBook}.
     */
    public VersionedResiReg(ReadOnlyAddressBook initialState) {
        super(initialState);

        resiRegStateList = new ArrayList<>();
        resiRegStateList.add(new AddressBook(initialState));
        currentStatePtr = 0;
    }

    private void removeStatesAfterCurrentPtr() {
        resiRegStateList.subList(currentStatePtr + 1, resiRegStateList.size()).clear();
    }

    /**
     * Saves a copy of the current {@code AddressBook} state at the end of
     * the state list. Undone states are removed from the state list.
     */
    public void save() {
        removeStatesAfterCurrentPtr();
        resiRegStateList.add(new AddressBook(this));
        currentStatePtr++;
    }

    /**
     * Returns true if {@code undo()} has ResiReg states to undo.
     */
    public boolean canUndo() {
        return currentStatePtr > 0;
    }

    /**
     * Returns true if {@code redo()} has ResiReg states to redo.
     */
    public boolean canRedo() {
        return currentStatePtr < resiRegStateList.size() - 1;
    }

    /**
     * Restores ResiReg to its previous state
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePtr--;
        resetData(resiRegStateList.get(currentStatePtr));
    }

    /**
     * Restores ResiReg to its previously undone state
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePtr++;
        resetData(resiRegStateList.get(currentStatePtr));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VersionedResiReg)) {
            return false;
        }

        VersionedResiReg otherVersionedResiReg = (VersionedResiReg) other;

        // state check
        return super.equals(otherVersionedResiReg)
                && resiRegStateList.equals(otherVersionedResiReg.resiRegStateList)
                && currentStatePtr == otherVersionedResiReg.currentStatePtr;
    }
}

