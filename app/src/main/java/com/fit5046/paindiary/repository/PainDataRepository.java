package com.fit5046.paindiary.repository;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.fit5046.paindiary.dao.PainDataDAO;
import com.fit5046.paindiary.database.PainDataDatabase;
import com.fit5046.paindiary.entity.PainData;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class PainDataRepository {
    private PainDataDAO painDataDAO;
    private LiveData<List<PainData>> allPainData;

    public PainDataRepository(Application application){
        PainDataDatabase db = PainDataDatabase.getInstance(application);
        painDataDAO = db.painDataDAO();
        allPainData = painDataDAO.getAll();
    }
    // Room executes this query on a separate thread
    public LiveData<List<PainData>> getAllPainData() {
        return allPainData;
    }
//    public LiveData<Integer> getPainLocationCount(final String painLocation) { return painDataDAO.countPainLocation(painLocation); }
    public  void insert(final PainData painData){
        PainDataDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painDataDAO.insert(painData);
            }
        });
    }
    public void deleteAll(){
        PainDataDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painDataDAO.deleteAll();
            }
        });
    }
    public void delete(final PainData painData){
        PainDataDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painDataDAO.delete(painData);
            }
        });
    }
    public void updatePainData(final PainData painData) {
        PainDataDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painDataDAO.updatePainData(painData);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainData> findByIDFuture(final int customerId) {
        return CompletableFuture.supplyAsync(new Supplier<PainData>() {
            @Override
            public PainData get() {
                return painDataDAO.findByID(customerId);
            }
        }, PainDataDatabase.databaseWriteExecutor);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainData> findByDateFuture(final String recordDate) {
        return CompletableFuture.supplyAsync(new Supplier<PainData>() {
            @Override
            public PainData get() {
                return painDataDAO.findByDate(recordDate);
            }
        }, PainDataDatabase.databaseWriteExecutor);
    }
}

