package com.maihealthtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.maihealthtracker.Adapter.PredictorAdapter;
import com.maihealthtracker.Adapter.RecordAdapter;
import com.maihealthtracker.Models.Predictor;
import com.maihealthtracker.Models.Record;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PredictorsActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String mCurrentUserId;

    private RecyclerView Predictor_List;

    private PredictorAdapter predictorAdapter;
    List<Predictor> predictorList;
    LinearLayoutManager layoutManager;

    private ProgressDialog mProgressDialog;

    private ImageView back;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictors);


        mProgressDialog = new ProgressDialog(this);

        title = findViewById(R.id.title);
        back = findViewById(R.id.back);

        back.setVisibility(View.VISIBLE);
        title.setText("Predictors");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PredictorsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Predictor_List = findViewById(R.id.predictor_list);

        Predictor_List.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        Predictor_List.setHasFixedSize(false);
        predictorList = new ArrayList<>();
        predictorAdapter = new PredictorAdapter(PredictorsActivity.this, predictorList);
        Predictor_List.setAdapter(predictorAdapter);

        readPredictors();
    }


    public void readPredictors() {

        mProgressDialog.setTitle("Loading");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        db.collection("Predictors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    predictorList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Predictor predictor = document.toObject(Predictor.class);
                        predictorList.add(predictor);
                    }
                    predictorAdapter.notifyDataSetChanged();
                    mProgressDialog.dismiss();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}