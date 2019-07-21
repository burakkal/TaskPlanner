package com.burakkal.taskplanner.data;

import com.burakkal.taskplanner.data.dao.EmployeeDao;
import com.burakkal.taskplanner.data.dao.EmployeeTaskDao;
import com.burakkal.taskplanner.data.dao.TaskDao;
import com.burakkal.taskplanner.data.entities.Employee;
import com.burakkal.taskplanner.data.entities.EmployeeTask;
import com.burakkal.taskplanner.data.entities.Task;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Employee.class, Task.class, EmployeeTask.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {

    public abstract EmployeeDao employeeDao();

    public abstract TaskDao taskDao();

    public abstract EmployeeTaskDao employeeTaskDao();
}
