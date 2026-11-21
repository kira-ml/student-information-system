package com.example.sis.data;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Demo authentication for the laboratory activity.
 *
 * <p>Accounts are held in memory and no password is written to disk. This is
 * intentional for a classroom demonstration; a production registrar system
 * would use a secure server-side identity provider.</p>
 */
public final class AuthService {

    public static final class User {
        private final String username;
        private final String displayName;
        private final String role;

        User(String username, String displayName, String role) {
            this.username = username;
            this.displayName = displayName;
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getRole() {
            return role;
        }

        public String getInitials() {
            String[] parts = displayName.trim().split("\\s+");
            String initials = parts[0].substring(0, 1);
            if (parts.length > 1) {
                initials += parts[parts.length - 1].substring(0, 1);
            }
            return initials.toUpperCase(Locale.ROOT);
        }
    }

    private static final class Account {
        private final String password;
        private final String displayName;
        private final String role;

        Account(String password, String displayName, String role) {
            this.password = password;
            this.displayName = displayName;
            this.role = role;
        }
    }

    private static final Map<String, Account> ACCOUNTS;

    static {
        Map<String, Account> accounts = new LinkedHashMap<>();
        accounts.put("admin", new Account("admin123", "Registrar", "Administrator"));
        accounts.put("teacher", new Account("teacher123", "Faculty", "Faculty Member"));
        ACCOUNTS = Collections.unmodifiableMap(accounts);
    }

    /** Returns the signed-in user, or {@code null} when credentials do not match. */
    public User authenticate(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        String key = username.trim().toLowerCase(Locale.ROOT);
        Account account = ACCOUNTS.get(key);
        if (account != null && account.password.equals(password)) {
            return new User(key, account.displayName, account.role);
        }
        return null;
    }
}
