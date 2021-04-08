package com.maihealthtracker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maihealthtracker.Models.Record;
import com.shawnlin.numberpicker.NumberPicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class WaterBottomSheet extends BottomSheetDialogFragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String user_id, date_key;

    private int count=0;

    private Calendar calendar;

    private MainActivity activity;

    public WaterBottomSheet(MainActivity activity, Calendar record_calendar, String user_id, String date_key) {
        this.date_key = date_key;
        this.user_id = user_id;
        this.activity = activity;
        this.calendar = record_calendar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_water_dialog_layout,
                container, false);

        ImageView plus = v.findViewById(R.id.plus);
        ImageView minus = v.findViewById(R.id.minus);
        TextView close = v.findViewById(R.id.close);
        TextView water_count = v.findViewById(R.id.water_count);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        DocumentReference bRef = db.collection("Records").document(user_id).
                collection("items").document(date_key);
        bRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document1 = task.getResult();
                    if (document1.exists()) {

                        final Record record = document1.toObject(Record.class);
                        count=record.getWater_count();
                        if (count==1 || count == 0)
                            water_count.setText(count+" Cup");
                        else
                            water_count.setText(count+" Cups");

                    }else{
                        water_count.setText(count+" Cup");
                    }
                }
            }
        });


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;

                db.collection("Records").document(user_id).
                        collection("items").document(date_key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                final HashMap recordMap = new HashMap<>();
                                recordMap.put("water_count", count);

                                db.collection("Records").document(user_id).
                                        collection("items").document(date_key).update(recordMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        activity.refreshRecords();
                                    }
                                });

                            } else {


                                SimpleDateFormat month_pattern = new SimpleDateFormat("MMMM");

                                HashMap new_recordMap = new HashMap<>();
                                new_recordMap.put("record_id", date_key);
                                new_recordMap.put("user_id", user_id);
                                new_recordMap.put("water_count", count);
                                new_recordMap.put("weight", 0);
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
                                    }
                                });



                            }
                        }
                    }
                });

                if (count==1 || count == 0)
                    water_count.setText(count+" Cup");
                else
                    water_count.setText(count+" Cups");

            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count!=0){
                    count--;

                    db.collection("Records").document(user_id).
                            collection("items").document(date_key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {

                                    final HashMap recordMap = new HashMap<>();
                                    recordMap.put("water_count", count);

                                    db.collection("Records").document(user_id).
                                            collection("items").document(date_key).update(recordMap);

                                    activity.refreshRecords();
                                } else {


                                    SimpleDateFormat month_pattern = new SimpleDateFormat("MMMM");

                                    HashMap new_recordMap = new HashMap<>();
                                    new_recordMap.put("record_id", date_key);
                                    new_recordMap.put("user_id", user_id);
                                    new_recordMap.put("water_count", count);
                                    new_recordMap.put("weight", 0);
                                    new_recordMap.put("notes", "");
                                    new_recordMap.put("day", calendar.get(Calendar.DAY_OF_MONTH));
                                    new_recordMap.put("month", calendar.get(Calendar.MONTH));
                                    new_recordMap.put("year", calendar.get(Calendar.YEAR));
                                    new_recordMap.put("month_name", month_pattern.format(calendar.getTime()));
                                    new_recordMap.put("health", 0);


                                    db.collection("Records").document(user_id).
                                            collection("items").document(date_key).set(new_recordMap);
                                    activity.refreshRecords();

                                }
                            }
                        }
                    });

                    if (count==1 || count == 0)
                        water_count.setText(count+" Cup");
                    else
                        water_count.setText(count+" Cups");
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.refreshRecords();
                dismiss();
            }
        });


        return v;
    }
}
