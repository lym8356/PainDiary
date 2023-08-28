package com.fit5046.paindiary.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fit5046.paindiary.entity.PainData;
import com.fit5046.paindiary.repository.PainDataRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PainDataViewModel extends AndroidViewModel {

    private PainDataRepository pRepository;
    private LiveData<List<PainData>> allPainData;
    public PainDataViewModel(@NonNull Application application) {
        super(application);
        pRepository = new PainDataRepository(application);
        allPainData = pRepository.getAllPainData();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainData> findByIDFuture(final int dataId){
        return pRepository.findByIDFuture(dataId);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainData> findByDateFuture(final String date){
        return pRepository.findByDateFuture(date);
    }
    public LiveData<List<PainData>> getAllPainData(){
        return allPainData;
    }
    public void insert(PainData painData){
        pRepository.insert(painData);
    }
    public void delete(PainData painData) { pRepository.delete(painData); }
    public void deleteAll(){
        pRepository.deleteAll();
    }
    public void update(PainData painData){
        pRepository.updatePainData(painData);
    }
//    public LiveData<Integer> getPainLocationCount(final String painLocation) { return pRepository.getPainLocationCount(painLocation); }
}
