package com.example.sis;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sis.data.StudentRepository;
import com.example.sis.model.Student;
import com.example.sis.ui.EdgeToEdge;
import com.example.sis.ui.UiUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

/** Add or edit one student record. */
public final class StudentFormActivity extends AppCompatActivity {

    public static final String EXTRA_STUDENT_ID = "extra_student_id";

    private static final Pattern NAME_PATTERN =
            Pattern.compile("[\\p{L} .'\\-]+");
    private static final Pattern STUDENT_NUMBER_PATTERN =
            Pattern.compile("[A-Za-z0-9\\-]{1,24}");

    private StudentRepository repository;
    private Student original;

    private TextView tvFormTitle;
    private TextView tvFormError;
    private TextInputLayout tilNumber;
    private TextInputLayout tilLastName;
    private TextInputLayout tilFirstName;
    private TextInputLayout tilMiddleName;
    private TextInputLayout tilProgram;
    private TextInputLayout tilYear;
    private TextInputEditText etNumber;
    private TextInputEditText etLastName;
    private TextInputEditText etFirstName;
    private TextInputEditText etMiddleName;
    private MaterialAutoCompleteTextView actProgram;
    private MaterialAutoCompleteTextView actYear;
    private MaterialButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.setup(this);
        setContentView(R.layout.activity_student_form);
        EdgeToEdge.padTop(findViewById(R.id.formToolbar));
        EdgeToEdge.padForIme(findViewById(R.id.formContent));

        repository = new StudentRepository(this);

        tvFormTitle = findViewById(R.id.tvFormTitle);
        tvFormError = findViewById(R.id.tvFormError);
        tilNumber = findViewById(R.id.tilNumber);
        tilLastName = findViewById(R.id.tilLastName);
        tilFirstName = findViewById(R.id.tilFirstName);
        tilMiddleName = findViewById(R.id.tilMiddleName);
        tilProgram = findViewById(R.id.tilProgram);
        tilYear = findViewById(R.id.tilYear);
        etNumber = findViewById(R.id.etNumber);
        etLastName = findViewById(R.id.etLastName);
        etFirstName = findViewById(R.id.etFirstName);
        etMiddleName = findViewById(R.id.etMiddleName);
        actProgram = findViewById(R.id.actProgram);
        actYear = findViewById(R.id.actYear);
        btnSave = findViewById(R.id.btnSave);

        findViewById(R.id.btnBack).setOnClickListener(view -> finish());
        findViewById(R.id.btnCancelForm).setOnClickListener(view -> finish());
        btnSave.setOnClickListener(view -> saveStudent());

        setupDropdown(actProgram, Student.PROGRAMS);
        setupDropdown(actYear, Student.YEAR_LEVELS);
        actProgram.setOnItemClickListener((parent, view, position, id) ->
                tilProgram.setError(null));
        actYear.setOnItemClickListener((parent, view, position, id) ->
                tilYear.setError(null));

        clearErrorOnEdit(etNumber, tilNumber);
        clearErrorOnEdit(etLastName, tilLastName);
        clearErrorOnEdit(etFirstName, tilFirstName);
        clearErrorOnEdit(etMiddleName, tilMiddleName);

