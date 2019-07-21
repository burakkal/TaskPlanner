package com.burakkal.taskplanner.ui.controlpanel;

import android.util.Pair;

import com.burakkal.taskplanner.data.AppDatabase;
import com.burakkal.taskplanner.data.entities.Employee;
import com.burakkal.taskplanner.data.entities.EmployeeTask;
import com.burakkal.taskplanner.data.entities.EmployeeWithAssignedTasks;
import com.burakkal.taskplanner.data.entities.Task;
import com.burakkal.taskplanner.ui.base.BasePresenter;
import com.burakkal.taskplanner.util.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ControlPanelPresenter extends BasePresenter<ControlPanelContract.View>
        implements ControlPanelContract.Presenter {

    AppDatabase db;

    public ControlPanelPresenter(ControlPanelContract.View view, AppDatabase db) {
        super(view);
        this.db = db;
    }

    @Override
    public void start() {
        view.showAssignInfo();
        view.showAssignActions();
        showProgressOnContent();
        addDisposable(loadEmployeeAndTaskCount());
        addDisposable(loadEmployeeWithAssignedTasksSync());
    }

    @NotNull
    private Disposable loadEmployeeWithAssignedTasksSync() {
        return db.employeeDao().getEmployeeWithAssignedTasks()
                .flatMapSingle(employeeWithAssignedTasks -> Flowable.fromIterable(employeeWithAssignedTasks).filter(employeetasks -> !employeetasks.getTasks().isEmpty()).toList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayEmployeeWithAssignedTasks, this::displayError);
    }

    private void displayEmployeeWithAssignedTasks(
            List<EmployeeWithAssignedTasks> employeeWithAssignedTasks) {
        Timber.i("loadEmployeeWithAssignedTasksSync.onSuccess(%d)", employeeWithAssignedTasks.size());
        if (employeeWithAssignedTasks.isEmpty()) view.showNoAssignedTasks();
        else view.showAssignedTasks(employeeWithAssignedTasks);
    }

    private void displayError(Throwable error) {
        Timber.e(error);
        view.showError(error);
    }

    private void showProgressOnContent() {
        view.hideContent();
        view.hideInfoView();
        view.showProgress();
    }

    private void hideProgressOnContent() {
        view.showContent();
        view.hideInfoView();
        view.hideProgress();
    }

    @NotNull
    private Disposable loadEmployeeAndTaskCount() {
        Timber.i("loadEmployeeAndTaskCount()");
        Flowable<Integer> employeeCount = db.employeeDao().getEmployeeCount();
        Flowable<Integer> taskCount = db.taskDao().getTaskCount();
        return Flowable.combineLatest(employeeCount, taskCount, (BiFunction<Integer, Integer, Pair>) Pair::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pair -> {
                    Timber.i("loadEmployeeAndTaskCount.subscribe()");
                    int eCount = (int) pair.first;
                    int tCount = (int) pair.second;
                    if (eCount == 0 || tCount == 0) {
                        view.showNoEnoughData(pair);
                    } else {
                        view.hideNoEnoughData();
                    }
                    view.showEmployeeCount(eCount);
                    view.showTaskCount(tCount);
                });
    }

    @Override
    public void assignTasksToEmployees(int numOfDays) {
        view.showNoEnoughData();
        view.showProgressInfoMessage();
        showProgressOnContent();
        addDisposable(getAllEmployeesObservable()
                .zipWith(getAllTasksObservable(), this::convertToEmployeeTask)
                .doOnSubscribe(disposable -> db.employeeTaskDao().deleteAll())
                .doOnNext(db.employeeTaskDao()::insert)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(employeeTask -> Timber.i("Employee [%d] -> Task [%d]", employeeTask.getEmployee().getId(), employeeTask.getTask().getId()), Timber::e, () ->  onInitialAssignmentComplete(numOfDays - 1)));
    }

    private Observable<Employee> getAllEmployeesObservable() {
        return db.employeeDao().getAllEmployees().flatMapObservable(Observable::fromIterable);
    }

    private Observable<Task> getAllTasksObservable() {
        return db.taskDao().getAllTasks().flatMapObservable(Observable::fromIterable);
    }

    @NotNull
    private EmployeeTask convertToEmployeeTask(Employee employee, Task task) {
        return convertToEmployeeTask(DateUtils.getToday(), employee, task);
    }

    @NotNull
    private EmployeeTask convertToEmployeeTask(long date, Employee employee, Task task) {
        Timber.i("convertToEmployeeTask(long date, Employee employee, Task task)");
        return new EmployeeTask(date, employee, task);
    }

    private void onInitialAssignmentComplete(int numOfDays) {
        Timber.i("onInitialAssignmentComplete(%d)", numOfDays);
        if (numOfDays == 0) {
            onAssignmentComplete();
            return;
        }
        AtomicInteger counter = new AtomicInteger(0);
        addDisposable(getAllEmployeesOrdered()
                .zipWith(getAllTasksOrdered(), (employee, task) -> convertToEmployeeTask(DateUtils.getDayAfter(DateUtils.getToday(), counter.get()), employee, task))
                .doOnSubscribe(disposable -> counter.incrementAndGet())
                .repeat(numOfDays)
                .doOnNext(db.employeeTaskDao()::insert)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(employeeTask -> Timber.i("Employee [%d] -> Task [%d]", employeeTask.getEmployee().getId(), employeeTask.getTask().getId()), Timber::e, this::onAssignmentComplete));
    }

    private void onAssignmentComplete() {
        Timber.i("onAssignmentComplete()");
        view.hideNoEnoughData();
        hideProgressOnContent();
    }

    private Observable<Task> getAllTasksOrdered() {
        return db.taskDao().getAllTasksOrderByAssignedEmployeeCountAndDifficulty()
                .flatMapObservable(Observable::fromIterable);
    }

    private Observable<Employee> getAllEmployeesOrdered() {
        return db.employeeDao().getAllEmployeesOrderByTaskCountAndWorkload()
                .flatMapObservable(Observable::fromIterable);
    }
}
