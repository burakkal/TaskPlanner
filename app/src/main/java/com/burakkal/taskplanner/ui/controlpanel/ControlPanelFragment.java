package com.burakkal.taskplanner.ui.controlpanel;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.burakkal.taskplanner.App;
import com.burakkal.taskplanner.R;
import com.burakkal.taskplanner.data.AppDatabase;
import com.burakkal.taskplanner.data.entities.EmployeeWithAssignedTasks;
import com.burakkal.taskplanner.ui.base.BaseFragment;
import com.burakkal.taskplanner.ui.schedule.EmployeeScheduleFragment;
import com.google.android.material.chip.Chip;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class ControlPanelFragment extends BaseFragment implements ControlPanelContract.View, ControlPanelAdapter.OnItemClickListener {

    @BindView(R.id.group_of_assign_info) Group groupOfAssignInfo;
    @BindView(R.id.chip_num_of_employees) Chip chipEmployeeCount;
    @BindView(R.id.chip_num_of_tasks) Chip chipTaskCount;
    @BindView(R.id.tv_label_assign_action_first) TextView tvLabelAssignActionFirst;
    @BindView(R.id.btn_assign_days_minus) Button btnAssignDaysMinus;
    @BindView(R.id.tv_assign_task_num_of_days) TextView tvAssignTaskNumOfDays;
    @BindView(R.id.btn_assign_days_plus) Button btnAssignDaysPlus;
    @BindView(R.id.tv_label_assign_action_second) TextView tvLabelAssignActionSecond;
    @BindView(R.id.divider_below_content) View dividerBelowContent;
    @BindView(R.id.btn_assign_tasks) Button btnAssignTasks;
    @BindView(R.id.img_assign_tasks_locked_icon) ImageView imgAssignTasksLockedIcon;
    @BindView(R.id.tv_assign_tasks_locked_info) TextView tvAssignTasksLockedInfo;
    @BindView(R.id.pb_assign_tasks_locked_progress) ProgressBar pbAssignTasksLocked;
    @BindView(R.id.rv_control_panel_employees) RecyclerView rvControlPanel;

    // -- Related to info_view

    @BindView(R.id.tv_info_title) TextView tvInfoTitle;
    @BindView(R.id.tv_info_detail) TextView tvInfoDetail;
    @BindView(R.id.img_info_icon) ImageView imgInfoIcon;

    // -- Related to progress_view

    @BindView(R.id.pb_loading) ProgressBar pbLoading;
    @BindView(R.id.tv_loading_info) TextView tvLoadingInfo;

    @Inject AppDatabase database;

    private ControlPanelPresenter presenter;
    private ControlPanelAdapter controlPanelAdapter;

    @Override
    public void onAttach(Context context) {
        App.get(getActivity()).getComponent().inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_control_panel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controlPanelAdapter = new ControlPanelAdapter(null, this);
        rvControlPanel.setAdapter(controlPanelAdapter);
        presenter = new ControlPanelPresenter(this, database);
        presenter.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.stop();
    }

    @OnClick(R.id.btn_assign_days_minus)
    void onMinusClick() {
        int numOfDays = getNumOfDays();
        if (numOfDays == 1) return;
        String strNumOfDays = numOfDays - 1 + "";
        tvAssignTaskNumOfDays.setText(strNumOfDays);
    }

    private int getNumOfDays() {
        return Integer.parseInt(tvAssignTaskNumOfDays.getText().toString());
    }

    @OnClick(R.id.btn_assign_days_plus)
    void onPlusClick() {
        int numOfDays = getNumOfDays();
        if (numOfDays == 30) return;
        String strNumOfDays = numOfDays + 1 + "";
        tvAssignTaskNumOfDays.setText(strNumOfDays);
    }

    @OnClick(R.id.btn_assign_tasks)
    void onAssignTaskClick() {
        int numOfDays = getNumOfDays();
        presenter.assignTasksToEmployees(numOfDays);
    }

    @Override
    public void showEmployeeCount(int employeeCount) {
        Timber.i("showEmployeeCount(%d)", employeeCount);
        String text = employeeCount + "";
        chipEmployeeCount.setText(text);
    }

    @Override
    public void showTaskCount(int taskCount) {
        Timber.i("showTaskCount(%d)", taskCount);
        String text = taskCount + "";
        chipTaskCount.setText(text);
    }

    @Override
    public void hideAssignActions() {
        Timber.i("hideAssignActions()");
        dividerBelowContent.setVisibility(View.GONE);
        tvLabelAssignActionFirst.setVisibility(View.GONE);
        btnAssignDaysMinus.setVisibility(View.GONE);
        tvAssignTaskNumOfDays.setVisibility(View.GONE);
        btnAssignDaysPlus.setVisibility(View.GONE);
        tvLabelAssignActionSecond.setVisibility(View.GONE);
        btnAssignTasks.setVisibility(View.GONE);
    }

    @Override
    public void showAssignActions() {
        Timber.i("showAssignActions()");
        dividerBelowContent.setVisibility(View.VISIBLE);
        tvLabelAssignActionFirst.setVisibility(View.VISIBLE);
        btnAssignDaysMinus.setVisibility(View.VISIBLE);
        tvAssignTaskNumOfDays.setVisibility(View.VISIBLE);
        btnAssignDaysPlus.setVisibility(View.VISIBLE);
        tvLabelAssignActionSecond.setVisibility(View.VISIBLE);
        btnAssignTasks.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAssignInfo() {
        Timber.i("hideAssignInfo()");
        groupOfAssignInfo.setVisibility(View.GONE);
    }

    @Override
    public void showAssignInfo() {
        Timber.i("showAssignInfo()");
        groupOfAssignInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoEnoughData(Pair employeeTaskPair) {
        Timber.i("showNoEnoughData(employeeTaskPair)");
        hideAssignActions();
        dividerBelowContent.setVisibility(View.VISIBLE);
        imgAssignTasksLockedIcon.setVisibility(View.VISIBLE);
        tvAssignTasksLockedInfo.setVisibility(View.VISIBLE);
        btnAssignTasks.setVisibility(View.VISIBLE);
        btnAssignTasks.setEnabled(false);
        String txtInfo;
        int employeeCount = (int) employeeTaskPair.first;
        int taskCount = (int) employeeTaskPair.second;
        if (employeeCount == 0 && taskCount == 0) {
            txtInfo = "You don't have enough employee and task. Add some employee and task to assign.";
        } else if (employeeCount == 0) {
            txtInfo = "You don't have enough employee. Add some employee to assign.";
        } else if (taskCount == 0) {
            txtInfo = "You don't have enough task. Add some task to assign.";
        } else {
            txtInfo = "Something went wrong.";
        }
        tvAssignTasksLockedInfo.setText(txtInfo);
    }

    @Override
    public void showNoEnoughData() {
        Timber.i("showNoEnoughData()");
        hideAssignActions();
        dividerBelowContent.setVisibility(View.VISIBLE);
        imgAssignTasksLockedIcon.setVisibility(View.VISIBLE);
        tvAssignTasksLockedInfo.setVisibility(View.VISIBLE);
        btnAssignTasks.setVisibility(View.VISIBLE);
        btnAssignTasks.setEnabled(false);
    }

    @Override
    public void showProgressInfoMessage() {
        imgAssignTasksLockedIcon.setVisibility(View.GONE);
        pbAssignTasksLocked.setVisibility(View.VISIBLE);
        tvAssignTasksLockedInfo.setText("Please wait until progress finish.");
    }

    @Override
    public void hideNoEnoughData() {
        Timber.i("hideNoEnoughData()");
        showAssignActions();
        imgAssignTasksLockedIcon.setVisibility(View.GONE);
        tvAssignTasksLockedInfo.setVisibility(View.GONE);
        pbAssignTasksLocked.setVisibility(View.GONE);
        tvLabelAssignActionFirst.setText("Assign tasks for");
        tvLabelAssignActionSecond.setText("day.");
        btnAssignTasks.setEnabled(true);
    }

    @Override
    public void showAssignedTasks(List<EmployeeWithAssignedTasks> assignedTasks) {
        hideProgress();
        hideInfoView();
        showContent();
        controlPanelAdapter.setEmployeeWithAssignedTasks(assignedTasks);
    }

    @Override
    public void showNoAssignedTasks() {
        hideContent();
        hideProgress();
        showInfoView();
        tvInfoTitle.setText("No assigned task!");
        tvInfoDetail.setText("Let's assign some tasks to every single employee from 'assign' button below.");
    }

    @Override
    public void showError(Throwable error) {
        hideContent();
        hideProgress();
        showInfoView();
        imgInfoIcon.setVisibility(View.GONE);
        tvInfoDetail.setVisibility(View.GONE);
        tvInfoTitle.setText(error.getMessage());
    }

    @Override
    public void showContent()  {
        rvControlPanel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContent() {
        rvControlPanel.setVisibility(View.GONE);
    }

    @Override
    public void showInfoView() {
        tvInfoTitle.setVisibility(View.VISIBLE);
        tvInfoDetail.setVisibility(View.VISIBLE);
        imgInfoIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInfoView() {
        tvInfoTitle.setVisibility(View.GONE);
        tvInfoDetail.setVisibility(View.GONE);
        imgInfoIcon.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        pbLoading.setVisibility(View.VISIBLE);
        tvLoadingInfo.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        pbLoading.setVisibility(View.GONE);
        tvLoadingInfo.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(EmployeeWithAssignedTasks employeeTasks) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .hide(ControlPanelFragment.this)
                .add(R.id.fragment_container_main, EmployeeScheduleFragment.newInstance(employeeTasks), EmployeeScheduleFragment.TAG)
                .addToBackStack(null)
                .commit();
    }
}