        long studentId = getIntent().getLongExtra(EXTRA_STUDENT_ID, -1L);
        if (studentId > 0) {
            loadStudent(studentId);
        } else {
            tvFormTitle.setText(R.string.add_student_title);
            btnSave.setText(R.string.add_student_action);
            try {
                etNumber.setText(repository.suggestStudentNumber());
            } catch (RuntimeException ignored) {
                // The number is still editable if a suggestion cannot be made.
            }
        }
    }

    @Override
    protected void onDestroy() {
        repository.close();
        super.onDestroy();
    }

    private void setupDropdown(MaterialAutoCompleteTextView view, String[] options) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, options);
        view.setAdapter(adapter);
    }

    private void loadStudent(long studentId) {
        try {
            original = repository.findById(studentId);
        } catch (RuntimeException exception) {
            original = null;
        }
        if (original == null) {
            Toast.makeText(this, R.string.error_load_failed, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        tvFormTitle.setText(R.string.edit_student_title);
        btnSave.setText(R.string.save_changes);
        etNumber.setText(original.getStudentNumber());
        etLastName.setText(original.getLastName());
        etFirstName.setText(original.getFirstName());
        etMiddleName.setText(original.getMiddleName());
        actProgram.setText(original.getProgram(), false);
        actYear.setText(original.getYearLevel(), false);
    }

    private void saveStudent() {
        tilNumber.setError(null);
        tilLastName.setError(null);
        tilFirstName.setError(null);
        tilMiddleName.setError(null);
        tilProgram.setError(null);
        tilYear.setError(null);
        tvFormError.setVisibility(TextView.GONE);

        String number = valueOf(etNumber);
        String lastName = normalizedName(etLastName);
        String firstName = normalizedName(etFirstName);
        String middleName = normalizedName(etMiddleName);
        String program = canonical(actProgram.getText() == null
                ? "" : actProgram.getText().toString(), Student.PROGRAMS);
        String yearLevel = canonical(actYear.getText() == null
                ? "" : actYear.getText().toString(), Student.YEAR_LEVELS);

        if (number.isEmpty()) {
            showFieldError(tilNumber, etNumber, R.string.error_required_field);
            return;
        }
        if (!STUDENT_NUMBER_PATTERN.matcher(number).matches()) {
            showFieldError(tilNumber, etNumber, R.string.error_invalid_number);
            return;
        }
        if (lastName.isEmpty()) {
            showFieldError(tilLastName, etLastName, R.string.error_required_field);
            return;
        }
        if (!isValidName(lastName)) {
            showFieldError(tilLastName, etLastName, R.string.error_invalid_name);
            return;
        }
        if (firstName.isEmpty()) {
            showFieldError(tilFirstName, etFirstName, R.string.error_required_field);
            return;
        }
        if (!isValidName(firstName)) {
            showFieldError(tilFirstName, etFirstName, R.string.error_invalid_name);
            return;
        }
        if (!middleName.isEmpty() && !isValidName(middleName)) {
            showFieldError(tilMiddleName, etMiddleName, R.string.error_invalid_name);
            return;
        }
        if (program == null) {
            tilProgram.setError(getString(R.string.error_select_program));
            actProgram.requestFocus();
            return;
        }
        if (yearLevel == null) {
            tilYear.setError(getString(R.string.error_select_year));
            actYear.requestFocus();
            return;
        }

        long exceptId = original == null ? -1L : original.getId();
        try {
            if (repository.existsStudentNumber(number, exceptId)) {
                showFieldError(tilNumber, etNumber,
                        getString(R.string.error_duplicate_number, number));
                return;
            }

            Student student = new Student(
                    original == null ? 0L : original.getId(),
                    number, lastName, firstName, middleName, program, yearLevel);
            if (original == null) {
                repository.insert(student);
                Toast.makeText(this, R.string.student_added, Toast.LENGTH_SHORT).show();
            } else {
                if (repository.update(student) == 0) {
                    throw new IllegalStateException("The record no longer exists.");
                }
                Toast.makeText(this, R.string.student_updated, Toast.LENGTH_SHORT).show();
            }
            UiUtils.hideKeyboard(this);
            setResult(RESULT_OK);
            finish();
        } catch (SQLiteConstraintException exception) {
            showFieldError(tilNumber, etNumber,
                    getString(R.string.error_duplicate_number, number));
        } catch (RuntimeException exception) {
            String detail = exception.getMessage() == null
                    ? exception.getClass().getSimpleName() : exception.getMessage();
            tvFormError.setText(getString(R.string.error_save_failed, detail));
            tvFormError.setVisibility(TextView.VISIBLE);
        }
    }

    private void showFieldError(TextInputLayout layout, EditText field, int message) {
        layout.setError(getString(message));
        field.requestFocus();
    }

    private void showFieldError(TextInputLayout layout, EditText field, String message) {
        layout.setError(message);
        field.requestFocus();
    }

    private void clearErrorOnEdit(EditText field, TextInputLayout layout) {
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
                tvFormError.setVisibility(TextView.GONE);
            }
        });
    }

    private static String valueOf(EditText field) {
        return field.getText() == null ? "" : field.getText().toString().trim();
    }

    private static String normalizedName(EditText field) {
        return valueOf(field).replaceAll("\\s+", " ");
    }

    private static boolean isValidName(String value) {
        return NAME_PATTERN.matcher(value).matches()
                && value.matches(".*\\p{L}.*");
    }

    private static String canonical(String value, String[] options) {
        String cleaned = value == null ? "" : value.trim();
        for (String option : options) {
            if (option.equalsIgnoreCase(cleaned)) {
                return option;
            }
        }
        return null;
    }
}
