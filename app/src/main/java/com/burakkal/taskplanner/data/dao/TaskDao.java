package com.burakkal.taskplanner.data.dao;

import com.burakkal.taskplanner.data.entities.Task;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM Task")
    Flowable<List<Task>> getAllTasksSync();

    @Query("SELECT * FROM Task ORDER BY random()")
    Single<List<Task>> getAllTasks();

    @Query("SELECT Task.* FROM Task " +
            "LEFT JOIN EmployeeTask on Task.id = EmployeeTask.task_id " +
            "LEFT JOIN Employee on Employee.id = EmployeeTask.employee_id " +
            "GROUP BY Task.id " +
            "ORDER BY count(Employee.id) asc, Task.difficulty desc, random()")
    Single<List<Task>> getAllTasksOrderByAssignedEmployeeCountAndDifficulty();

    @Query("SELECT count(id) FROM Task")
    Flowable<Integer> getTaskCount();

    @Update
    void update(Task task);

    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);
}
