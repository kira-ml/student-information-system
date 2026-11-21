package com.example.sis.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Creates and upgrades the local SQLite database. A fresh install is seeded
 * with sample records so the application can be demonstrated immediately.
 */
public final class StudentDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "sis.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE = "students";
    public static final String COL_ID = "_id";
    public static final String COL_STUDENT_NUMBER = "student_number";
    public static final String COL_LAST_NAME = "last_name";
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_MIDDLE_NAME = "middle_name";
    public static final String COL_PROGRAM = "program";
    public static final String COL_YEAR_LEVEL = "year_level";

    public StudentDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_STUDENT_NUMBER + " TEXT NOT NULL COLLATE NOCASE UNIQUE, "
                + COL_LAST_NAME + " TEXT NOT NULL, "
                + COL_FIRST_NAME + " TEXT NOT NULL, "
                + COL_MIDDLE_NAME + " TEXT NOT NULL DEFAULT '', "
                + COL_PROGRAM + " TEXT NOT NULL, "
                + COL_YEAR_LEVEL + " TEXT NOT NULL)");
        seedSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database only contains lab data, so a clean rebuild is acceptable.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private void seedSampleData(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            insertSample(db, "2026-0001", "Dela Cruz", "Maria Clara", "Santos",
                    "BS Information Technology", "3rd Year");
            insertSample(db, "2026-0002", "Reyes", "Juan Miguel", "Bautista",
                    "BS Computer Science", "2nd Year");
            insertSample(db, "2026-0003", "Santos", "Ana Marie", "Lopez",
                    "BS Information Systems", "4th Year");
            insertSample(db, "2026-0004", "Garcia", "Jose Antonio", "Ramos",
                    "BS Civil Engineering", "1st Year");
            insertSample(db, "2026-0005", "Mendoza", "Liza", "Fernandez",
                    "BS Business Administration", "3rd Year");
            insertSample(db, "2026-0006", "Bautista", "Mark Angelo", "Cruz",
                    "BS Information Technology", "1st Year");
            insertSample(db, "2026-0007", "Villanueva", "Sofia Isabel", "Navarro",
                    "BS Nursing", "2nd Year");
            insertSample(db, "2026-0008", "Aquino", "Paolo", "Domingo",
                    "BS Accountancy", "4th Year");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void insertSample(SQLiteDatabase db, String number, String lastName,
                              String firstName, String middleName,
                              String program, String yearLevel) {
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_NUMBER, number);
        values.put(COL_LAST_NAME, lastName);
        values.put(COL_FIRST_NAME, firstName);
        values.put(COL_MIDDLE_NAME, middleName);
        values.put(COL_PROGRAM, program);
        values.put(COL_YEAR_LEVEL, yearLevel);
        db.insertOrThrow(TABLE, null, values);
    }
}
