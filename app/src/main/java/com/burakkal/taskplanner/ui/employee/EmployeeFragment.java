package com.burakkal.taskplanner.ui.employee;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.burakkal.taskplanner.App;
import com.burakkal.taskplanner.R;
import com.burakkal.taskplanner.data.AppDatabase;
import com.burakkal.taskplanner.data.entities.Employee;
import com.burakkal.taskplanner.ui.base.BaseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class EmployeeFragment extends BaseFragment implements EmployeeContract.View,
        EmployeeAdapter.OnItemMenuClickListener {

    @BindView(R.id.rv_employee_list) RecyclerView rvEmployee;
    @BindView(R.id.fab_add_employee) FloatingActionButton fabAddEmployee;
    @BindView(R.id.tv_info_title) TextView tvInfoTitle;
    @BindView(R.id.tv_info_detail) TextView tvInfoDetail;
    @BindView(R.id.img_info_icon) ImageView imgInfoIcon;

    @Inject AppDatabase database;

    private EmployeePresenter presenter;
    private EmployeeAdapter employeeAdapter;

    @Override
    public void onAttach(Context context) {
        App.get(getActivity()).getComponent().inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_employee, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        employeeAdapter = new EmployeeAdapter(null, this);
        rvEmployee.setAdapter(employeeAdapter);
        presenter = new EmployeePresenter(this, database);
        presenter.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.stop();
    }

    @OnClick(R.id.fab_add_employee)
    public void onAddEmployeeClick() {
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_add_edit, null);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Add employee")
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .create();

        EditText etName = view.findViewById(R.id.et_first);
        etName.setHint("Name *");
        EditText etSurname = view.findViewById(R.id.et_second);
        etSurname.setHint("Surname *");
        etName.requestFocus();

        dialog.setOnShowListener(d -> {
            AlertDialog alertDialog = (AlertDialog) d;
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
                String name = etName.getText().toString();
                String surname = etSurname.getText().toString();
                if (name.length() == 0) {
                    Timber.i("Name is required!");
                    etName.setError("Name is required!");
                } else if (surname.length() == 0) {
                    Timber.i("Surname is required!");
                    etSurname.setError("Surname is required!");
                } else {
                    Timber.i("presenter.addEmployee(name, surname)");
                    presenter.addEmployee(name, surname);
                    dialog.cancel();
                }
            });

            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setOnClickListener(v -> dialog.cancel());
        });

        dialog.show();
    }

    @Override
    public void onEditClick(Employee employee) {
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_add_edit, null);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Edit employee")
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        EditText etName = view.findViewById(R.id.et_first);
        etName.setHint("Name *");
        etName.append(employee.getName());
        EditText etSurname = view.findViewById(R.id.et_second);
        etSurname.setHint("Surname *");
        etSurname.append(employee.getSurname());
        etName.requestFocus();

        dialog.setOnShowListener(d -> {
            AlertDialog alertDialog = (AlertDialog) d;
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
                String name = etName.getText().toString();
                String surname = etSurname.getText().toString();
                if (name.length() == 0) {
                    Timber.i("Name is required!");
                    etName.setError("Name is required!");
                } else if (surname.length() == 0) {
                    Timber.i("Surname is required!");
                    etSurname.setError("Surname is required!");
                } else {
                    Timber.i("presenter.editEmployee(name, surname)");
                    employee.setName(name);
                    employee.setSurname(surname);
                    presenter.editEmployee(employee);
                    dialog.cancel();
                }
            });

            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setOnClickListener(v -> dialog.cancel());
        });

        dialog.show();
    }

    @Override
    public void onDeleteClick(Employee employee) {
        presenter.deleteEmployee(employee);
    }

    @Override
    public void showNoEmployee() {
        Timber.i("showNoEmployee()");
        rvEmployee.setVisibility(View.GONE);
        tvInfoTitle.setVisibility(View.VISIBLE);
        tvInfoDetail.setVisibility(View.VISIBLE);
        imgInfoIcon.setVisibility(View.VISIBLE);
        tvInfoTitle.setText(R.string.no_employee_title);
        tvInfoDetail.setText(R.string.no_employee_detail);
        imgInfoIcon.setImageResource(R.drawable.ic_person_black_24dp);
    }

    @Override
    public void showEmployees(List<Employee> employees) {
        Timber.i("showEmployees()");
        rvEmployee.setVisibility(View.VISIBLE);
        tvInfoTitle.setVisibility(View.GONE);
        tvInfoDetail.setVisibility(View.GONE);
        imgInfoIcon.setVisibility(View.GONE);
        employeeAdapter.setEmployees(employees);
    }
}
