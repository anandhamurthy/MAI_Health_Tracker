package com.maihealthtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maihealthtracker.Models.Users;

import java.util.Calendar;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText Name, B, L, S, D;
    private FloatingActionButton Save, B_Time, L_Time, S_Time, D_Time;
    private TextView Email;

    private String B_S_T="", L_S_T="", S_S_T="", D_ST="";

    public static ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mProgressDialog = new ProgressDialog(ProfileActivity.this);
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Loading");
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);

        Name = findViewById(R.id.name);

        B_Time = findViewById(R.id.add_b_time);
        L_Time = findViewById(R.id.add_l_time);
        S_Time = findViewById(R.id.add_s_time);
        D_Time = findViewById(R.id.add_d_time);

        B = findViewById(R.id.breakfast);
        L = findViewById(R.id.lunch);
        S = findViewById(R.id.snack);
        D = findViewById(R.id.dinner);

        Email = findViewById(R.id.email_id);
        Save = findViewById(R.id.save);

        B_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int minute = now.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(ProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        B.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Breakfast Time");
                mTimePicker.show();
            }
        });

        L_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int minute = now.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(ProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        L.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Lunch Time");
                mTimePicker.show();
            }
        });

        S_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int minute = now.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(ProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        S.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Snack Time");
                mTimePicker.show();
            }
        });

        D_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int minute = now.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(ProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        D.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Dinner Time");
                mTimePicker.show();
            }
        });

        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Users users = document.toObject(Users.class);

                        Name.setText(users.getName());
                        Email.setText(users.getEmail_id());
                        B.setText(users.getB_time());
                        S.setText(users.getS_time());
                        L.setText(users.getL_time());
                        D.setText(users.getD_time());

                        mProgressDialog.dismiss();
                    }
                }
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Name.getText().toString();
                String b_time = B.getText().toString();
                String l_time = L.getText().toString();
                String s_time = S.getText().toString();
                String d_time = D.getText().toString();

                if (!b_time.isEmpty() && !l_time.isEmpty() && !s_time.isEmpty() && !d_time.isEmpty() && !name.isEmpty()) {
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("name", name);
                    userMap.put("b_time", b_time);
                    userMap.put("l_time", l_time);
                    userMap.put("s_time", s_time);
                    userMap.put("d_time", d_time);
                    db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ProfileActivity.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }else{
                    Toast.makeText(ProfileActivity.this, "Please set Remainder timing", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}