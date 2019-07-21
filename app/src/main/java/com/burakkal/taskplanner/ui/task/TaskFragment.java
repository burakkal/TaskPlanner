package com.burakkal.taskplanner.ui.task;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.burakkal.taskplanner.App;
import com.burakkal.taskplanner.R;
import com.burakkal.taskplanner.data.AppDatabase;
import com.burakkal.taskplanner.data.converter.TaskDifficultyConverter;
import com.burakkal.taskplanner.data.entities.Task;
import com.burakkal.taskplanner.data.entities.TaskDifficulty;
import com.burakkal.taskplanner.ui.base.BaseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class TaskFragment extends BaseFragment implements TaskContract.View, TaskAdapter.OnItemMenuClickListener {

    @BindView(R.id.rv_task_list) RecyclerView rvTask;
    @BindView(R.id.fab_add_task) FloatingActionButton fabAddTask;
    @BindView(R.id.tv_info_title) TextView tvInfoTitle;
    @BindView(R.id.tv_info_detail) TextView tvInfoDetail;
    @BindView(R.id.img_info_icon) ImageView imgInfoIcon;

    @Inject AppDatabase database;

    private TaskPresenter presenter;
    private TaskAdapter taskAdapter;

    @Override
    public void onAttach(Context context) {
        App.get(getActivity()).getComponent().inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskAdapter = new TaskAdapter(null, this);
        rvTask.setAdapter(taskAdapter);
        presenter = new TaskPresenter(this, database);
        presenter.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.stop();
    }

    @OnClick(R.id.fab_add_task)
    public void onAddTaskClick() {
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_add_edit, null);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Add task")
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .create();

        EditText etName = view.findViewById(R.id.et_first);
        etName.setHint("Name *");
        etName.requestFocus();
        EditText etSecond = view.findViewById(R.id.et_second);
        etSecond.setVisibility(View.GONE);
        TextView tvSeekLabel = view.findViewById(R.id.tv_seek_first_label);
        tvSeekLabel.setVisibility(View.VISIBLE);
        SeekBar seekDifficulty = view.findViewById(R.id.seek_first);
        seekDifficulty.setVisibility(View.VISIBLE);
        String txtLabelDiff = getString(R.string.label_difficulty) +
                TaskDifficultyConverter
                        .toDifficulty(seekDifficulty.getProgress() + 1)
                        .toString();
        tvSeekLabel.setText(txtLabelDiff);
        seekDifficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String txtLabelDiff = getString(R.string.label_difficulty) +
                        TaskDifficultyConverter
                                .toDifficulty(seekDifficulty.getProgress() + 1)
                                .toString();
                tvSeekLabel.setText(txtLabelDiff);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        dialog.setOnShowListener(d -> {
            AlertDialog alertDialog = (AlertDialog) d;
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
                String name = etName.getText().toString();
                TaskDifficulty difficulty = TaskDifficultyConverter
                        .toDifficulty(seekDifficulty.getProgress() + 1);
                if (name.length() == 0) {
                    Timber.i("Name is required!");
                    etName.setError("Name is required!");
                } else {
                    Timber.i("presenter.addTask(name, difficulty)");
                    presenter.addTask(name, difficulty);
                    dialog.cancel();
                }
            });

            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setOnClickListener(v -> dialog.cancel());
        });

        dialog.show();
    }

    @Override
    public void onEditClick(Task task) {
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_add_edit, null);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Edit task")
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        EditText etName = view.findViewById(R.id.et_first);
        etName.setHint("Name *");
        etName.setText(task.getName());
        etName.requestFocus();
        EditText etSecond = view.findViewById(R.id.et_second);
        etSecond.setVisibility(View.GONE);
        TextView tvSeekLabel = view.findViewById(R.id.tv_seek_first_label);
        tvSeekLabel.setVisibility(View.VISIBLE);
        SeekBar seekDifficulty = view.findViewById(R.id.seek_first);
        seekDifficulty.setVisibility(View.VISIBLE);
        seekDifficulty.setProgress(task.getDifficulty().getValue() - 1);
        String txtLabelDiff = getString(R.string.label_difficulty) +
                TaskDifficultyConverter
                        .toDifficulty(seekDifficulty.getProgress() + 1)
                        .toString();
        tvSeekLabel.setText(txtLabelDiff);
        seekDifficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String txtLabelDiff = getString(R.string.label_difficulty) +
                        TaskDifficultyConverter
                                .toDifficulty(seekDifficulty.getProgress() + 1)
                                .toString();
                tvSeekLabel.setText(txtLabelDiff);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        dialog.setOnShowListener(d -> {
            AlertDialog alertDialog = (AlertDialog) d;
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
                String name = etName.getText().toString();
                TaskDifficulty difficulty = TaskDifficultyConverter
                        .toDifficulty(seekDifficulty.getProgress() + 1);
                if (name.length() == 0) {
                    Timber.i("Name is required!");
                    etName.setError("Name is required!");
                } else {
                    Timber.i("presenter.editTask(task)");
                    task.setName(name);
                    task.setDifficulty(difficulty);
                    presenter.editTask(task);
                    dialog.cancel();
                }
            });

            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setOnClickListener(v -> dialog.cancel());
        });

        dialog.show();
    }

    @Override
    public void onDeleteClick(Task task) {
        presenter.deleteTask(task);
    }

    @Override
    public void showNoTask() {
        Timber.i("showNoTask()");
        rvTask.setVisibility(View.GONE);
        tvInfoTitle.setVisibility(View.VISIBLE);
        tvInfoDetail.setVisibility(View.VISIBLE);
        imgInfoIcon.setVisibility(View.VISIBLE);
        tvInfoTitle.setText(R.string.no_task_title);
        tvInfoDetail.setText(R.string.no_task_detail);
        imgInfoIcon.setImageResource(R.drawable.ic_assignment_black_24dp);
    }

    @Override
    public void showTasks(List<Task> tasks) {
        Timber.i("showTasks()");
        rvTask.setVisibility(View.VISIBLE);
        tvInfoTitle.setVisibility(View.GONE);
        tvInfoDetail.setVisibility(View.GONE);
        imgInfoIcon.setVisibility(View.GONE);
        taskAdapter.setTasks(tasks);
    }
}
