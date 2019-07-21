package com.burakkal.taskplanner.data.dao;

import com.burakkal.taskplanner.data.entities.Employee;
import com.burakkal.taskplanner.data.entities.EmployeeWithAssignedTasks;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface EmployeeDao {

    @Query("SELECT * FROM Employee")
    Flowable<List<Employee>> getAllEmployeesSync();

    @Query("SELECT * FROM Employee ORDER BY random()")
    Single<List<Employee>> getAllEmployees();

    @Query("SELECT Employee.* FROM Employee " +
            "LEFT JOIN EmployeeTask on Employee.id = EmployeeTask.employee_id " +
            "LEFT JOIN Task on Task.id = EmployeeTask.task_id " +
            "GROUP BY Employee.id " +
            "ORDER BY count(Task.id) asc, sum(Task.difficulty) asc, random()")
    Single<List<Employee>> getAllEmployeesOrderByTaskCountAndWorkload();

    @Query("SELECT * FROM Employee")
    Flowable<List<EmployeeWithAssignedTasks>> getEmployeeWithAssignedTasks();

    @Query("SELECT count(id) FROM Employee")
    Flowable<Integer> getEmployeeCount();

    @Update
    void update(Employee employee);

    @Insert
    void insert(Employee employee);

    @Delete
    void delete(Employee employee);
}
