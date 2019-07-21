package com.burakkal.taskplanner.di.module;

import android.content.Context;

import com.burakkal.taskplanner.data.AppDatabase;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

@Module(includes = AppContextModule.class)
public class DatabaseModule {

    @Singleton
    @Provides
    AppDatabase provideAppDb(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "taskplanner.db")
                .fallbackToDestructiveMigration()
                .build();
    }
}
