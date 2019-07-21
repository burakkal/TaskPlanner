package com.burakkal.taskplanner.ui.task;

import com.burakkal.taskplanner.data.AppDatabase;
import com.burakkal.taskplanner.data.entities.Task;
import com.burakkal.taskplanner.data.entities.TaskDifficulty;
import com.burakkal.taskplanner.ui.base.BasePresenter;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TaskPresenter extends BasePresenter<TaskContract.View>
        implements TaskContract.Presenter {

    private AppDatabase db;

    public TaskPresenter(TaskContract.View view, AppDatabase db) {
        super(view);
        this.db = db;
    }

    @Override
    public void start() {
        loadTasks();
    }

    @Override
    public void loadTasks() {
        Disposable disposable = db.taskDao().getAllTasksSync()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tasks -> {
                    if (tasks.isEmpty()) view.showNoTask();
                    else view.showTasks(tasks);
                }, Timber::e);
        addDisposable(disposable);
    }

    @Override
    public void addTask(String name, TaskDifficulty difficulty) {
        Disposable disposable = Completable
                .fromAction(() -> db.taskDao().insert(new Task(name, difficulty)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        addDisposable(disposable);
    }

    @Override
    public void deleteTask(Task task) {
        Disposable disposable = Completable.fromAction(() -> db.taskDao().delete(task))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        addDisposable(disposable);
    }

    @Override
    public void editTask(Task task) {
        Disposable disposable = Completable.fromAction(() -> db.taskDao().update(task))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, Timber::e);
        addDisposable(disposable);
    }
}
