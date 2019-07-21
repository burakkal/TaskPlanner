package com.burakkal.taskplanner.ui.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<V> {

    protected V view;
    private CompositeDisposable disposables;

    public BasePresenter(V view) {
        this.view = view;
        disposables = new CompositeDisposable();
    }

    public abstract void start();

    public void stop() {
        disposables.clear();
    }

    protected void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }
}
