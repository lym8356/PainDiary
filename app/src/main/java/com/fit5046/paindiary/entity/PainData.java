package com.fit5046.paindiary.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


/*
    1. id
    2. current date DATE
    3. user email string
    4. pain level int
    5. pain location string
    6. mood with icon string
    7. exercise goal default 10,000 int
    8. total steps taken int
    9. temperature float
    10. humidity float
    11. pressure float

*/

@Entity
public class PainData {
    @PrimaryKey (autoGenerate = true)
    public int pid;

    @ColumnInfo (name = "recordDate")
    @NonNull
    public String recordDate;

    @ColumnInfo (name = "userEmail")
    @NonNull
    public String userEmail;

    @ColumnInfo (name = "painLevel")
    @NonNull
    public int painLevel;

    @ColumnInfo (name = "painLocation")
    @NonNull
    public String painLocation;

    @ColumnInfo (name = "userMood")
    @NonNull
    public String userMood;

    @ColumnInfo (name = "userExerciseGoal")
    @NonNull
    public int userExerciseGoal;

    @ColumnInfo (name = "userStepsWalked")
    @NonNull
    public int userStepsWalked;

    @ColumnInfo (name = "dayTemperature")
    @NonNull
    public float dayTemperature;

    @ColumnInfo (name = "dayHumidity")
    @NonNull
    public float dayHumidity;

    @ColumnInfo (name = "dayPressure")
    @NonNull
    public float dayPressure;

    public int getPid() {
        return pid;
    }

    @NonNull
    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(@NonNull String recordDate) {
        this.recordDate = recordDate;
    }

    @NonNull
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(@NonNull String userEmail) {
        this.userEmail = userEmail;
    }

    public int getPainLevel() {
        return painLevel;
    }

    public void setPainLevel(int painLevel) {
        this.painLevel = painLevel;
    }

    @NonNull
    public String getPainLocation() {
        return painLocation;
    }

    public void setPainLocation(@NonNull String painLocation) {
        this.painLocation = painLocation;
    }

    @NonNull
    public String getUserMood() {
        return userMood;
    }

    public void setUserMood(@NonNull String userMood) {
        this.userMood = userMood;
    }

    public int getUserExerciseGoal() {
        return userExerciseGoal;
    }

    public void setUserExerciseGoal(int userExerciseGoal) {
        this.userExerciseGoal = userExerciseGoal;
    }

    public int getUserStepsWalked() {
        return userStepsWalked;
    }

    public void setUserStepsWalked(int userStepsWalked) {
        this.userStepsWalked = userStepsWalked;
    }

    public float getDayTemperature() {
        return dayTemperature;
    }

    public void setDayTemperature(float dayTemperature) {
        this.dayTemperature = dayTemperature;
    }

    public float getDayHumidity() {
        return dayHumidity;
    }

    public void setDayHumidity(float dayHumidity) {
        this.dayHumidity = dayHumidity;
    }

    public float getDayPressure() {
        return dayPressure;
    }

    public void setDayPressure(float dayPressure) {
        this.dayPressure = dayPressure;
    }

    public PainData(@NonNull String recordDate, @NonNull String userEmail, int painLevel, @NonNull String painLocation, @NonNull String userMood,
                    int userExerciseGoal, int userStepsWalked, float dayTemperature, float dayHumidity, float dayPressure) {
        this.recordDate = recordDate;
        this.userEmail = userEmail;
        this.painLevel = painLevel;
        this.painLocation = painLocation;
        this.userMood = userMood;
        this.userExerciseGoal = userExerciseGoal;
        this.userStepsWalked = userStepsWalked;
        this.dayTemperature = dayTemperature;
        this.dayHumidity = dayHumidity;
        this.dayPressure = dayPressure;
    }
}
