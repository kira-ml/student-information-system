package com.example.sis.ui;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Locale;

/** Small UI utilities shared by the activities. */
public final class UiUtils {

    private UiUtils() {
    }

    public static int dp(Context context, float value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }

    public static void hideKeyboard(Activity activity) {
        View focus = activity.getCurrentFocus();
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (focus != null && manager != null) {
            manager.hideSoftInputFromWindow(focus.getWindowToken(), 0);
            focus.clearFocus();
        }
    }

    public static String initialsOf(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "?";
        }
        String[] parts = name.trim().split("\\s+");
        String initials = parts[0].substring(0, 1);
        if (parts.length > 1) {
            initials += parts[parts.length - 1].substring(0, 1);
        }
        return initials.toUpperCase(Locale.ROOT);
    }
}
