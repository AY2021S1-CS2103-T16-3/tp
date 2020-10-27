package seedu.resireg.model;

import static java.util.Objects.requireNonNull;
import static seedu.resireg.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.resireg.commons.core.GuiSettings;
import seedu.resireg.commons.core.LogsCenter;
import seedu.resireg.model.alias.CommandWordAlias;
import seedu.resireg.model.allocation.Allocation;
import seedu.resireg.model.bin.BinItem;
import seedu.resireg.model.room.Room;
import seedu.resireg.model.semester.Semester;
import seedu.resireg.model.student.Student;

/**
 * Represents the in-memory model of ResiReg data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedResiReg versionedResiReg;
    private final UserPrefs userPrefs;
    private final Semester semester;
    private final ModelAwareFilteredList<Student> filteredStudents;
    private final ModelAwareFilteredList<Room> filteredRooms;
    private final ModelAwareFilteredList<Allocation> filteredAllocations;
    private final ModelAwareFilteredList<BinItem> filteredBinItems;


    /**
     * Initializes a ModelManager with the given ResiReg data and userPrefs.
     */
    public ModelManager(ReadOnlyResiReg readOnlyResiReg, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(readOnlyResiReg, userPrefs);

        logger.fine("Initializing with ResiReg data: " + readOnlyResiReg + " and user prefs " + userPrefs);

        versionedResiReg = new VersionedResiReg(readOnlyResiReg);
        this.userPrefs = new UserPrefs(userPrefs);
        semester = versionedResiReg.getSemester();
        filteredStudents = new ModelAwareFilteredList<>(versionedResiReg.getStudentList());
        filteredRooms = new ModelAwareFilteredList<>(versionedResiReg.getRoomList());
        filteredAllocations = new ModelAwareFilteredList<>(versionedResiReg.getAllocationList());
        filteredBinItems = new ModelAwareFilteredList<>(versionedResiReg.getBinItemList());
    }

    public ModelManager() {
        this(new ResiReg(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public List<CommandWordAlias> getCommandWordAliases() {
        return Collections.unmodifiableList(userPrefs.getCommandWordAliases());
    }

    @Override
    public String getCommandWordAliasesAsString() {
        return userPrefs.getCommandWordAliasesAsString();
    }

    @Override
    public boolean hasCommandWordAlias(CommandWordAlias target) {
        requireNonNull(target);
        return userPrefs.hasAlias(target);
    }

    @Override
    public void deleteCommandWordAlias(CommandWordAlias target) {
        userPrefs.deleteAlias(target);
    }

    @Override
    public void addCommandWordAlias(CommandWordAlias source) {
        requireNonNull(source);
        userPrefs.addAlias(source);
    }

    @Override
    public Path getResiRegFilePath() {
        return userPrefs.getResiRegFilePath();
    }

    @Override
    public void setResiRegFilePath(Path resiRegFilePath) {
        requireNonNull(resiRegFilePath);
        userPrefs.setResiRegFilePath(resiRegFilePath);
    }

    @Override
    public void setDaysStoredInBin(int daysStoredInBin) {
        assert daysStoredInBin > 0 : "Days stored must be a positive integer";
        userPrefs.setDaysStoredInBin(daysStoredInBin);
    }

    //=========== ResiReg ================================================================================

    @Override
    public void setResiReg(ReadOnlyResiReg resiReg) {
        versionedResiReg.resetData(resiReg);
    }

    @Override
    public ReadOnlyResiReg getResiReg() {
        return versionedResiReg;
    }

    //=========== Utils  ================================================================================

    private void refilterLists() {
        filteredStudents.refilter();
        filteredRooms.refilter();
        filteredAllocations.refilter();
    }

    //=========== Student  ================================================================================

    @Override
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return versionedResiReg.hasStudent(student);
    }

    @Override
    public void deleteStudent(Student target) {
        versionedResiReg.removeStudent(target);
        refilterLists();
    }

    @Override
    public void addStudent(Student student) {
        requireNonNull(student);
        versionedResiReg.addStudent(student);
        refilterLists();
    }

    @Override
    public void addStudent(Student student, boolean isFront) {
        requireNonNull(student);
        versionedResiReg.addStudent(student, isFront);
        refilterLists();
    }

    @Override
    public void setStudent(Student target, Student editedStudent) {
        requireAllNonNull(target, editedStudent);
        versionedResiReg.setStudent(target, editedStudent);
        refilterLists();
    }

    //=========== Room ================================================================================
    @Override
    public void setRoom(Room target, Room editedRoom) {
        requireAllNonNull(target, editedRoom);
        versionedResiReg.setRoom(target, editedRoom);
        refilterLists();
    }

    @Override
    public boolean hasRoom(Room room) {
        requireNonNull(room);
        return versionedResiReg.hasRoom(room);
    }

    @Override
    public void deleteRoom(Room target) {
        versionedResiReg.removeRoom(target);
        refilterLists();
    }

    @Override
    public void addRoom(Room room) {
        requireNonNull(room);
        versionedResiReg.addRoom(room);
        refilterLists();
    }

    //=========== Allocation ================================================================================
    @Override
    public boolean isAllocated(Student student) {
        requireNonNull(student);
        return versionedResiReg.isAllocated(student);
    }

    /**
     * Returns true if an allocation relating to {@code room} exists in ResiReg.
     */
    @Override
    public boolean isAllocated(Room room) {
        requireNonNull(room);
        return versionedResiReg.isAllocated(room);
    }

    @Override
    public boolean hasAllocation(Allocation allocation) {
        requireNonNull(allocation);
        return versionedResiReg.hasAllocation(allocation);
    }

    @Override
    public void removeAllocation(Allocation target) {
        requireNonNull(target);
        versionedResiReg.removeAllocation(target);
        refilterLists();
    }

    @Override
    public void addAllocation(Allocation allocation) {
        requireNonNull(allocation);
        versionedResiReg.addAllocation(allocation);
        refilterLists();
    }

    @Override
    public void setAllocation(Allocation target, Allocation editedAllocation) {
        requireAllNonNull(target, editedAllocation);
        versionedResiReg.setAllocation(target, editedAllocation);
        refilterLists();
    }

    //=========== Bin Items =============================================================
    @Override
    public void setBinItem(BinItem target, BinItem editedItem) {
        requireAllNonNull(target, editedItem);
        versionedResiReg.setBinItem(target, editedItem);
        refilterLists();
    }

    @Override
    public boolean hasBinItem(BinItem binItem) {
        requireNonNull(binItem);
        return versionedResiReg.hasBinItem(binItem);
    }

    @Override
    public void deleteBinItem(BinItem target) {
        versionedResiReg.removeBinItem(target);
        refilterLists();
    }

    @Override
    public void addBinItem(BinItem binItem) {
        requireNonNull(binItem);
        versionedResiReg.addBinItem(binItem);
        refilterLists();
    }

    @Override
    public void deleteExpiredBinItems() {
        versionedResiReg.deleteExpiredBinItems(userPrefs.getDaysStoredInBin());
    }

    //=========== Semester =============================================================
    public Semester getSemester() {
        return semester;
    }

    //=========== Filtered Student List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Student} backed by the internal list of
     * {@code versionedResiReg}
     */
    @Override
    public ObservableList<Student> getFilteredStudentList() {
        return filteredStudents.getObservableList();
    }

    @Override
    public void updateFilteredStudentList(Predicate<Student> predicate) {
        requireNonNull(predicate);
        filteredStudents.setPredicate(predicate);
    }

    @Override
    public void updateFilteredStudentList(ModelPredicate<Student> predicate) {
        requireNonNull(predicate);
        filteredStudents.setPredicate(predicate);
    }

    //=========== Filtered Room List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Room} backed by the internal list of
     * {@code versionedResiReg}
     */
    @Override
    public ObservableList<Room> getFilteredRoomList() {
        return filteredRooms.getObservableList();
    }

    //=========== Filtered Allocation List Accessors =============================================================

    @Override
    public ObservableList<Allocation> getFilteredAllocationList() {
        return filteredAllocations.getObservableList();
    }

    //=========== Filtered Bin Item List Accessors =============================================================

    @Override
    public ObservableList<BinItem> getFilteredBinItemList() {
        return filteredBinItems.getObservableList();
    }

    //=========== Filtered  List Setters =============================================================

    @Override
    public void updateFilteredRoomList(Predicate<Room> predicate) {
        requireNonNull(predicate);
        filteredRooms.setPredicate(predicate);
    }

    @Override
    public void updateFilteredRoomList(ModelPredicate<Room> predicate) {
        requireNonNull(predicate);
        filteredRooms.setPredicate(predicate);
    }

    @Override
    public void updateFilteredAllocationList(Predicate<Allocation> predicate) {
        requireNonNull(predicate);
        filteredAllocations.setPredicate(predicate);
    }

    @Override
    public void updateFilteredAllocationList(ModelPredicate<Allocation> predicate) {
        requireNonNull(predicate);
        filteredAllocations.setPredicate(predicate);
    }

    @Override
    public void updateFilteredBinItemList(Predicate<BinItem> predicate) {
        requireNonNull(predicate);
        filteredBinItems.setPredicate(predicate);
    }

    @Override
    public void updateFilteredBinItemList(ModelPredicate<BinItem> predicate) {
        requireNonNull(predicate);
        filteredBinItems.setPredicate(predicate);
    }



    //=========== Undo/Redo =============================================================

    @Override
    public boolean canUndoResiReg() {
        return versionedResiReg.canUndo();
    }

    @Override
    public boolean canRedoResiReg() {
        return versionedResiReg.canRedo();
    }

    @Override
    public void undoResiReg() {
        versionedResiReg.undo();
        refilterLists();
    }

    @Override
    public void redoResiReg() {
        versionedResiReg.redo();
        refilterLists();
    }

    @Override
    public void saveStateResiReg() {
        versionedResiReg.save();
    }

    @Override
    public AppMode getAppMode() {
        return AppMode.NORMAL;
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedResiReg.equals(other.versionedResiReg)
                && userPrefs.equals(other.userPrefs)
                && filteredStudents.equals(other.filteredStudents)
                && filteredRooms.equals(other.filteredRooms)
                && filteredAllocations.equals(other.filteredAllocations)
                && filteredBinItems.equals(other.filteredBinItems);
    }

    /**
     * Essentially a FilteredList which allows predicates that use methods from the {@code Model}. This class
     * contains a useful method to refilter the list after the state of the model or any of the elements in the
     * underlying {@code ObservableList} have changed.
     */
    private class ModelAwareFilteredList<T> {
        private final FilteredList<T> filteredList;
        private ModelPredicate<T> modelPredicate;

        ModelAwareFilteredList(ObservableList<T> list) {
            filteredList = new FilteredList<>(list);
            setPredicate((t, model) -> true);
        }

        void setPredicate(ModelPredicate<T> predicate) {
            requireNonNull(predicate);
            modelPredicate = predicate;
            refilter();
        }

        void setPredicate(Predicate<T> predicate) {
            setPredicate((t, model) -> predicate.test(t));
        }

        /**
         * Refilter the list in case any elements in the list or the state of the model has changed.
         */
        void refilter() {
            filteredList.setPredicate(t -> modelPredicate.test(t, ModelManager.this));
        }

        ObservableList<T> getObservableList() {
            return filteredList;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof ModelAwareFilteredList)) {
                return false;
            }
            return filteredList.equals(((ModelAwareFilteredList<?>) obj).filteredList);
        }
    }
}
