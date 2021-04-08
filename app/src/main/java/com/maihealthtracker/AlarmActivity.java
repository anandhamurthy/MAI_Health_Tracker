package com.maihealthtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maihealthtracker.Models.Users;

import java.util.Calendar;
import java.util.HashMap;

public class AlarmActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String B_Time="", L_Time="", S_Time="", D_Time="";

    private ProgressDialog mProgressDialog;

    private ImageView back;
    private TextView title;

    private SwitchMaterial b_remainder, l_remainder, s_remainder, d_remainder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setCanceledOnTouchOutside(false);

        title = findViewById(R.id.title);
        back = findViewById(R.id.back);

        b_remainder = findViewById(R.id.b_remainder);
        l_remainder = findViewById(R.id.l_remainder);
        s_remainder = findViewById(R.id.s_remainder);
        d_remainder = findViewById(R.id.d_remainder);

        back.setVisibility(View.VISIBLE);
        title.setText("Remainders");

        LottieAnimationView anim = findViewById(R.id.clock);
        anim.setAnimation(R.raw.water);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getDetails();

        b_remainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (b_remainder.isChecked()){
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("b_alarm", true);
                    db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                getDetails();
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR, Integer.parseInt(B_Time.split(":")[0]));
                                calendar.set(Calendar.MINUTE, Integer.parseInt(B_Time.split(":")[1]));
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MILLISECOND, 0);
                                setAlarm(calendar, 1);
                                Toast.makeText(AlarmActivity.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("b_alarm", false);
                    db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                getDetails();
                                cancelAlarm(1);
                                Toast.makeText(AlarmActivity.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        l_remainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (l_remainder.isChecked()){
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("l_alarm", true);
                    db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                getDetails();
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR, Integer.parseInt(L_Time.split(":")[0]));
                                calendar.set(Calendar.MINUTE, Integer.parseInt(L_Time.split(":")[1]));
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MILLISECOND, 0);
                                setAlarm(calendar, 2);
                                Toast.makeText(AlarmActivity.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("l_alarm", false);
                    db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                getDetails();
                                cancelAlarm(2);
                                Toast.makeText(AlarmActivity.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        s_remainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s_remainder.isChecked()){
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("s_alarm", true);
                    db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                getDetails();
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR, Integer.parseInt(S_Time.split(":")[0]));
                                calendar.set(Calendar.MINUTE, Integer.parseInt(S_Time.split(":")[1]));
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MILLISECOND, 0);
                                setAlarm(calendar, 3);
                                Toast.makeText(AlarmActivity.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("s_alarm", false);
                    db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                getDetails();
                                cancelAlarm(3);
                                Toast.makeText(AlarmActivity.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        d_remainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (d_remainder.isChecked()){
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("d_alarm", true);
                    db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR, Integer.parseInt(D_Time.split(":")[0]));
                                calendar.set(Calendar.MINUTE, Integer.parseInt(D_Time.split(":")[1]));
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MILLISECOND, 0);
                                setAlarm(calendar, 4);
                                getDetails();
                                Toast.makeText(AlarmActivity.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("d_alarm", false);
                    db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                getDetails();
                                cancelAlarm(4);
                                Toast.makeText(AlarmActivity.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void getDetails(){
        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Users users = document.toObject(Users.class);

                        B_Time=users.getB_time();
                        L_Time=users.getL_time();
                        S_Time=users.getS_time();
                        D_Time=users.getD_time();
                        b_remainder.setChecked(users.isB_alarm());
                        l_remainder.setChecked(users.isL_alarm());
                        s_remainder.setChecked(users.isS_alarm());
                        d_remainder.setChecked(users.isD_alarm());

                        mProgressDialog.dismiss();
                    }
                }
            }
        });
    }

    private void setAlarm(Calendar calendar, int code){

        Intent intent = new Intent(AlarmActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, code,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    private void cancelAlarm(int code){
        Intent intent = new Intent(AlarmActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, code,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
    }
}