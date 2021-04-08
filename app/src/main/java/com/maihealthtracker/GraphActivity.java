package com.maihealthtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class GraphActivity extends AppCompatActivity {

    private ImageView back;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        title = findViewById(R.id.title);
        back = findViewById(R.id.back);

        back.setVisibility(View.VISIBLE);
        title.setText("My Stats");


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GraphActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}