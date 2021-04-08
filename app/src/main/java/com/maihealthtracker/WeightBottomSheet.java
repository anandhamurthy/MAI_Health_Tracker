package com.maihealthtracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shawnlin.numberpicker.NumberPicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class WeightBottomSheet extends BottomSheetDialogFragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String user_id, date_key;
    private MainActivity activity;
    private Calendar calendar;

    public WeightBottomSheet(MainActivity activity, Calendar record_calendar, String user_id, String date_key) {
        this.date_key = date_key;
        this.user_id = user_id;
        this.activity = activity;
        this.calendar = record_calendar;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_weight_dialog_layout,
                container, false);

        NumberPicker numberPicker = v.findViewById(R.id.kg_counter);
        TextView save = v.findViewById(R.id.save);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("Records").document(user_id).
                        collection("items").document(date_key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                final HashMap recordMap = new HashMap<>();
                                recordMap.put("weight", numberPicker.getValue());

                                db.collection("Records").document(user_id).
                                        collection("items").document(date_key).update(recordMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        activity.refreshRecords();
                                        dismiss();
                                    }
                                });


                            } else {


                                SimpleDateFormat month_pattern = new SimpleDateFormat("MMMM");

                                HashMap new_recordMap = new HashMap<>();
                                new_recordMap.put("record_id", date_key);
                                new_recordMap.put("user_id", user_id);
                                new_recordMap.put("water_count", 0);
                                new_recordMap.put("weight", numberPicker.getValue());
                                new_recordMap.put("notes", "");
                                new_recordMap.put("day", calendar.get(Calendar.DAY_OF_MONTH));
                                new_recordMap.put("month", calendar.get(Calendar.MONTH));
                                new_recordMap.put("year", calendar.get(Calendar.YEAR));
                                new_recordMap.put("month_name", month_pattern.format(calendar.getTime()));
                                new_recordMap.put("health", 0);


                                db.collection("Records").document(user_id).
                                        collection("items").document(date_key).set(new_recordMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        activity.refreshRecords();
                                        dismiss();
                                    }
                                });


                            }
                        }
                    }
                });

            }
        });


        return v;
    }
}
