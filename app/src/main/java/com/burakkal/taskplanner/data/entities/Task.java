package com.burakkal.taskplanner.data.entities;

import com.burakkal.taskplanner.data.converter.TaskDifficultyConverter;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class Task {

    @PrimaryKey(autoGenerate = true) private long id;
    private String name;
    @TypeConverters(TaskDifficultyConverter.class) private TaskDifficulty difficulty;

    public Task(String name, TaskDifficulty difficulty) {
        this.name = name;
        this.difficulty = difficulty;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(TaskDifficulty difficulty) {
        this.difficulty = difficulty;
    }
}
