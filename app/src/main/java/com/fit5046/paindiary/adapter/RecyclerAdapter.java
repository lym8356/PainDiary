package com.fit5046.paindiary.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.fit5046.paindiary.R;
import com.fit5046.paindiary.entity.PainData;
import com.fit5046.paindiary.fragment.DailyRecordsFragment;
import com.fit5046.paindiary.helper.CustomSpinnerItem;
import com.fit5046.paindiary.helper.DatePickerHelper;
import com.fit5046.paindiary.viewmodel.PainDataViewModel;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


    private List<PainData> painDataList = new ArrayList<>();
    private DailyRecordsFragment fragment;
    private PainDataViewModel painDataViewModel;
    ArrayList<CustomSpinnerItem> itemList;

    public RecyclerAdapter(DailyRecordsFragment fragment) {
        this.fragment = fragment;
        painDataViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(fragment.getActivity().getApplication()).create(PainDataViewModel.class);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PainData currentData = painDataList.get(position);
        holder.rvDate.setText(currentData.getRecordDate());
        holder.rvPainLocation.setText(currentData.getPainLocation());
        holder.rvPainLevel.setText(String.valueOf(currentData.getPainLevel()));
    }


    @Override
    public int getItemCount() {
        return painDataList.size();
    }

    public void setPainDataList(List<PainData> painDataList) {
        this.painDataList = painDataList;
        notifyDataSetChanged();
    }

    public PainData getPainDataAt(int position) {
        return painDataList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView rvDate;
        private TextView rvPainLocation;
        private TextView rvPainLevel;
        private ImageView imgDelete;
        private ImageView imgEdit;
        private int painLevel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initialize view objects in the recycler view
            rvDate = itemView.findViewById(R.id.rvDate);
            rvPainLocation = itemView.findViewById(R.id.rvPainLocation);
            rvPainLevel = itemView.findViewById(R.id.rvPainLevel);
            imgDelete = itemView.findViewById(R.id.rvDelete);
            imgEdit = itemView.findViewById(R.id.rvEdit);
            imgEdit.setOnClickListener(this);
            imgDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //handle button delete
                case R.id.rvDelete:
                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
                    builder.setMessage("Are you sure you want to delete this record?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PainData dataToDelete = painDataList.get(getBindingAdapterPosition());
                            painDataViewModel.delete(dataToDelete);
                            Toast.makeText(fragment.getActivity(), "Record deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create();
                    builder.show();
                    break;

                case R.id.rvEdit:
                    //get current pain data details and setup dialog values
                    PainData painData = painDataList.get(getBindingAdapterPosition());
                    int pid = painData.getPid();
                    int painLevelDB = painData.getPainLevel();
                    String painLocationDB = painData.getPainLocation();
                    String userMoodDB = painData.getUserMood();
                    int exerciseGoalDB = painData.getUserExerciseGoal();
                    int stepsWalkedDB = painData.getUserStepsWalked();
                    Float dayTemperatureDB = painData.getDayTemperature();
                    Float dayHumidityDB = painData.getDayHumidity();
                    Float dayPressureDB = painData.getDayPressure();
                    String recordDateDB = painData.getRecordDate();
                    Dialog dialog = new Dialog(fragment.getActivity());
                    dialog.setContentView(R.layout.dialog_update);
                    int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;
                    int dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;
                    dialog.getWindow().setLayout(dialogWidth, dialogHeight);
                    dialog.show();

                    //initialize view holders
                    TextInputLayout dialogTemperature = dialog.findViewById(R.id.updateTemperatureTextLayout);
                    TextInputLayout dialogHumidity = dialog.findViewById(R.id.updateHumidityTextLayout);
                    TextInputLayout dialogPressure = dialog.findViewById(R.id.updatePressureTextLayout);
                    TextInputLayout dialogDate = dialog.findViewById(R.id.updateDateTextLayout);
                    TextInputLayout dialogPainLocationLayout = dialog.findViewById(R.id.updatePainLocationLayout);
                    AutoCompleteTextView dialogPainLocation = dialog.findViewById(R.id.updatePainLocation);
                    Slider dialogPainLevel = dialog.findViewById(R.id.updatePainLevel);
                    TextInputLayout dialogUserMoodLayout = dialog.findViewById(R.id.updateMoodLayout);
                    AutoCompleteTextView dialogUserMood = dialog.findViewById(R.id.updateMood);
                    TextInputLayout dialogExerciseGoalLayout = dialog.findViewById(R.id.updateExerciseGoalLayout);
                    TextInputLayout dialogStepsWalkedLayout = dialog.findViewById(R.id.updateStepsWalkedLayout);
                    Button dialogDateBtn = dialog.findViewById(R.id.updateDateBtn);
                    Button dialogCancelBtn = dialog.findViewById(R.id.updateCancelBtn);
                    Button dialogSaveBtn = dialog.findViewById(R.id.updateSaveBtn);

                    //initialize user mood dropdown menu
                    itemList = getCustomList();
                    CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(fragment.getActivity(), itemList);
                    if (dialogUserMood != null) {
                        dialogUserMoodLayout.getEditText().setText(userMoodDB);
                        dialogUserMood.setAdapter(adapter);
                        dialogUserMood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                try {
                                    LinearLayout linearLayout = fragment.getView().findViewById(R.id.customSpinnerItemLayout);
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                CustomSpinnerItem item = (CustomSpinnerItem) adapterView.getItemAtPosition(position);
                                CustomSpinnerAdapter adapter = (CustomSpinnerAdapter) dialogUserMood.getAdapter();
                                dialogUserMood.setAdapter(null);
                                dialogUserMood.setText(item.getSpinnerItemName());
                                dialogUserMood.setAdapter(adapter);
                            }
                        });
                    }

                    //initialize pain location dropdown menu
                    ArrayList<String> painLocationList = new ArrayList<>();
                    painLocationList.add("Back");
                    painLocationList.add("Neck");
                    painLocationList.add("Knees");
                    painLocationList.add("Hips");
                    painLocationList.add("Abdomen");
                    painLocationList.add("Elbows");
                    painLocationList.add("Shoulders");
                    painLocationList.add("Shins");
                    painLocationList.add("Jaw");
                    painLocationList.add("Facial");
                    ArrayAdapter<String> painLocationAdapter = new ArrayAdapter<>(fragment.getActivity(), R.layout.support_simple_spinner_dropdown_item, painLocationList);
                    dialogPainLocationLayout.getEditText().setText(painLocationDB);
                    dialogPainLocation.setAdapter(painLocationAdapter);

                    //set current dialog values using the values from db
                    dialogTemperature.getEditText().setText(String.valueOf(dayTemperatureDB));
                    dialogHumidity.getEditText().setText(String.valueOf(dayHumidityDB));
                    dialogPressure.getEditText().setText(String.valueOf(dayPressureDB));

                    dialogDate.getEditText().setText(recordDateDB);
                    dialogExerciseGoalLayout.getEditText().setText(Integer.toString(exerciseGoalDB));
                    dialogStepsWalkedLayout.getEditText().setText(Integer.toString(stepsWalkedDB));
                    dialogPainLevel.setValue(painLevelDB);

                    //set onclick listener for buttons in dialog
                    dialogDateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment datepickerFragment = new DatePickerHelper(dialog);
                            datepickerFragment.show(fragment.getParentFragmentManager(), "DatePickerHelper");
                        }
                    });

                    //set on change listener for slider
                    dialogPainLevel.addOnChangeListener(new Slider.OnChangeListener() {
                        @Override
                        public void onValueChange(@NonNull @NotNull Slider slider, float value, boolean fromUser) {
                            painLevel = (int) value;
                        }
                    });

                    //set cancel button onclick listner
                    dialogCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialogSaveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //get user input date value
                            String dateData = dialogDate.getEditText().getText().toString();
                            //if it is unchanged, only change the four editable fields
                            if (dateData.equals(recordDateDB)) {
                                //validation for the fields that are intractable to the user
                                if (dialogPainLocationLayout.getEditText().getText().toString().isEmpty()) {
                                    dialogPainLocationLayout.setErrorEnabled(true);
                                    dialogPainLocationLayout.setError("This field cannot be empty.");
                                } else if (dialogUserMoodLayout.getEditText().getText().toString().isEmpty()) {
                                    dialogUserMoodLayout.setErrorEnabled(true);
                                    dialogUserMood.setError("This field cannot be empty.");
                                } else if (dialogExerciseGoalLayout.getEditText().getText().toString().isEmpty()) {
                                    dialogExerciseGoalLayout.setErrorEnabled(true);
                                    dialogExerciseGoalLayout.setError("This field cannot be empty.");
                                } else if (dialogStepsWalkedLayout.getEditText().getText().toString().isEmpty()) {
                                    dialogStepsWalkedLayout.setErrorEnabled(true);
                                    dialogStepsWalkedLayout.setError("This field cannot be empty.");
                                } else {
                                    dialogPainLocationLayout.setError(null);
                                    dialogUserMoodLayout.setError(null);
                                    dialogExerciseGoalLayout.setError(null);
                                    dialogStepsWalkedLayout.setError(null);
                                    dialogPainLocationLayout.setErrorEnabled(false);
                                    dialogUserMoodLayout.setErrorEnabled(false);
                                    dialogExerciseGoalLayout.setErrorEnabled(false);
                                    dialogStepsWalkedLayout.setErrorEnabled(false);

                                    if (android.os.Build.VERSION.SDK_INT >=
                                            android.os.Build.VERSION_CODES.N) {
                                        CompletableFuture<PainData> dataToUpdate = painDataViewModel.findByIDFuture(pid);
                                        dataToUpdate.thenApply(painDataToUpdate -> {
                                            if (painDataToUpdate != null) {
                                                fragment.getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //update data with new data entered by the user
                                                        painDataToUpdate.painLocation = dialogPainLocationLayout.getEditText().getText().toString();
                                                        painDataToUpdate.painLevel = painLevel;
                                                        painDataToUpdate.userExerciseGoal = Integer.parseInt(dialogExerciseGoalLayout.getEditText().getText().toString());
                                                        painDataToUpdate.userStepsWalked = Integer.parseInt(dialogStepsWalkedLayout.getEditText().getText().toString());
                                                        painDataToUpdate.userMood = dialogUserMoodLayout.getEditText().getText().toString();
//                                                        painDataToUpdate.dayTemperature = dialogTemperature.getEditText().getText().toString();
                                                        painDataViewModel.update(painDataToUpdate);
                                                        dialog.dismiss();
                                                        Toast.makeText(fragment.getContext(), "Update was successful", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else {
                                                fragment.getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(fragment.getContext(), "Error: data not found.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                            return true;
                                        });
                                    }
                                }
                                //if the user wants to edit the date field
                            } else {
                                if (android.os.Build.VERSION.SDK_INT >=
                                        android.os.Build.VERSION_CODES.N) {
                                    //run a date search for the input date
                                    //if it exists, do not allow user to set data for this date
                                    CompletableFuture<PainData> dateToChecked = painDataViewModel.findByDateFuture(dateData);
                                    dateToChecked.thenApply(painData -> {
                                        if (painData != null) {
                                            fragment.getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(fragment.getContext(), "Pain data for this date already exists.", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        } else {
                                            //if the entered date does not exist in database
                                            fragment.getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //validations
                                                    if (dialogPainLocationLayout.getEditText().getText().toString().isEmpty()) {
                                                        dialogPainLocationLayout.setErrorEnabled(true);
                                                        dialogPainLocationLayout.setError("This field cannot be empty.");
                                                    } else if (dialogUserMoodLayout.getEditText().getText().toString().isEmpty()) {
                                                        dialogUserMoodLayout.setErrorEnabled(true);
                                                        dialogUserMood.setError("This field cannot be empty.");
                                                    } else if (dialogExerciseGoalLayout.getEditText().getText().toString().isEmpty()) {
                                                        dialogExerciseGoalLayout.setErrorEnabled(true);
                                                        dialogExerciseGoalLayout.setError("This field cannot be empty.");
                                                    } else if (dialogStepsWalkedLayout.getEditText().getText().toString().isEmpty()) {
                                                        dialogStepsWalkedLayout.setErrorEnabled(true);
                                                        dialogStepsWalkedLayout.setError("This field cannot be empty.");
                                                    } else {
                                                        dialogPainLocationLayout.setError(null);
                                                        dialogUserMoodLayout.setError(null);
                                                        dialogExerciseGoalLayout.setError(null);
                                                        dialogStepsWalkedLayout.setError(null);
                                                        dialogPainLocationLayout.setErrorEnabled(false);
                                                        dialogUserMoodLayout.setErrorEnabled(false);
                                                        dialogExerciseGoalLayout.setErrorEnabled(false);
                                                        dialogStepsWalkedLayout.setErrorEnabled(false);

                                                        //get the pain data by id
                                                        CompletableFuture<PainData> dataToUpdate = painDataViewModel.findByIDFuture(pid);
                                                        dataToUpdate.thenApply(painDataToUpdate -> {
                                                            if (painDataToUpdate != null) {
                                                                fragment.getActivity().runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        //update data with new data entered by the user
                                                                        painDataToUpdate.recordDate = dialogDate.getEditText().getText().toString();
                                                                        painDataToUpdate.userMood = dialogUserMoodLayout.getEditText().getText().toString();
                                                                        painDataToUpdate.painLocation = dialogPainLocationLayout.getEditText().getText().toString();
                                                                        painDataToUpdate.painLevel = painLevel;
                                                                        painDataToUpdate.userExerciseGoal = Integer.parseInt(dialogExerciseGoalLayout.getEditText().getText().toString());
                                                                        painDataToUpdate.userStepsWalked = Integer.parseInt(dialogStepsWalkedLayout.getEditText().getText().toString());
                                                                        painDataViewModel.update(painDataToUpdate);
                                                                        dialog.dismiss();
                                                                        Toast.makeText(fragment.getContext(), "Update was successful", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            } else {
                                                                fragment.getActivity().runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Toast.makeText(fragment.getContext(), "Error: data not found.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                            return true;
                                                        });

                                                    }
                                                }
                                            });
                                        }
                                        return true;
                                    });
                                }
                            }
                        }
                    });

                    break;
            }
        }

        //initalize the drop down list with icons
        private ArrayList<CustomSpinnerItem> getCustomList() {
            itemList = new ArrayList<>();
            itemList.add(new CustomSpinnerItem("Very Low", R.drawable.sad));
            itemList.add(new CustomSpinnerItem("Low", R.drawable.unhappy));
            itemList.add(new CustomSpinnerItem("Average", R.drawable.average));
            itemList.add(new CustomSpinnerItem("Good", R.drawable.smile));
            itemList.add(new CustomSpinnerItem("Very Good", R.drawable.happy));
            return itemList;
        }

    }
}
