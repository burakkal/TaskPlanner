package com.burakkal.taskplanner.ui;

import android.os.Bundle;

import com.burakkal.taskplanner.R;
import com.burakkal.taskplanner.ui.controlpanel.ControlPanelFragment;
import com.burakkal.taskplanner.ui.employee.EmployeeFragment;
import com.burakkal.taskplanner.ui.schedule.EmployeeScheduleFragment;
import com.burakkal.taskplanner.ui.task.TaskFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    enum ActiveFragmentType {
        EMPLOYEE(0), CONTROL_PANEL(1), TASK(2);

        private int value;

        ActiveFragmentType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ActiveFragmentType toActiveFragmentType(int value) {
            for (ActiveFragmentType activeFragmentType :
                    ActiveFragmentType.values()) {
                if (activeFragmentType.getValue() == value) return activeFragmentType;
            }
            throw new IllegalArgumentException();
        }
    }

    public static final String ARGS_ACTIVE_FRAGMENT = "args_active_fragment";

    Fragment employeeFragment = new EmployeeFragment();
    Fragment controlPanelFragment = new ControlPanelFragment();
    Fragment taskFragment = new TaskFragment();
    ActiveFragmentType activeFragmentType = ActiveFragmentType.CONTROL_PANEL;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_employee:
                        Timber.i("navigation_employee");
                        if (activeFragmentType == ActiveFragmentType.EMPLOYEE) return false;
                        showFragment(ActiveFragmentType.EMPLOYEE.toString());
                        activeFragmentType = ActiveFragmentType.EMPLOYEE;
                        return true;
                    case R.id.navigation_control_panel:
                        Timber.i("navigation_control_panel");
                        if (activeFragmentType == ActiveFragmentType.CONTROL_PANEL) return false;
                        showFragment(ActiveFragmentType.CONTROL_PANEL.toString());
                        activeFragmentType = ActiveFragmentType.CONTROL_PANEL;
                        return true;
                    case R.id.navigation_task:
                        Timber.i("navigation_task");
                        if (activeFragmentType == ActiveFragmentType.TASK) return false;
                        showFragment(ActiveFragmentType.TASK.toString());
                        activeFragmentType = ActiveFragmentType.TASK;
                        return true;
                }
                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_control_panel);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            startFragment(employeeFragment, ActiveFragmentType.EMPLOYEE.toString(), true);
            startFragment(taskFragment, ActiveFragmentType.TASK.toString(), true);
            startFragment(controlPanelFragment, ActiveFragmentType.CONTROL_PANEL.toString());
        } else {
            int activeFragmentNum = savedInstanceState.getInt(ARGS_ACTIVE_FRAGMENT);
            activeFragmentType = ActiveFragmentType.toActiveFragmentType(activeFragmentNum);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARGS_ACTIVE_FRAGMENT, activeFragmentType.getValue());
        super.onSaveInstanceState(outState);
    }

    private void startFragment(Fragment fragment, String tag) {
        startFragment(fragment, tag, false);
    }

    private void startFragment(Fragment fragment, String tag, boolean isHidden) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container_main, fragment, tag);
        if (isHidden) ft.hide(fragment);
        ft.commit();
    }

    private void showFragment(String fragmentTagToShow) {
        Fragment fragmentToShow = getSupportFragmentManager().findFragmentByTag(fragmentTagToShow);
        Timber.i("showFragment(%s)", fragmentToShow.getClass().getName());
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : allFragments) {
            if (fragment.getTag().equals(EmployeeScheduleFragment.TAG)) {
                Timber.i("%s == %s", fragment.getTag(), EmployeeScheduleFragment.TAG);
                getSupportFragmentManager().popBackStack();
            } else {
                Timber.i("%s != %s", fragment.getTag(), EmployeeScheduleFragment.TAG);
                ft.hide(fragment);
            }
        }
        ft.show(fragmentToShow).commit();
    }

}
