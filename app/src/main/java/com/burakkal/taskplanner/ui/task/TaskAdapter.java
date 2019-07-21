package com.burakkal.taskplanner.ui.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.burakkal.taskplanner.R;
import com.burakkal.taskplanner.data.entities.Task;
import com.google.android.material.chip.Chip;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private OnItemMenuClickListener menuClickListener;
    private ColorGenerator colorGenerator = ColorGenerator.DEFAULT;

    public TaskAdapter(List<Task> tasks, OnItemMenuClickListener menuClickListener) {
        this.tasks = tasks;
        this.menuClickListener = menuClickListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_task_container, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        TextDrawable textDrawable = TextDrawable.builder()
                .buildRound(task.getDifficulty().getValue() + "",
                        colorGenerator.getColor(task.getDifficulty().toString()));
        holder.imgIcon.setImageDrawable(textDrawable);

        holder.tvName.setText(task.getName());
        holder.chipDifficulty.setText(task.getDifficulty().toString());

        holder.btnEdit.setOnClickListener(v -> {
            if (menuClickListener != null) menuClickListener.onEditClick(task);
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (menuClickListener != null) menuClickListener.onDeleteClick(task);
        });
    }

    @Override
    public int getItemCount() {
        return tasks == null ? 0 : tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public interface OnItemMenuClickListener {
        void onEditClick(Task task);

        void onDeleteClick(Task task);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_task_icon) ImageView imgIcon;
        @BindView(R.id.tv_task_name) TextView tvName;
        @BindView(R.id.chip_task_difficulty) Chip chipDifficulty;
        @BindView(R.id.imgBtn_task_edit) ImageButton btnEdit;
        @BindView(R.id.imgBtn_task_delete) ImageButton btnDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
