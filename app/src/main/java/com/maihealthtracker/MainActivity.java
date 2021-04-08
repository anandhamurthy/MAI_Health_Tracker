package com.maihealthtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.maihealthtracker.Adapter.RecordAdapter;
import com.maihealthtracker.Models.Record;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FloatingActionButton Add;

    private String mCurrentUserId;
    private RelativeLayout No_Layout;

    private RecyclerView Record_List;

    private RecordAdapter recordAdapter;
    List<Record> recordList;
    LinearLayoutManager layoutManager;

    ImageView backWard, forward, calendar_button;
    TextView month_year;

    DatePickerDialog datePickerDialog;

    private Calendar calendar, current_calendar, record_calendar;
    private static final String DATE_FORMAT = "MMMM, yyyy";

    SimpleDateFormat date_title_pattern = new SimpleDateFormat("EEE, dd-MMM-yyyy");
    SimpleDateFormat date_key_pattern = new SimpleDateFormat("dd-MM-yyyy");

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private Toolbar toolbar;

    public static int navItemIndex = 0;

    private static final String TAG_HOME = "home";
    public static String CURRENT_TAG = TAG_HOME;

    private boolean shouldLoadHomeFragOnBackPress = true;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser()!=null){

            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Loading");
            calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            backWard = findViewById(R.id.calendar_prev_button);
            forward = findViewById(R.id.calendar_next_button);
            month_year = findViewById(R.id.month_year);
            month_year.setText(sdf.format(calendar.getTime()));
            calendar_button = findViewById(R.id.calendar_button);
            Add = findViewById(R.id.add_record);
            No_Layout = findViewById(R.id.no_layout);
            Record_List = findViewById(R.id.record_list);
            LottieAnimationView anim = findViewById(R.id.animationView);
            anim.setAnimation(R.raw.empty);

            mCurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);

            navHeader = navigationView.getHeaderView(0);

            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            setUpNavigationView();
            if (savedInstanceState == null) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
            }

            current_calendar = Calendar.getInstance();
            current_calendar.set(Calendar.HOUR_OF_DAY, 0);
            current_calendar.set(Calendar.MINUTE, 0);
            current_calendar.set(Calendar.SECOND, 0);
            current_calendar.set(Calendar.MILLISECOND, 0);

            record_calendar = Calendar.getInstance();
            record_calendar.set(Calendar.HOUR_OF_DAY, 0);
            record_calendar.set(Calendar.MINUTE, 0);
            record_calendar.set(Calendar.SECOND, 0);
            record_calendar.set(Calendar.MILLISECOND, 0);

            if (check(current_calendar,calendar)){
                forward.setVisibility(View.INVISIBLE);
            }else{
                forward.setVisibility(View.VISIBLE);
            }

            forward.setOnClickListener(v -> {
                calendar.add(Calendar.MONTH, 1);
                readRecords();
                month_year.setText(sdf.format(calendar.getTime()));

                if (check(current_calendar,calendar)){
                    forward.setVisibility(View.INVISIBLE);
                    Add.setVisibility(View.VISIBLE);
                }else{
                    forward.setVisibility(View.VISIBLE);
                    Add.setVisibility(View.GONE);
                }
            });

            backWard.setOnClickListener(v -> {
                calendar.add(Calendar.MONTH, -1);
                month_year.setText(sdf.format(calendar.getTime()));
                readRecords();
                if (check(current_calendar,calendar)){
                    forward.setVisibility(View.INVISIBLE);
                    Add.setVisibility(View.VISIBLE);
                }else{
                    forward.setVisibility(View.VISIBLE);
                    Add.setVisibility(View.GONE);
                }

            });

            calendar_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dateDialog();
                }
            });

            datePickerDialog = DatePickerDialog.newInstance(
                    (view, year, monthOfYear, dayOfMonth) -> {
                        record_calendar.set(Calendar.YEAR, year);
                        record_calendar.set(Calendar.MONTH, monthOfYear);
                        record_calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        getAddDialog();

                        SimpleDateFormat jdf1 = new SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault());
                        Log.i("fe", jdf1.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR), // Initial year selection
                    calendar.get(Calendar.MONTH), // Initial month selection
                    calendar.get(Calendar.DAY_OF_MONTH) // Inital day selection
            );
            datePickerDialog.setMaxDate(calendar);



            layoutManager = new LinearLayoutManager(MainActivity.this);
            Record_List.setLayoutManager(layoutManager);
            Record_List.setHasFixedSize(false);
            Record_List.setNestedScrollingEnabled(false);
            recordList = new ArrayList<>();
            recordAdapter = new RecordAdapter(MainActivity.this, MainActivity.this, recordList);
            Record_List.setAdapter(recordAdapter);


            readRecords();

            Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAddDialog();

                }
            });
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {

            sendToLogin();

        }else{
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if (document.getString("b_time").isEmpty()){
                                Intent mainIntent = new Intent(MainActivity.this, ProfileActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                            }
                        }
                    } else {
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                }
            });
        }
    }


    public void readRecords() {

        mProgressDialog.setTitle("Loading");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        db.collection("Records").document(mCurrentUserId).
                collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    recordList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Record record = document.toObject(Record.class);
                        if (calendar.get(Calendar.YEAR)==record.getYear() && calendar.get(Calendar.MONTH)==record.getMonth())
                        recordList.add(record);
                    }
                    recordAdapter.notifyDataSetChanged();
                    if (recordList.isEmpty()){
                        No_Layout.setVisibility(View.VISIBLE);
                        Record_List.setVisibility(View.GONE);
                        mProgressDialog.dismiss();
                    }else{
                        No_Layout.setVisibility(View.GONE);
                        Record_List.setVisibility(View.VISIBLE);
                        mProgressDialog.dismiss();
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    public void refreshRecords() {


        db.collection("Records").document(mCurrentUserId).
                collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    recordList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Record record = document.toObject(Record.class);
                        if (calendar.get(Calendar.YEAR)==record.getYear() && calendar.get(Calendar.MONTH)==record.getMonth())
                            recordList.add(record);
                    }
                    recordAdapter.notifyDataSetChanged();
                    if (recordList.isEmpty()){
                        No_Layout.setVisibility(View.VISIBLE);
                        Record_List.setVisibility(View.GONE);
                        mProgressDialog.dismiss();
                    }else{
                        No_Layout.setVisibility(View.GONE);
                        Record_List.setVisibility(View.VISIBLE);
                        mProgressDialog.dismiss();
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

    }

    public boolean check(Calendar date1, Calendar date2){
        if (date1.compareTo(date2) == 0){
            Log.i("app", "Date1 is equal Date2");
            return true;
        }else if (date1.compareTo(date2) < 0) {
            Log.i("app", "Date1 is before Date2");
            return true;
        } else if (date1.compareTo(date2) > 0) {
            Log.i("app", "Date1 is after Date2");
            return false;
        }else{
            return true;
        }
    }

    private void loadHomeFragment() {
        selectNavMenu();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            return;
        }

        switch (navItemIndex) {
            case 0:
                break;
            default:

        }
        drawer.closeDrawers();
        invalidateOptionsMenu();
    }
    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.predictor:
                        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
                            Intent intent = new Intent(MainActivity.this, PredictorsActivity.class);
                            startActivity(intent);
                        }
                        drawer.closeDrawers();
                        return true;
                    case R.id.graph:
                        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
                            Intent intent1 = new Intent(MainActivity.this, GraphActivity.class);
                            startActivity(intent1);
                        }
                        drawer.closeDrawers();
                        return true;
                    case R.id.remainder:
                        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
                            Intent intent1 = new Intent(MainActivity.this, AlarmActivity.class);
                            startActivity(intent1);
                        }
                        drawer.closeDrawers();
                        return true;
                    case R.id.profile:
                        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
                            Intent intent1 = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent1);
                        }
                        drawer.closeDrawers();
                        return true;

                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        sendToLogin();
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }

    public void dateDialog() {
        datePickerDialog.show(getSupportFragmentManager(), datePickerDialog.getTag());
    }

    private void getAddDialog(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
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
                String date_key = date_key_pattern.format(record_calendar.getTime());
                WeightBottomSheet bottomSheet = new WeightBottomSheet(MainActivity.this, record_calendar, mCurrentUserId, date_key);
                bottomSheet.show(getSupportFragmentManager(),
                        "ModalBottomSheet");
                inner_dialog.dismiss();

            }
        });

        water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date_key = date_key_pattern.format(record_calendar.getTime());
                WaterBottomSheet bottomSheet = new WaterBottomSheet(MainActivity.this, record_calendar, mCurrentUserId, date_key);
                bottomSheet.show(getSupportFragmentManager(),
                        "ModalBottomSheet");
                inner_dialog.dismiss();

            }
        });

        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date_key = date_key_pattern.format(record_calendar.getTime());
                String title = date_title_pattern.format(record_calendar.getTime());
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                intent.putExtra("user_id", mCurrentUserId);
                intent.putExtra("date_key", date_key);
                intent.putExtra("title", title);
                intent.putExtra("id", "breakfast");
                intent.putExtra("calendar", record_calendar);
                startActivity(intent);
                finish();
                inner_dialog.dismiss();
            }
        });

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date_key = date_key_pattern.format(record_calendar.getTime());
                String title = date_title_pattern.format(record_calendar.getTime());
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                intent.putExtra("user_id", mCurrentUserId);
                intent.putExtra("date_key", date_key);
                intent.putExtra("title", title);
                intent.putExtra("id", "lunch");
                intent.putExtra("calendar", record_calendar);
                startActivity(intent);
                finish();
                inner_dialog.dismiss();
            }
        });

        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date_key = date_key_pattern.format(record_calendar.getTime());
                String title = date_title_pattern.format(record_calendar.getTime());
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                intent.putExtra("user_id", mCurrentUserId);
                intent.putExtra("date_key", date_key);
                intent.putExtra("title", title);
                intent.putExtra("id", "snack");
                intent.putExtra("calendar", record_calendar);
                startActivity(intent);
                finish();
                inner_dialog.dismiss();
            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date_key = date_key_pattern.format(record_calendar.getTime());
                String title = date_title_pattern.format(record_calendar.getTime());
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                intent.putExtra("user_id", mCurrentUserId);
                intent.putExtra("date_key", date_key);
                intent.putExtra("title", title);
                intent.putExtra("id", "dinner");
                intent.putExtra("calendar", record_calendar);
                startActivity(intent);
                finish();
                inner_dialog.dismiss();
            }
        });
    }
}