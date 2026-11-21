package com.example.sis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.example.sis.data.AuthService;

import org.junit.Test;

public class AuthServiceTest {

    private final AuthService authService = new AuthService();

    @Test
    public void registrarAccountSignsIn() {
        AuthService.User user = authService.authenticate("admin", "admin123");
        assertNotNull(user);
        assertEquals("Registrar", user.getDisplayName());
        assertEquals("Administrator", user.getRole());
        assertEquals("R", user.getInitials());
    }

    @Test
    public void usernameIsCaseInsensitiveAndTrimmed() {
        AuthService.User user = authService.authenticate("  TEACHER  ", "teacher123");
        assertNotNull(user);
        assertEquals("Faculty", user.getDisplayName());
        assertEquals("F", user.getInitials());
    }

    @Test
    public void wrongPasswordIsRejected() {
        assertNull(authService.authenticate("admin", "wrong-password"));
    }

    @Test
    public void unknownUserIsRejected() {
        assertNull(authService.authenticate("student", "admin123"));
    }
}
