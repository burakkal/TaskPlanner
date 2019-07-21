package com.burakkal.taskplanner.ui.task;

import com.burakkal.taskplanner.data.entities.Task;
import com.burakkal.taskplanner.data.entities.TaskDifficulty;

import java.util.List;

public interface TaskContract {

    interface View {
        void showNoTask();

        void showTasks(List<Task> tasks);
    }

    interface Presenter {
        void loadTasks();

        void addTask(String name, TaskDifficulty difficulty);

        void deleteTask(Task task);

        void editTask(Task task);
    }
}
