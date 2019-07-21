package com.burakkal.taskplanner.ui.schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.burakkal.taskplanner.R;
import com.burakkal.taskplanner.data.entities.EmployeeTask;
import com.burakkal.taskplanner.util.DateUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeScheduleAdapter extends RecyclerView.Adapter<EmployeeScheduleAdapter.EmployeeScheduleViewHolder> {

    private List<EmployeeTask> assignedTasks;

    public EmployeeScheduleAdapter(List<EmployeeTask> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }

    @NonNull
    @Override
    public EmployeeScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_schedule_container, parent, false);
        return new EmployeeScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeScheduleViewHolder holder, int position) {
        EmployeeTask assignedTask = assignedTasks.get(position);

        holder.tvDate.setText(DateUtils.toString(assignedTask.getDate()));
        holder.tvTaskName.setText(assignedTask.getTask().getName());
    }

    @Override
    public int getItemCount() {
        return assignedTasks == null ? 0 : assignedTasks.size();
    }

    class EmployeeScheduleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_schedule_date) TextView tvDate;
        @BindView(R.id.tv_schedule_task_name) TextView tvTaskName;

        public EmployeeScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
