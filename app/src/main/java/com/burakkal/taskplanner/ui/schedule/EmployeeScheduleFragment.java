package com.burakkal.taskplanner.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.burakkal.taskplanner.R;
import com.burakkal.taskplanner.data.entities.Employee;
import com.burakkal.taskplanner.data.entities.EmployeeWithAssignedTasks;
import com.burakkal.taskplanner.ui.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class EmployeeScheduleFragment extends BaseFragment {

    public static final String ARGS_ASSIGNED_TASKS = "args_employee_with_assigned_tasks";
    public static final String TAG = "EmployeeScheduleFragment";

    @BindView(R.id.tv_schedule_employee_name) TextView tvScheduleEmployeeName;
    @BindView(R.id.rv_employee_schedule) RecyclerView rvEmployeeSchedule;

    public EmployeeScheduleFragment() {
    }

    public static EmployeeScheduleFragment newInstance(EmployeeWithAssignedTasks assignedTasks) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_ASSIGNED_TASKS, assignedTasks);
        EmployeeScheduleFragment fragment = new EmployeeScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_employee_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EmployeeWithAssignedTasks assignedTasks = (EmployeeWithAssignedTasks)
                getArguments().getSerializable(ARGS_ASSIGNED_TASKS);
        Employee employee = assignedTasks.getEmployee();
        String employeeNameAndSurname = employee.getName() + " " + employee.getSurname();
        tvScheduleEmployeeName.setText(employeeNameAndSurname);
        EmployeeScheduleAdapter employeeScheduleAdapter = new EmployeeScheduleAdapter(assignedTasks.getTasks());
        rvEmployeeSchedule.setAdapter(employeeScheduleAdapter);
    }
}
