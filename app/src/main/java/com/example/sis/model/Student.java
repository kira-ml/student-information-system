package com.example.sis.model;

import java.util.Locale;
import java.util.Objects;

/**
 * A student record used by both the database layer and the UI.
 *
 * <p>This class is deliberately free of Android imports so it can be tested
 * with a plain JUnit unit test.</p>
 */
public final class Student {

    public static final String[] PROGRAMS = {
            "BS Information Technology",
            "BS Computer Science",
            "BS Information Systems",
            "BS Business Administration",
            "BS Accountancy",
            "BS Civil Engineering",
            "BS Education",
            "BS Nursing"
    };

    public static final String[] YEAR_LEVELS = {
            "1st Year",
            "2nd Year",
            "3rd Year",
            "4th Year",
            "5th Year"
    };

    private final long id;
    private final String studentNumber;
    private final String lastName;
    private final String firstName;
    private final String middleName;
    private final String program;
    private final String yearLevel;

    public Student(long id,
                   String studentNumber,
                   String lastName,
                   String firstName,
                   String middleName,
                   String program,
                   String yearLevel) {
        this.id = id;
        this.studentNumber = required(studentNumber, "Student number");
        this.lastName = required(lastName, "Last name");
        this.firstName = required(firstName, "First name");
        this.middleName = middleName == null ? "" : middleName.trim();
        this.program = required(program, "Program");
        this.yearLevel = required(yearLevel, "Year level");
    }

    public Student(String studentNumber,
                   String lastName,
                   String firstName,
                   String middleName,
                   String program,
                   String yearLevel) {
        this(0L, studentNumber, lastName, firstName, middleName, program, yearLevel);
    }

    private static String required(String value, String label) {
        String cleaned = value == null ? "" : value.trim();
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException(label + " is required.");
        }
        return cleaned;
    }

    public long getId() {
        return id;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getProgram() {
        return program;
    }

    public String getYearLevel() {
        return yearLevel;
    }

    /** First name, middle name (if any) and last name. */
    public String getFullName() {
        StringBuilder name = new StringBuilder(firstName);
        if (!middleName.isEmpty()) {
            name.append(' ').append(middleName);
        }
        name.append(' ').append(lastName);
        return name.toString();
    }

    public String getInitials() {
        String first = firstName.isEmpty() ? "" : firstName.substring(0, 1);
        String last = lastName.isEmpty() ? "" : lastName.substring(0, 1);
        return (first + last).toUpperCase(Locale.ROOT);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Student)) {
            return false;
        }
        Student student = (Student) other;
        return id == student.id
                && studentNumber.equalsIgnoreCase(student.studentNumber)
                && lastName.equals(student.lastName)
                && firstName.equals(student.firstName)
                && middleName.equals(student.middleName)
                && program.equals(student.program)
                && yearLevel.equals(student.yearLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentNumber.toLowerCase(Locale.ROOT),
                lastName, firstName, middleName, program, yearLevel);
    }

    @Override
    public String toString() {
        return getFullName() + " (" + studentNumber + ")";
    }
}
