package com.maihealthtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maihealthtracker.Adapter.IconAdapter;
import com.maihealthtracker.Models.Breakfast;
import com.maihealthtracker.Models.Dinner;
import com.maihealthtracker.Models.Icon;
import com.maihealthtracker.Models.Lunch;
import com.maihealthtracker.Models.Record;
import com.maihealthtracker.Models.Snack;
import com.trafi.ratingseekbar.RatingSeekBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddRecordActivity extends AppCompatActivity {

    String date_title, user_id, date_key, id;
    private RecyclerView food_list, drink_list, symptom_list;
    private ImageView plus, minus;
    private TextView title_top, count, add_notes, health_result;

    public ArrayList<Icon> foods, drinks, symptom, selected_food, selected_drink, selected_symptom;

    public List<String> food_selected_item, drink_selected_item, symptom_selected_item;

    IconAdapter foodAdapter, drinkAdapter, symptomAdapter;

    private FloatingActionButton done;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Calendar calendar;

    private int water = 0, health = 0;

    private String notes="";

    private ImageView back;
    private TextView activity_title;

    private RatingSeekBar health_bar;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        mProgressDialog = new ProgressDialog(this);

        Intent intent = getIntent();

        date_title = intent.getStringExtra("title");
        user_id = intent.getStringExtra("user_id");
        date_key = intent.getStringExtra("date_key");
        id = intent.getStringExtra("id");
        calendar = (Calendar) intent.getSerializableExtra("calendar");

        activity_title = findViewById(R.id.title);
        back = findViewById(R.id.back);

        back.setVisibility(View.VISIBLE);
        activity_title.setText(date_title);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddRecordActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        food_list = findViewById(R.id.food_list);
        drink_list = findViewById(R.id.drink_list);
        symptom_list = findViewById(R.id.symptom_list);

        title_top = findViewById(R.id.top_title);
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        count = findViewById(R.id.count);
        add_notes = findViewById(R.id.add_notes);
        health_bar = findViewById(R.id.health_bar);
        health_result = findViewById(R.id.health_result_text);
        done = findViewById(R.id.done);

        foods = new ArrayList<>();
        drinks = new ArrayList<>();
        symptom = new ArrayList<>();

        food_selected_item = new ArrayList<>();
        drink_selected_item = new ArrayList<>();
        symptom_selected_item = new ArrayList<>();

        selected_food = new ArrayList<>();
        selected_drink = new ArrayList<>();
        selected_symptom = new ArrayList<>();

        readFood(null);
        readDrinks(null);
        readSymptom(null);

        title_top.setText(id.toUpperCase());

        mProgressDialog.setTitle("Loading");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();


        DocumentReference bRef = db.collection("Records").document(user_id).
                collection("items").document(date_key);
        bRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document1 = task.getResult();
                    if (document1.exists()) {

                        final Record record = document1.toObject(Record.class);

                        count.setText(record.getWater_count()+" Cups");
                        water=record.getWater_count();

                        notes=record.getNotes();
                        if (record.getNotes().equals("")){
                            add_notes.setText("add notes...");
                        }else {
                            add_notes.setText(record.getNotes());
                        }



                        if (id.equals("breakfast")) {
                            if (record.getBreakfast()!=null) {
                                health=record.getBreakfast().getHealth();
                                health_bar.setProgress(record.getBreakfast().getHealth());
                                getHealthProgress(record.getBreakfast().getHealth());
                                if (!record.getBreakfast().getFoods().isEmpty()) {
                                    Log.i("fe", record.getBreakfast().getFoods().toString());
                                    readFood(record.getBreakfast().getFoods());
                                }

                                if (!record.getBreakfast().getDrinks().isEmpty()) {
                                    readDrinks(record.getBreakfast().getDrinks());
                                }

                                if (!record.getBreakfast().getSymptoms().isEmpty()) {
                                    readSymptom(record.getBreakfast().getSymptoms());
                                }
                            }
                        }else if (id.equals("lunch")){
                            if (record.getLunch()!=null) {
                                health=record.getLunch().getHealth();
                                health_bar.setProgress(record.getLunch().getHealth());
                                getHealthProgress(record.getLunch().getHealth());
                                if (!record.getLunch().getFoods().isEmpty()) {
                                    Log.i("fe", record.getLunch().getFoods().toString());
                                    readFood(record.getLunch().getFoods());
                                }

                                if (!record.getLunch().getDrinks().isEmpty()) {
                                    readDrinks(record.getLunch().getDrinks());
                                }

                                if (!record.getLunch().getSymptoms().isEmpty()) {
                                    readSymptom(record.getLunch().getSymptoms());
                                }
                            }
                        }else if (id.equals("snack")){
                            if (record.getSnack()!=null) {
                                health=record.getSnack().getHealth();
                                health_bar.setProgress(record.getSnack().getHealth());
                                getHealthProgress(record.getSnack().getHealth());
                                if (!record.getSnack().getFoods().isEmpty()) {
                                    Log.i("fe", record.getSnack().getFoods().toString());
                                    readFood(record.getSnack().getFoods());
                                }

                                if (!record.getSnack().getDrinks().isEmpty()) {
                                    readDrinks(record.getSnack().getDrinks());
                                }

                                if (!record.getSnack().getSymptoms().isEmpty()) {
                                    readSymptom(record.getSnack().getSymptoms());
                                }
                            }
                        }else if (id.equals("dinner")){
                            if (record.getDinner()!=null) {
                                health=record.getDinner().getHealth();
                                health_bar.setProgress(record.getDinner().getHealth());
                                getHealthProgress(record.getDinner().getHealth());
                                if (!record.getDinner().getFoods().isEmpty()) {
                                    Log.i("fe", record.getDinner().getFoods().toString());
                                    readFood(record.getDinner().getFoods());
                                }

                                if (!record.getDinner().getDrinks().isEmpty()) {
                                    readDrinks(record.getDinner().getDrinks());
                                }

                                if (!record.getDinner().getSymptoms().isEmpty()) {
                                    readSymptom(record.getDinner().getSymptoms());
                                }
                            }
                        }

                        mProgressDialog.dismiss();

                    }else{
                        mProgressDialog.dismiss();
                    }
                }
            }
        });

        food_list.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
        food_list.setHasFixedSize(false);
        food_list.setNestedScrollingEnabled(false);
        foodAdapter = new IconAdapter(AddRecordActivity.this, foods, selected_food, selected_drink, selected_symptom, food_selected_item, drink_selected_item, symptom_selected_item);
        food_list.setAdapter(foodAdapter);

        drink_list.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
        drink_list.setHasFixedSize(false);
        drink_list.setNestedScrollingEnabled(false);
        drinkAdapter = new IconAdapter(AddRecordActivity.this, drinks, selected_food, selected_drink, selected_symptom, food_selected_item, drink_selected_item, symptom_selected_item);
        drink_list.setAdapter(drinkAdapter);

        symptom_list.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
        symptom_list.setHasFixedSize(false);
        symptom_list.setNestedScrollingEnabled(false);
        symptomAdapter = new IconAdapter(AddRecordActivity.this, symptom, selected_food, selected_drink, selected_symptom, food_selected_item, drink_selected_item, symptom_selected_item);
        symptom_list.setAdapter(symptomAdapter);



        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                water++;
                if (water==1 || water == 0)
                    count.setText(water+" Cup");
                else
                    count.setText(water+" Cups");
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (water!=0){
                    water--;
                    if (water==1 || water == 0)
                        count.setText(water+" Cup");
                    else
                        count.setText(water+" Cups");
                }
            }
        });


        health_bar.setOnSeekBarChangeListener(new RatingSeekBar.OnRatingSeekBarChangeListener() {
            @Override
            public void onProgressChanged(RatingSeekBar ratingSeekBar, int i) {
                getHealthProgress(i);

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgressDialog.setTitle("Save");
                mProgressDialog.setMessage("saving");
                mProgressDialog.show();

                db.collection("Records").document(user_id).
                        collection("items").document(date_key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {



                                final HashMap recordMap = new HashMap<>();
                                recordMap.put("water_count", water);
                                recordMap.put("notes", notes);

                                if (id.equals("breakfast")) {
                                    recordMap.put("breakfast", getBreakfast(foodAdapter.getFoodSelectedDetails(),
                                            drinkAdapter.getDrinkSelectedDetails(), symptomAdapter.getSymptomSelectedDetails()));
                                }else if (id.equals("lunch")) {
                                    recordMap.put("lunch", getLunch(foodAdapter.getFoodSelectedDetails(),
                                            drinkAdapter.getDrinkSelectedDetails(), symptomAdapter.getSymptomSelectedDetails()));
                                }else if (id.equals("snack")) {
                                    recordMap.put("snack", getSnack(foodAdapter.getFoodSelectedDetails(),
                                            drinkAdapter.getDrinkSelectedDetails(), symptomAdapter.getSymptomSelectedDetails()));
                                }else if (id.equals("dinner")) {
                                    recordMap.put("dinner", getDinner(foodAdapter.getFoodSelectedDetails(),
                                            drinkAdapter.getDrinkSelectedDetails(), symptomAdapter.getSymptomSelectedDetails()));
                                }

                                db.collection("Records").document(user_id).
                                        collection("items").document(date_key).update(recordMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        Intent intent = new Intent(AddRecordActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        mProgressDialog.dismiss();
                                    }
                                });

                            } else {


                                SimpleDateFormat month_pattern = new SimpleDateFormat("MMMM");

                                HashMap new_recordMap = new HashMap<>();
                                new_recordMap.put("record_id", date_key);
                                new_recordMap.put("user_id", user_id);
                                new_recordMap.put("water_count", water);
                                new_recordMap.put("weight", 0);
                                new_recordMap.put("notes", notes);
                                new_recordMap.put("day", calendar.get(Calendar.DAY_OF_MONTH));
                                new_recordMap.put("month", calendar.get(Calendar.MONTH));
                                new_recordMap.put("year", calendar.get(Calendar.YEAR));
                                new_recordMap.put("month_name", month_pattern.format(calendar.getTime()));

                                if (id.equals("breakfast")) {
                                    new_recordMap.put("breakfast", getBreakfast(foodAdapter.getFoodSelectedDetails(),
                                            drinkAdapter.getDrinkSelectedDetails(), symptomAdapter.getSymptomSelectedDetails()));
                                }else if (id.equals("lunch")) {
                                    new_recordMap.put("lunch", getLunch(foodAdapter.getFoodSelectedDetails(),
                                            drinkAdapter.getDrinkSelectedDetails(), symptomAdapter.getSymptomSelectedDetails()));
                                }else if (id.equals("snack")) {
                                    new_recordMap.put("snack", getSnack(foodAdapter.getFoodSelectedDetails(),
                                            drinkAdapter.getDrinkSelectedDetails(), symptomAdapter.getSymptomSelectedDetails()));
                                }else if (id.equals("dinner")) {
                                    new_recordMap.put("dinner", getDinner(foodAdapter.getFoodSelectedDetails(),
                                            drinkAdapter.getDrinkSelectedDetails(), symptomAdapter.getSymptomSelectedDetails()));
                                }

                                db.collection("Records").document(user_id).
                                        collection("items").document(date_key).set(new_recordMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(AddRecordActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        mProgressDialog.dismiss();
                                    }
                                });


                            }
                        }
                    }
                });
            }
        });

        add_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddRecordActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.add_notes_dialog_layout, null);
                EditText note = mView.findViewById(R.id.note);
                Button add = mView.findViewById(R.id.add);

                if (add_notes.getText().toString().equals("add notes...")) {
                    note.setHint(add_notes.getText().toString());
                }else{
                    note.setText(add_notes.getText().toString());
                }

                mBuilder.setView(mView);
                final AlertDialog inner_dialog = mBuilder.create();
                inner_dialog.setCanceledOnTouchOutside(true);
                inner_dialog.show();
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notes = note.getText().toString();
                        add_notes.setText(notes);
                        inner_dialog.dismiss();
                    }
                });
            }
        });

    }

    private Breakfast getBreakfast(ArrayList<Icon> foodSelectedDetails, ArrayList<Icon> drinkSelectedDetails, ArrayList<Icon> symptomSelectedDetails) {

        Breakfast breakfast = new Breakfast(foodSelectedDetails, drinkSelectedDetails, symptomSelectedDetails, health);
        return breakfast;
    }

    private Lunch getLunch(ArrayList<Icon> foodSelectedDetails, ArrayList<Icon> drinkSelectedDetails, ArrayList<Icon> symptomSelectedDetails) {

        Lunch lunch = new Lunch(foodSelectedDetails, drinkSelectedDetails, symptomSelectedDetails, health);
        return lunch;
    }

    private Snack getSnack(ArrayList<Icon> foodSelectedDetails, ArrayList<Icon> drinkSelectedDetails, ArrayList<Icon> symptomSelectedDetails) {

        Snack snack = new Snack(foodSelectedDetails, drinkSelectedDetails, symptomSelectedDetails, health);
        return snack;
    }

    private Dinner getDinner(ArrayList<Icon> foodSelectedDetails, ArrayList<Icon> drinkSelectedDetails, ArrayList<Icon> symptomSelectedDetails) {

        Dinner dinner = new Dinner(foodSelectedDetails, drinkSelectedDetails, symptomSelectedDetails, health);
        return dinner;
    }


    private void readDrinks(ArrayList<Icon> f_d) {

        drinks.clear();
        drinks.add(new Icon(R.drawable.ic_d_1, "Water", "drink", false, 0));
        drinks.add(new Icon(R.drawable.ic_d_2, "Tea", "drink", false, 0));
        drinks.add(new Icon(R.drawable.ic_d_3, "Coffee", "drink", false, 0));
        drinks.add(new Icon(R.drawable.ic_d_4, "Soda", "drink", false, 0));
        drinks.add(new Icon(R.drawable.ic_d_5, "Milk", "drink", false, 0));
        drinks.add(new Icon(R.drawable.ic_d_6, "Juice", "drink", false, 0));
        drinks.add(new Icon(R.drawable.ic_d_7, "Energy Drink", "drink", false, 0));
        drinks.add(new Icon(R.drawable.ic_d_8, "Alcohol", "drink", false, 0));

        if (f_d!=null) {
            for (int i = 0; i < drinks.size(); i++) {
                for (int j = 0; j < f_d.size(); j++) {
                    if (drinks.get(i).getIcon_id() == f_d.get(j).getIcon_id()) {
                        drinks.get(i).setSelected(true);
                        drinks.get(i).setCount(f_d.get(j).getCount());
                        drink_selected_item.add(String.valueOf(drinks.get(i).getIcon_id()));
                    }
                }
            }
            drinkAdapter.notifyDataSetChanged();
        }

    }

    private void readFood(ArrayList<Icon> f_f) {
        foods.clear();
        foods.add(new Icon(R.drawable.ic_f_1, "Bakery", "food", false, 0));
        foods.add(new Icon(R.drawable.ic_f_2, "Oats", "food", false, 0));
        foods.add(new Icon(R.drawable.ic_f_3, "Eggs", "food", false, 0));
        foods.add(new Icon(R.drawable.ic_f_4, "Fruits", "food", false, 0));
        foods.add(new Icon(R.drawable.ic_f_5, "Vegetables", "food", false, 0));
        foods.add(new Icon(R.drawable.ic_f_6, "Rice", "food", false, 0));
        foods.add(new Icon(R.drawable.ic_f_13, "Wheat", "food", false, 0));
        foods.add(new Icon(R.drawable.ic_f_7, "Salad", "food", false, 0));
        foods.add(new Icon(R.drawable.ic_f_8, "Sweet", "food", false, 0));
        foods.add(new Icon(R.drawable.ic_f_9, "Soup", "food", false, 0));
        foods.add(new Icon(R.drawable.ic_f_10, "Pizza", "food", false, 0));
        foods.add(new Icon(R.drawable.ic_f_11, "Burger", "food", false, 0));
        foods.add(new Icon(R.drawable.ic_f_12, "Noodles", "food", false, 0));

        if (f_f!=null) {
            for (int i = 0; i < foods.size(); i++) {
                for (int j = 0; j < f_f.size(); j++) {
                    if (foods.get(i).getIcon_id() == f_f.get(j).getIcon_id()) {
                        foods.get(i).setSelected(true);
                        foods.get(i).setCount(f_f.get(j).getCount());
                        food_selected_item.add(String.valueOf(foods.get(i).getIcon_id()));
                    }
                }
            }
            foodAdapter.notifyDataSetChanged();
            Log.i("fe", foods.toString());
        }
    }

    private void readSymptom(ArrayList<Icon> f_s) {
        symptom.clear();
        symptom.add(new Icon(R.drawable.ic_s_1, "Reflux", "symptom", false, 0));
        symptom.add(new Icon(R.drawable.ic_s_2, "Gas", "symptom", false, 0));
        symptom.add(new Icon(R.drawable.ic_s_3, "Rash", "symptom", false, 0));
        symptom.add(new Icon(R.drawable.ic_s_5, "Vomiting", "symptom", false, 0));
        symptom.add(new Icon(R.drawable.ic_s_6, "Diarrhea", "symptom", false, 0));
        symptom.add(new Icon(R.drawable.ic_s_7, "Belly Pain", "symptom", false, 0));
        symptom.add(new Icon(R.drawable.ic_s_8, "Headache", "symptom", false, 0));
        symptom.add(new Icon(R.drawable.ic_s_9, "Fatigue", "symptom", false, 0));
        symptom.add(new Icon(R.drawable.ic_s_4, "Constipation", "symptom", false, 0));

        if (f_s!=null) {
            for (int i = 0; i < symptom.size(); i++) {
                for (int j = 0; j < f_s.size(); j++) {
                    if (symptom.get(i).getIcon_id() == f_s.get(j).getIcon_id()) {
                        symptom.get(i).setSelected(true);
                        symptom.get(i).setCount(f_s.get(j).getCount());
                        symptom_selected_item.add(String.valueOf(symptom.get(i).getIcon_id()));
                    }
                }
            }
            symptomAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddRecordActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getHealthProgress(int value) {
        switch (value){
            case 0:
                health=0;
                health_result.setText("Select Point");
                break;
            case 1:
                health=1;
                health_result.setText("Juck");
                break;
            case 2:
                health=2;
                health_result.setText("Unhealthy");
                break;
            case 3:
                health=3;
                health_result.setText("Ok");
                break;
            case 4:
                health=4;
                health_result.setText("Healthy");
                break;
            case 5:
                health=5;
                health_result.setText("Super Healthy");
                break;
        }
    }
}