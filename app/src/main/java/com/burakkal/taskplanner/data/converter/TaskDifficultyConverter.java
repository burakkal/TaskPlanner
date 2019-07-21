package com.burakkal.taskplanner.data.converter;

import com.burakkal.taskplanner.data.entities.TaskDifficulty;

import androidx.room.TypeConverter;

public class TaskDifficultyConverter {

    @TypeConverter
    public static TaskDifficulty toDifficulty(int value) {
        for (TaskDifficulty difficulty :
                TaskDifficulty.values()) {
            if (difficulty.getValue() == value) return difficulty;
        }
        throw new IllegalArgumentException();
    }

    @TypeConverter
    public static int fromDifficulty(TaskDifficulty difficulty) {
        return difficulty.getValue();
    }
}
