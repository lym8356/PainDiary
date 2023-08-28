package com.fit5046.paindiary.helper;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.fit5046.paindiary.entity.PainData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class DatabaseWorker extends Worker {

    Context context;
    WorkerParameters workerParameters;
    FirebaseDatabase rootNode;
    DatabaseReference dbReference;


    public DatabaseWorker(@NonNull Context context, @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.workerParameters = workerParams;
    }

    private void uploadToFireBase(List<PainData> dataToUpload){

        //get firebase instance
        rootNode = FirebaseDatabase.getInstance();
        //get root reference
        dbReference = rootNode.getReference("pain_data");

        for (int i=0; i<dataToUpload.size(); i++){
            Log.i("onUploadStart", "Upload starting ....");

            //ask the worker to insert into firebase
            dbReference.child(String.valueOf(dataToUpload.get(i).getPid())).setValue(dataToUpload.get(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Log.i("onComplete Successful", "Upload successful");
                    }else{
                        Log.i("onComplete Failed", "Upload failed");
                    }
                }
            });
        }
    }

    @NotNull
    @Override
    public Result doWork() {

        //get data passed from the fragment
        String dataJson = getInputData().getString("list");

        //convert the json data into pain data objects and store in a list
        List<PainData> list = Arrays.asList(new GsonBuilder().create().fromJson(dataJson, PainData[].class));

        //pass the list to the worker
        uploadToFireBase(list);

        return Result.success();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.i("onStopped","Worker has been stopped");
    }
}
