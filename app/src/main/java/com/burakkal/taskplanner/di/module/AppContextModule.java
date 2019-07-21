package com.burakkal.taskplanner.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppContextModule {

    private Context context;

    public AppContextModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Singleton
    @Provides
    Context provideAppContext() {
        return context;
    }
}
