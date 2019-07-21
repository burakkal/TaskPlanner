package com.burakkal.taskplanner.ui.controlpanel;

import android.util.Pair;

import com.burakkal.taskplanner.data.entities.EmployeeWithAssignedTasks;

import java.util.List;

public interface ControlPanelContract {

    interface View {

        void showEmployeeCount(int employeeCount);

        void showTaskCount(int taskCount);

        void hideAssignActions();

        void showAssignActions();

        void hideAssignInfo();

        void showAssignInfo();

        void showNoEnoughData(Pair employeeTaskPair);

        void showNoEnoughData();

        void showProgressInfoMessage();

        void hideNoEnoughData();

        void showAssignedTasks(List<EmployeeWithAssignedTasks> assignedTasks);

        void showNoAssignedTasks();

        void showError(Throwable error);

        void showContent();

        void hideContent();

        void showInfoView();

        void hideInfoView();

        void showProgress();

        void hideProgress();
    }

    interface Presenter {
        void assignTasksToEmployees(int numOfDays);
    }
}
