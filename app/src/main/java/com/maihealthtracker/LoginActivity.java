package com.maihealthtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class LoginActivity extends AppCompatActivity {

    private EditText Email_Id, Password;
    private TextView Forgot_Password;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ProgressDialog mProgressDialog;
    private FloatingActionButton Login;
    private TextView Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

        Login = findViewById(R.id.login);
        Register = findViewById(R.id.register);
        Email_Id = findViewById(R.id.email_id);
        Password = findViewById(R.id.password);
        Forgot_Password = findViewById(R.id.forgot_password);

        mProgressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email_id = Email_Id.getText().toString().trim();
                final String password = Password.getText().toString().trim();

                if (isEmpty(email_id, password)) {

                    mProgressDialog.setTitle("Logging In");
                    mProgressDialog.setMessage("Authenticating User..");
                    mProgressDialog.show();
                    mProgressDialog.setCanceledOnTouchOutside(false);

                    mAuth.signInWithEmailAndPassword(email_id, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                mProgressDialog.setMessage("Setting User..");

                                final String mCurrent_User_Id = mAuth.getCurrentUser().getUid();

                                final HashMap userMap = new HashMap<>();
                                String devicetoken = FirebaseInstanceId.getInstance().getToken();
                                userMap.put("device_token", devicetoken);

                                db.collection("Users").document(mCurrent_User_Id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                if (document.getString("b_time").isEmpty()){
                                                    mProgressDialog.dismiss();
                                                    Intent mainIntent = new Intent(LoginActivity.this, ProfileActivity.class);
                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(mainIntent);
                                                    finish();
                                                }else{
                                                    mProgressDialog.dismiss();
                                                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(mainIntent);
                                                    finish();
                                                }
                                            } else {
                                                db.collection("Users").document(mCurrent_User_Id).update(userMap)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                mProgressDialog.dismiss();
                                                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(mainIntent);
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(LoginActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                                                Log.d(TAG, e.toString());
                                                            }
                                                        });
                                            }
                                        } else {
                                            Log.d(TAG, "Failed with: ", task.getException());
                                        }
                                    }
                                });




                            } else {

                                mProgressDialog.hide();
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();

                            }


                        }
                    });

                } else {

                    Toast.makeText(LoginActivity.this, "Enter all the details.", Toast.LENGTH_LONG).show();

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

    private boolean isEmpty(String email_id, String password) {
        if (email_id.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Complete All the Details", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();

        }
    }

}