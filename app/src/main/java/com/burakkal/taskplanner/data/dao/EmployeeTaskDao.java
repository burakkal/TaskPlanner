package com.burakkal.taskplanner.data.dao;

import com.burakkal.taskplanner.data.entities.EmployeeTask;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface EmployeeTaskDao {

    @Insert
    void insert(EmployeeTask employeeTask);

    @Query("DELETE FROM EmployeeTask")
    void deleteAll();
}
