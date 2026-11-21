package com.example.sis.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sis.R;
import com.example.sis.model.Student;

import java.util.ArrayList;
import java.util.List;

/** RecyclerView adapter for student cards. */
public final class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    public interface Listener {
        void onEdit(Student student);

        void onDelete(Student student);
    }

    private static final int[] AVATAR_BACKGROUNDS = {
            R.color.indigo_soft,
            R.color.amber_soft,
            R.color.green_soft,
            R.color.purple_soft
    };

    private static final int[] AVATAR_TEXT_COLORS = {
            R.color.indigo,
            R.color.amber,
            R.color.green,
            R.color.purple
    };

    private final List<Student> students = new ArrayList<>();
    private final Listener listener;

    public StudentAdapter(Listener listener) {
        this.listener = listener;
        setHasStableIds(true);
    }

    public void submitList(List<Student> updatedStudents) {
        students.clear();
        students.addAll(updatedStudents);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return students.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.bind(students.get(position));
    }

    final class StudentViewHolder extends RecyclerView.ViewHolder {
        private final View avatarContainer;
        private final TextView tvInitials;
        private final TextView tvName;
        private final TextView tvNumber;
        private final TextView tvAcademic;
        private final ImageButton btnMore;

        StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarContainer = itemView.findViewById(R.id.avatarContainer);
            tvInitials = itemView.findViewById(R.id.tvInitials);
            tvName = itemView.findViewById(R.id.tvStudentName);
            tvNumber = itemView.findViewById(R.id.tvStudentNumber);
            tvAcademic = itemView.findViewById(R.id.tvStudentAcademic);
            btnMore = itemView.findViewById(R.id.btnMore);
        }

        void bind(Student student) {
            Context context = itemView.getContext();
            tvName.setText(student.getFullName());
            tvNumber.setText(student.getStudentNumber());
            tvAcademic.setText(context.getString(R.string.program_year,
                    student.getProgram(), student.getYearLevel()));
            tvInitials.setText(student.getInitials());

            int colorIndex = (student.getFullName().hashCode() & 0x7FFFFFFF)
                    % AVATAR_BACKGROUNDS.length;
            GradientDrawable avatar = new GradientDrawable();
            avatar.setShape(GradientDrawable.OVAL);
            avatar.setColor(ContextCompat.getColor(context,
                    AVATAR_BACKGROUNDS[colorIndex]));
            avatarContainer.setBackground(avatar);
            tvInitials.setTextColor(ContextCompat.getColor(context,
                    AVATAR_TEXT_COLORS[colorIndex]));

            itemView.setOnClickListener(view -> listener.onEdit(student));
            btnMore.setContentDescription(context.getString(
                    R.string.content_desc_more_options) + ": " + student.getFullName());
            btnMore.setOnClickListener(view -> showActions(view, student));
        }

        private void showActions(View anchor, Student student) {
            Context context = anchor.getContext();
            PopupMenu popup = new PopupMenu(context, anchor);
            popup.inflate(R.menu.menu_student_actions);
            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_edit) {
                    listener.onEdit(student);
                    return true;
                }
                if (id == R.id.action_delete) {
                    listener.onDelete(student);
                    return true;
                }
                return false;
            });
            popup.show();
        }
    }
}
