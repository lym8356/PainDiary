package com.fit5046.paindiary.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fit5046.paindiary.dao.PainDataDAO;
import com.fit5046.paindiary.entity.PainData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PainData.class}, version = 1, exportSchema = false)
public abstract class PainDataDatabase extends RoomDatabase {

    public abstract PainDataDAO painDataDAO();

    private static final String dbName = "PainDataDatabase";

    private static PainDataDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized PainDataDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    PainDataDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
