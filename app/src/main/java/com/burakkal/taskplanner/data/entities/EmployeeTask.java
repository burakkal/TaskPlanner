package com.burakkal.taskplanner.data.entities;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = Employee.class,
                parentColumns = "id",
                childColumns = "employee_id",
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Task.class,
                parentColumns = "id",
                childColumns = "task_id",
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE)})
public class EmployeeTask {

    @PrimaryKey(autoGenerate = true) private long id;
    private long date;
    @Embedded(prefix = "employee_") private Employee employee;
    @Embedded(prefix = "task_") private Task task;

    public EmployeeTask(long date, Employee employee, Task task) {
        this.date = date;
        this.employee = employee;
        this.task = task;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
