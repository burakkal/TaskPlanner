package com.burakkal.taskplanner.ui.controlpanel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.burakkal.taskplanner.R;
import com.burakkal.taskplanner.data.entities.Employee;
import com.burakkal.taskplanner.data.entities.EmployeeTask;
import com.burakkal.taskplanner.data.entities.EmployeeWithAssignedTasks;
import com.google.android.material.chip.Chip;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ControlPanelAdapter extends RecyclerView.Adapter<ControlPanelAdapter.ControlPanelViewHolder> {

    private List<EmployeeWithAssignedTasks> employeeWithAssignedTasks;
    private ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
    private OnItemClickListener itemClickListener;

    public ControlPanelAdapter(List<EmployeeWithAssignedTasks> employeeWithAssignedTasks,
                               OnItemClickListener itemClickListener) {
        this.employeeWithAssignedTasks = employeeWithAssignedTasks;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ControlPanelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_control_panel_container, parent, false);
        return new ControlPanelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ControlPanelViewHolder holder, int position) {
        EmployeeWithAssignedTasks employeeWithTask = employeeWithAssignedTasks.get(position);
        Employee employee = employeeWithTask.getEmployee();
        List<EmployeeTask> employeeTaskList = employeeWithTask.getTasks();

        TextDrawable textDrawable = TextDrawable.builder()
                .buildRoundRect(employee.getName().charAt(0) + "" + employee.getSurname().charAt(0),
                        colorGenerator.getColor(employee.getName() + employee.getSurname()),
                        12);
        holder.imgIcon.setImageDrawable(textDrawable);

        holder.tvEmployeeName.setText(employee.getName());
        holder.tvEmployeeSurname.setText(employee.getSurname());
        holder.chipAssignedTasks.setText("Assigned Tasks: " + employeeTaskList.size());

        int workload = 0;
        for (EmployeeTask employeeTask : employeeTaskList) {
            workload += employeeTask.getTask().getDifficulty().getValue();
        }
        holder.chipWorkload.setText("Workload: " + workload);

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) itemClickListener.onItemClick(employeeWithTask);
        });
    }

    @Override
    public int getItemCount() {
        return employeeWithAssignedTasks == null ? 0 : employeeWithAssignedTasks.size();
    }

    public void setEmployeeWithAssignedTasks(List<EmployeeWithAssignedTasks> employeeWithAssignedTasks) {
        this.employeeWithAssignedTasks = employeeWithAssignedTasks;
        notifyDataSetChanged();
    }

    interface OnItemClickListener {
        void onItemClick(EmployeeWithAssignedTasks employeeTasks);
    }

    class ControlPanelViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_employee_icon) ImageView imgIcon;
        @BindView(R.id.tv_employee_name) TextView tvEmployeeName;
        @BindView(R.id.tv_employee_surname) TextView tvEmployeeSurname;
        @BindView(R.id.chip_assigned_tasks) Chip chipAssignedTasks;
        @BindView(R.id.chip_workload) Chip chipWorkload;

        public ControlPanelViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
