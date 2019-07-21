package com.burakkal.taskplanner.ui.employee;

import com.burakkal.taskplanner.data.AppDatabase;
import com.burakkal.taskplanner.data.entities.Employee;
import com.burakkal.taskplanner.ui.base.BasePresenter;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EmployeePresenter extends BasePresenter<EmployeeContract.View> implements EmployeeContract.Presenter {

    private AppDatabase db;

    public EmployeePresenter(EmployeeContract.View view, AppDatabase db) {
        super(view);
        this.db = db;
    }

    @Override
    public void start() {
        loadEmployees();
    }

    @Override
    public void loadEmployees() {
        Disposable disposable = db.employeeDao().getAllEmployeesSync()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(employees -> {
                    if (employees.isEmpty()) view.showNoEmployee();
                    else view.showEmployees(employees);
                });
        addDisposable(disposable);
    }

    @Override
    public void addEmployee(String name, String surname) {
        Disposable disposable = Completable.fromAction(() -> db.employeeDao().insert(new Employee(name, surname)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        addDisposable(disposable);
    }

    @Override
    public void deleteEmployee(Employee employee) {
        Disposable disposable = Completable.fromAction(() -> db.employeeDao().delete(employee))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        addDisposable(disposable);
    }

    @Override
    public void editEmployee(Employee employee) {
        Disposable disposable = Completable.fromAction(() -> db.employeeDao().update(employee))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        addDisposable(disposable);
    }
}
