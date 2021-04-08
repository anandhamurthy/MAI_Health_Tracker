package com.maihealthtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends AppCompatActivity {

    private EditText Name, Email_Id, Password, Confirm_Password;
    private TextView Forgot_Password;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ProgressDialog mProgressDialog;
    private FloatingActionButton Register;
    private TextView Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

        Login = findViewById(R.id.login);
        Register = findViewById(R.id.register);
        Name = findViewById(R.id.name);
        Email_Id = findViewById(R.id.email_id);
        Password = findViewById(R.id.password);
        Confirm_Password = findViewById(R.id.confirm_password);
        Forgot_Password = findViewById(R.id.forgot_password);

        mProgressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = Name.getText().toString();
                final String email_id = Email_Id.getText().toString().trim();
                final String password = Password.getText().toString().trim();
                final String confirm_pass = Confirm_Password.getText().toString();

                if (isEmpty(name, email_id, password, confirm_pass)) {

                    if (password.length() > 5 && confirm_pass.length() > 5) {

                        if (password.equals(confirm_pass)) {

                            mProgressDialog.setTitle("Registering");
                            mProgressDialog.setMessage("Creating User..");
                            mProgressDialog.show();
                            mProgressDialog.setCanceledOnTouchOutside(false);

                            mAuth.createUserWithEmailAndPassword(email_id, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {


                                        String device_token = FirebaseInstanceId.getInstance().getToken();

                                        HashMap userMap = new HashMap<>();
                                        userMap.put("email_id", email_id);
                                        userMap.put("name", name);
                                        userMap.put("b_time", "");
                                        userMap.put("l_time", "");
                                        userMap.put("s_time", "");
                                        userMap.put("d_time", "");
                                        userMap.put("b_alarm", false);
                                        userMap.put("l_alarm", false);
                                        userMap.put("s_alarm", false);
                                        userMap.put("d_alarm", false);
                                        userMap.put("user_id", mAuth.getCurrentUser().getUid());
                                        userMap.put("device_token", device_token);

                                        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                        String uid = current_user.getUid();

                                        db.collection("Users").document(uid).set(userMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        mProgressDialog.dismiss();
                                                        Intent mainIntent = new Intent(RegisterActivity.this, ProfileActivity.class);
                                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(mainIntent);
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                                        Log.d(TAG, e.toString());
                                                    }
                                                });


                                    } else {

                                        mProgressDialog.dismiss();
                                        String error = task.getException().getMessage();
                                        Toast.makeText(RegisterActivity.this, "Error : " + error, Toast.LENGTH_LONG).show();


                                    }

                                }
                            });

                        }else {

                            Toast.makeText(RegisterActivity.this, "Confirm Password and Password Field doesn't match.", Toast.LENGTH_LONG).show();

                        }

                    } else {

                        Password.setError("Atleast 6 Characters");
                        Confirm_Password.setError("Atleast 6 Characters");
                        Toast.makeText(RegisterActivity.this, "Password must contain atleast 6 characters.", Toast.LENGTH_LONG).show();
                    }



                } else {

                    Toast.makeText(RegisterActivity.this, "Enter all the details.", Toast.LENGTH_LONG).show();

                }


            }
        });
        Forgot_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));

            }
        });

    }

    private boolean isEmpty(String name, String email_id, String password, String confirm_pass) {
        if (name.isEmpty() || email_id.isEmpty() || password.isEmpty() || confirm_pass.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Complete All the Details", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}