package com.example.sis.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sis.model.Student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/** Data-access layer for student records backed by SQLite. */
public final class StudentRepository {

    private final StudentDbHelper helper;

    public StudentRepository(Context context) {
        helper = new StudentDbHelper(context.getApplicationContext());
    }

    public List<Student> findAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String order = StudentDbHelper.COL_LAST_NAME + " COLLATE NOCASE ASC, "
                + StudentDbHelper.COL_FIRST_NAME + " COLLATE NOCASE ASC, "
                + StudentDbHelper.COL_MIDDLE_NAME + " COLLATE NOCASE ASC";
        List<Student> students = new ArrayList<>();
        try (Cursor cursor = db.query(StudentDbHelper.TABLE, null, null,
                null, null, null, order)) {
            while (cursor.moveToNext()) {
                students.add(readStudent(cursor));
            }
        }
        return students;
    }

    public Student findById(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        try (Cursor cursor = db.query(StudentDbHelper.TABLE, null,
                StudentDbHelper.COL_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, "1")) {
            return cursor.moveToFirst() ? readStudent(cursor) : null;
        }
    }

    public long insert(Student student) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.insertOrThrow(StudentDbHelper.TABLE, null, valuesFor(student));
    }

    public int update(Student student) {
        if (student.getId() <= 0) {
            throw new IllegalArgumentException("A saved student id is required for an update.");
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.update(StudentDbHelper.TABLE, valuesFor(student),
                StudentDbHelper.COL_ID + " = ?",
                new String[]{String.valueOf(student.getId())});
    }

    public void delete(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(StudentDbHelper.TABLE, StudentDbHelper.COL_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Checks whether a student number is already used by a different record.
     * Pass {@code -1} as {@code exceptId} when adding a new student.
     */
    public boolean existsStudentNumber(String studentNumber, long exceptId) {
        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            return false;
        }
        SQLiteDatabase db = helper.getReadableDatabase();
        String selection = StudentDbHelper.COL_STUDENT_NUMBER + " = ? COLLATE NOCASE";
        List<String> arguments = new ArrayList<>();
        arguments.add(studentNumber.trim());
        if (exceptId > 0) {
            selection += " AND " + StudentDbHelper.COL_ID + " <> ?";
            arguments.add(String.valueOf(exceptId));
        }
        try (Cursor cursor = db.query(StudentDbHelper.TABLE,
                new String[]{StudentDbHelper.COL_ID}, selection,
                arguments.toArray(new String[0]), null, null, null, "1")) {
            return cursor.moveToFirst();
        }
    }

    /** Suggests a student number such as {@code 2026-0009} for a new record. */
    public String suggestStudentNumber() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String prefix = year + "-";
        SQLiteDatabase db = helper.getReadableDatabase();
        int highest = 0;
        try (Cursor cursor = db.query(StudentDbHelper.TABLE,
                new String[]{StudentDbHelper.COL_STUDENT_NUMBER},
                StudentDbHelper.COL_STUDENT_NUMBER + " LIKE ?",
                new String[]{prefix + "%"}, null, null, null)) {
            while (cursor.moveToNext()) {
                String number = cursor.getString(0);
                if (number == null) {
                    continue;
                }
                String suffix = number.substring(prefix.length());
                try {
                    highest = Math.max(highest, Integer.parseInt(suffix));
                } catch (NumberFormatException ignored) {
                    // A custom number does not advance the sequence.
                }
            }
        }
        return prefix + String.format(Locale.ROOT, "%04d", highest + 1);
    }

    public void close() {
        helper.close();
    }

    private static Student readStudent(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(StudentDbHelper.COL_ID));
        String number = cursor.getString(cursor.getColumnIndexOrThrow(
                StudentDbHelper.COL_STUDENT_NUMBER));
        String lastName = cursor.getString(cursor.getColumnIndexOrThrow(
                StudentDbHelper.COL_LAST_NAME));
        String firstName = cursor.getString(cursor.getColumnIndexOrThrow(
                StudentDbHelper.COL_FIRST_NAME));
        String middleName = cursor.getString(cursor.getColumnIndexOrThrow(
                StudentDbHelper.COL_MIDDLE_NAME));
        String program = cursor.getString(cursor.getColumnIndexOrThrow(
                StudentDbHelper.COL_PROGRAM));
        String yearLevel = cursor.getString(cursor.getColumnIndexOrThrow(
                StudentDbHelper.COL_YEAR_LEVEL));
        return new Student(id, number, lastName, firstName, middleName,
                program, yearLevel);
    }

    private static ContentValues valuesFor(Student student) {
        ContentValues values = new ContentValues();
        values.put(StudentDbHelper.COL_STUDENT_NUMBER, student.getStudentNumber());
        values.put(StudentDbHelper.COL_LAST_NAME, student.getLastName());
        values.put(StudentDbHelper.COL_FIRST_NAME, student.getFirstName());
        values.put(StudentDbHelper.COL_MIDDLE_NAME, student.getMiddleName());
        values.put(StudentDbHelper.COL_PROGRAM, student.getProgram());
        values.put(StudentDbHelper.COL_YEAR_LEVEL, student.getYearLevel());
        return values;
    }
}
