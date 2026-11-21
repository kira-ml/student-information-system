package com.example.sis;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sis.data.AuthService;
import com.example.sis.ui.EdgeToEdge;
import com.example.sis.ui.UiUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/** Sign-in screen for the Student Information System. */
public final class LoginActivity extends AppCompatActivity {

    private final AuthService authService = new AuthService();

    private TextInputLayout tilUsername;
    private TextInputLayout tilPassword;
    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private TextView tvLoginError;
    private MaterialCardView loginCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.setup(this);
        setContentView(R.layout.activity_login);
        EdgeToEdge.padTop(findViewById(R.id.loginRoot));
        EdgeToEdge.padForIme(findViewById(R.id.loginContainer));

        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPassword);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvLoginError = findViewById(R.id.tvLoginError);
        loginCard = findViewById(R.id.loginCard);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        View demoAccountCard = findViewById(R.id.demoAccountCard);

        btnLogin.setOnClickListener(view -> attemptLogin());
        clearErrorOnEdit(etUsername, tilUsername);
        clearErrorOnEdit(etPassword, tilPassword);
        etPassword.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                attemptLogin();
                return true;
            }
            return false;
        });
        etUsername.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                etPassword.requestFocus();
                return true;
            }
            return false;
        });

        // A small convenience for the classroom demo: tapping the demo card
        // fills in the registrar account without hiding the credentials.
        demoAccountCard.setOnClickListener(view -> {
            etUsername.setText("admin");
            etPassword.setText("admin123");
            tilUsername.setError(null);
            tilPassword.setError(null);
            tvLoginError.setVisibility(View.GONE);
            etPassword.requestFocus();
            etPassword.setSelection(etPassword.getText() == null
                    ? 0 : etPassword.getText().length());
        });
    }

    private void attemptLogin() {
        tilUsername.setError(null);
        tilPassword.setError(null);
        tvLoginError.setVisibility(View.GONE);

        String username = valueOf(etUsername);
        String password = valueOf(etPassword);
        boolean valid = true;
        if (username.isEmpty()) {
            tilUsername.setError(getString(R.string.error_username_required));
            valid = false;
        }
        if (password.isEmpty()) {
            tilPassword.setError(getString(R.string.error_password_required));
            valid = false;
        }
        if (!valid) {
            if (username.isEmpty()) {
                etUsername.requestFocus();
            } else {
                etPassword.requestFocus();
            }
            return;
        }

        AuthService.User user = authService.authenticate(username, password);
        if (user == null) {
            String message = getString(R.string.error_invalid_login);
            tvLoginError.setText(message);
            tvLoginError.setVisibility(View.VISIBLE);
            tvLoginError.announceForAccessibility(message);
            ObjectAnimator.ofFloat(loginCard, View.TRANSLATION_X,
                    0f, 12f, -12f, 9f, -9f, 5f, -5f, 0f)
                    .setDuration(340)
                    .start();
            etPassword.requestFocus();
            return;
        }

        UiUtils.hideKeyboard(this);
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra(DashboardActivity.EXTRA_DISPLAY_NAME, user.getDisplayName());
        intent.putExtra(DashboardActivity.EXTRA_ROLE, user.getRole());
        startActivity(intent);
        finish();
    }

    private static String valueOf(TextInputEditText field) {
        return field.getText() == null ? "" : field.getText().toString().trim();
    }

    private void clearErrorOnEdit(TextInputEditText field, TextInputLayout layout) {
        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                layout.setError(null);
                tvLoginError.setVisibility(View.GONE);
            }
        });
    }
}
