package com.burakkal.taskplanner.di.component;

import com.burakkal.taskplanner.di.module.AppContextModule;
import com.burakkal.taskplanner.di.module.DatabaseModule;
import com.burakkal.taskplanner.ui.controlpanel.ControlPanelFragment;
import com.burakkal.taskplanner.ui.employee.EmployeeFragment;
import com.burakkal.taskplanner.ui.task.TaskFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppContextModule.class, DatabaseModule.class})
public interface AppComponent {

    void inject(EmployeeFragment employeeFragment);
    void inject(TaskFragment taskFragment);
    void inject(ControlPanelFragment controlPanelFragment);
}
