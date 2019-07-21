package com.burakkal.taskplanner.ui.employee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.burakkal.taskplanner.R;
import com.burakkal.taskplanner.data.entities.Employee;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<Employee> employees;
    private OnItemMenuClickListener menuClickListener;

    public EmployeeAdapter(List<Employee> employees, OnItemMenuClickListener menuClickListener) {
        this.employees = employees;
        this.menuClickListener = menuClickListener;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_employee_container, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employees.get(position);

        holder.imgIcon.setImageResource(R.drawable.ic_account_circle_black_24dp);

        holder.tvName.setText(employee.getName());
        holder.tvSurname.setText(employee.getSurname());

        holder.btnEdit.setOnClickListener(v -> {
            if (menuClickListener != null) menuClickListener.onEditClick(employee);
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (menuClickListener != null) menuClickListener.onDeleteClick(employee);
        });
    }

    @Override
    public int getItemCount() {
        return employees == null ? 0 : employees.size();
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
        notifyDataSetChanged();
    }

    public interface OnItemMenuClickListener {
        void onEditClick(Employee employee);

        void onDeleteClick(Employee employee);
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_employee_icon) ImageView imgIcon;
        @BindView(R.id.tv_employee_name) TextView tvName;
        @BindView(R.id.tv_employee_surname) TextView tvSurname;
        @BindView(R.id.imgBtn_employee_edit) ImageButton btnEdit;
        @BindView(R.id.imgBtn_employee_delete) ImageButton btnDelete;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
