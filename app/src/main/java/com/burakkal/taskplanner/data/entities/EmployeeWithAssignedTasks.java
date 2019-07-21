package com.burakkal.taskplanner.data.entities;

import java.io.Serializable;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class EmployeeWithAssignedTasks implements Serializable {

    @Embedded private Employee employee;
    @Relation(parentColumn = "id", entityColumn = "employee_id") private List<EmployeeTask> tasks;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<EmployeeTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<EmployeeTask> tasks) {
        this.tasks = tasks;
    }
}
