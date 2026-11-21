package com.example.sis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.example.sis.model.Student;

import org.junit.Test;

public class StudentTest {

    @Test
    public void fullNameIncludesMiddleName() {
        Student student = new Student("2026-0001", "Dela Cruz", "Maria Clara",
                "Santos", "BS Information Technology", "3rd Year");
        assertEquals("Maria Clara Santos Dela Cruz", student.getFullName());
        assertEquals("MD", student.getInitials());
    }

    @Test
    public void middleNameIsOptional() {
        Student student = new Student("2026-0002", "Reyes", "Juan", "",
                "BS Computer Science", "2nd Year");
        assertEquals("Juan Reyes", student.getFullName());
    }

    @Test
    public void requiredFieldsAreValidated() {
        assertThrows(IllegalArgumentException.class, () -> new Student("",
                "Reyes", "Juan", "", "BS Computer Science", "2nd Year"));
    }

    @Test
    public void idIsPreserved() {
        Student student = new Student(42L, "2026-0003", "Santos", "Ana",
                "", "BS Nursing", "1st Year");
        assertEquals(42L, student.getId());
    }
}
