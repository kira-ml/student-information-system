package com.example.sis.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.sis.R;

/**
 * Insets helper for an edge-to-edge layout. Headers extend behind the status
 * bar while their content stays below it, and lists avoid the navigation bar.
 */
public final class EdgeToEdge {

    private EdgeToEdge() {
    }

    public static void setup(Activity activity) {
        Window window = activity.getWindow();
        WindowCompat.setDecorFitsSystemWindows(window, false);
        window.setStatusBarColor(Color.TRANSPARENT);

        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(window, window.getDecorView());
        controller.setAppearanceLightStatusBars(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.setNavigationBarColor(Color.TRANSPARENT);
            controller.setAppearanceLightNavigationBars(true);
        } else {
            window.setNavigationBarColor(ContextCompat.getColor(activity, R.color.navy));
            controller.setAppearanceLightNavigationBars(false);
        }
    }

    /** Adds the status-bar inset to a header, keeping its existing padding. */
    public static void padTop(View view) {
        final int baseTop = view.getPaddingTop();
        ViewCompat.setOnApplyWindowInsetsListener(view, (target, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            target.setPadding(target.getPaddingLeft(), baseTop + bars.top,
                    target.getPaddingRight(), target.getPaddingBottom());
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(view);
    }

    /** Adds the navigation-bar inset to the bottom of a view. */
    public static void padBottom(View view) {
        final int baseBottom = view.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(view, (target, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars());
            target.setPadding(target.getPaddingLeft(), target.getPaddingTop(),
                    target.getPaddingRight(), baseBottom + bars.bottom);
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(view);
    }

    /**
     * Adds space below scrollable form content for both the navigation bar and
     * the on-screen keyboard, so the save button can always be reached.
     */
    public static void padForIme(View view) {
        final int baseBottom = view.getPaddingBottom();
        final int left = view.getPaddingLeft();
        final int top = view.getPaddingTop();
        final int right = view.getPaddingRight();
        ViewCompat.setOnApplyWindowInsetsListener(view, (target, windowInsets) -> {
            int navigation = windowInsets.getInsets(
                    WindowInsetsCompat.Type.navigationBars()).bottom;
            int keyboard = windowInsets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
            target.setPadding(left, top, right,
                    baseBottom + Math.max(navigation, keyboard));
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(view);
    }
}
