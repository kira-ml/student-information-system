package com.example.sis;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sis.data.StudentRepository;
import com.example.sis.model.Student;
import com.example.sis.ui.EdgeToEdge;
import com.example.sis.ui.StudentAdapter;
import com.example.sis.ui.UiUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** The signed-in dashboard: statistics, search, CRUD actions and logout. */
public final class DashboardActivity extends AppCompatActivity
        implements StudentAdapter.Listener {

    public static final String EXTRA_DISPLAY_NAME = "extra_display_name";
    public static final String EXTRA_ROLE = "extra_role";

    private StudentRepository repository;
    private StudentAdapter adapter;
    private final List<Student> allStudents = new ArrayList<>();

    private AppCompatEditText etSearch;
    private ImageButton btnClearSearch;
    private View emptyState;
    private TextView tvTotalStudents;
    private TextView tvProgramCount;
    private TextView tvYearCount;
    private TextView tvCount;
    private TextView tvEmptyTitle;
    private TextView tvEmptySubtitle;
    private MaterialButton btnEmptyAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.setup(this);
        setContentView(R.layout.activity_dashboard);
        EdgeToEdge.padTop(findViewById(R.id.dashboardHeader));
        EdgeToEdge.padBottom(findViewById(R.id.dashboardRoot));

        repository = new StudentRepository(this);

        TextView tvUserName = findViewById(R.id.tvUserName);
        TextView tvUserRole = findViewById(R.id.tvUserRole);
        TextView tvUserInitials = findViewById(R.id.tvUserInitials);
        String displayName = getIntent().getStringExtra(EXTRA_DISPLAY_NAME);
        String role = getIntent().getStringExtra(EXTRA_ROLE);
        if (displayName == null || displayName.trim().isEmpty()) {
            displayName = "Registrar";
        }
        if (role == null || role.trim().isEmpty()) {
            role = "Administrator";
        }
        tvUserName.setText(displayName);
        tvUserRole.setText(role);
        tvUserInitials.setText(UiUtils.initialsOf(displayName));

        etSearch = findViewById(R.id.etSearch);
        btnClearSearch = findViewById(R.id.btnClearSearch);
        emptyState = findViewById(R.id.emptyState);
        tvTotalStudents = findViewById(R.id.tvTotalStudents);
        tvProgramCount = findViewById(R.id.tvProgramCount);
        tvYearCount = findViewById(R.id.tvYearCount);
        tvCount = findViewById(R.id.tvCount);
        tvEmptyTitle = findViewById(R.id.tvEmptyTitle);
        tvEmptySubtitle = findViewById(R.id.tvEmptySubtitle);
        btnEmptyAdd = findViewById(R.id.btnEmptyAdd);

        RecyclerView recyclerView = findViewById(R.id.rvStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter(this);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnLogout).setOnClickListener(view -> confirmSignOut());
        findViewById(R.id.fabAdd).setOnClickListener(view -> openForm(null));
        btnEmptyAdd.setOnClickListener(view -> openForm(null));
        btnClearSearch.setOnClickListener(view -> {
            etSearch.setText("");
            etSearch.requestFocus();
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                applyFilter();
            }
        });
        etSearch.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                UiUtils.hideKeyboard(this);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
    }

    @Override
    protected void onDestroy() {
        repository.close();
        super.onDestroy();
    }

    private void loadStudents() {
        try {
            allStudents.clear();
            allStudents.addAll(repository.findAll());
        } catch (RuntimeException exception) {
            showSnackbar(getString(R.string.error_load_failed));
            return;
        }

        Set<String> programs = new HashSet<>();
        Set<String> years = new HashSet<>();
        for (Student student : allStudents) {
            programs.add(student.getProgram());
            years.add(student.getYearLevel());
        }
        tvTotalStudents.setText(String.valueOf(allStudents.size()));
        tvProgramCount.setText(String.valueOf(programs.size()));
        tvYearCount.setText(String.valueOf(years.size()));
        applyFilter();
    }

    private void applyFilter() {
        String query = etSearch.getText() == null
                ? "" : etSearch.getText().toString().trim().toLowerCase(Locale.ROOT);
        List<Student> filtered = new ArrayList<>();
        for (Student student : allStudents) {
            if (matches(student, query)) {
                filtered.add(student);
            }
        }
        adapter.submitList(filtered);
        tvCount.setText(String.valueOf(filtered.size()));
        btnClearSearch.setVisibility(query.isEmpty() ? View.GONE : View.VISIBLE);

        boolean isEmpty = filtered.isEmpty();
        emptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        if (isEmpty) {
            boolean searching = !query.isEmpty();
            tvEmptyTitle.setText(searching
                    ? R.string.no_results_title : R.string.no_students_title);
            tvEmptySubtitle.setText(searching
                    ? R.string.no_results_subtitle : R.string.no_students_subtitle);
            btnEmptyAdd.setVisibility(searching ? View.GONE : View.VISIBLE);
        }
    }

    private static boolean matches(Student student, String query) {
        if (query.isEmpty()) {
            return true;
        }
        return contains(student.getStudentNumber(), query)
                || contains(student.getFirstName(), query)
                || contains(student.getLastName(), query)
                || contains(student.getMiddleName(), query)
                || contains(student.getFullName(), query)
                || contains(student.getProgram(), query)
                || contains(student.getYearLevel(), query);
    }

    private static boolean contains(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }

    @Override
    public void onEdit(Student student) {
        openForm(student);
    }

    @Override
    public void onDelete(Student student) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_dialog_title)
                .setMessage(getString(R.string.delete_dialog_message,
                        student.getFullName(), student.getStudentNumber()))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteStudent(student))
                .show();
    }

    private void deleteStudent(Student student) {
        try {
            repository.delete(student.getId());
            loadStudents();
            showSnackbar(getString(R.string.student_deleted));
        } catch (RuntimeException exception) {
            showSnackbar(getString(R.string.error_delete_failed));
        }
    }

    private void confirmSignOut() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.logout_dialog_title)
                .setMessage(R.string.logout_dialog_message)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.sign_out, (dialog, which) -> {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    private void openForm(Student student) {
        Intent intent = new Intent(this, StudentFormActivity.class);
        if (student != null) {
            intent.putExtra(StudentFormActivity.EXTRA_STUDENT_ID, student.getId());
        }
        startActivity(intent);
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(R.id.dashboardRoot), message, Snackbar.LENGTH_SHORT)
                .show();
    }
}
