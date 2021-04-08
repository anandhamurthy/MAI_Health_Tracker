package com.maihealthtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Calendar;
import java.util.HashMap;

public class EditDeleteBottomSheet extends BottomSheetDialogFragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String user_id, date_key;

    private MainActivity activity;

    private Calendar calendar;

    public EditDeleteBottomSheet(MainActivity activity, Calendar record_calendar, String user_id, String date_key) {
        this.date_key = date_key;
        this.user_id = user_id;
        this.activity = activity;
        this.calendar = record_calendar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_delete_dialog_layout,
                container, false);

        LinearLayout edit = v.findViewById(R.id.edit_title);
        LinearLayout delete = v.findViewById(R.id.delete_title);


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("Records").document(user_id).
                        collection("items").document(date_key).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            activity.refreshRecords();
                            dismiss();
                        }
                    }
                });

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.add_dialog_layout, null);
                Button weight = mView.findViewById(R.id.weight);
                Button water = mView.findViewById(R.id.water);
                Button breakfast = mView.findViewById(R.id.breakfast);
                Button lunch = mView.findViewById(R.id.lunch);
                Button snack = mView.findViewById(R.id.snack);
                Button dinner = mView.findViewById(R.id.dinner);

                mBuilder.setView(mView);
                final AlertDialog inner_dialog = mBuilder.create();
                inner_dialog.setCanceledOnTouchOutside(true);
                inner_dialog.show();
                weight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        WeightBottomSheet bottomSheet = new WeightBottomSheet(activity, calendar, user_id, date_key);
                        bottomSheet.show(getFragmentManager(),
                                "ModalBottomSheet");
                        inner_dialog.dismiss();

                    }
                });

                water.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        WaterBottomSheet bottomSheet = new WaterBottomSheet(activity, calendar, user_id, date_key);
                        bottomSheet.show(getFragmentManager(),
                                "ModalBottomSheet");
                        inner_dialog.dismiss();

                    }
                });

                breakfast.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(v.getContext(), AddRecordActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("date_key", date_key);
                        intent.putExtra("title", "Edit");
                        intent.putExtra("id", "breakfast");
                        intent.putExtra("calendar", calendar);
                        v.getContext().startActivity(intent);
                        inner_dialog.dismiss();
                    }
                });

                lunch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(v.getContext(), AddRecordActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("date_key", date_key);
                        intent.putExtra("title", "Edit");
                        intent.putExtra("id", "lunch");
                        intent.putExtra("calendar", calendar);
                        v.getContext().startActivity(intent);
                        inner_dialog.dismiss();
                    }
                });

                snack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(v.getContext(), AddRecordActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("date_key", date_key);
                        intent.putExtra("title", "Edit");
                        intent.putExtra("id", "snack");
                        intent.putExtra("calendar", calendar);
                        v.getContext().startActivity(intent);
                        inner_dialog.dismiss();
                    }
                });

                dinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(v.getContext(), AddRecordActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("date_key", date_key);
                        intent.putExtra("title", "Edit");
                        intent.putExtra("id", "dinner");
                        intent.putExtra("calendar", calendar);
                        v.getContext().startActivity(intent);
                        inner_dialog.dismiss();
                    }
                });

                dismiss();
            }
        });


        return v;
    }
}
