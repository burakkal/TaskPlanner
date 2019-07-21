package com.burakkal.taskplanner.ui.employee;

import com.burakkal.taskplanner.data.entities.Employee;

import java.util.List;

public interface EmployeeContract {

    interface View {
        void showNoEmployee();

        void showEmployees(List<Employee> employees);
    }

    interface Presenter {
        void loadEmployees();

        void addEmployee(String name, String surname);

        void deleteEmployee(Employee employee);

        void editEmployee(Employee employee);
    }
}
