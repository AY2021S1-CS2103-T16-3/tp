package seedu.resireg.testutil;

import seedu.resireg.model.semester.Semester;

/**
 * A utility class containing a list of {@code Semester} objects to be used in tests.
 */
public class TypicalSemesters {

    public static final Semester AY2020_SEM_1 = new SemesterBuilder()
            .withAcademicYear(2020)
            .withSemesterNumber(1).build();

    public static final Semester AY2020_SEM_2 = new SemesterBuilder()
            .withAcademicYear(2020)
            .withSemesterNumber(2).build();

    private TypicalSemesters() {
    } // prevents instantiation
}