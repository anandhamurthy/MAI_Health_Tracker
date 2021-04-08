package com.maihealthtracker.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.maihealthtracker.AddRecordActivity;
import com.maihealthtracker.EditDeleteBottomSheet;
import com.maihealthtracker.MainActivity;
import com.maihealthtracker.Models.Record;
import com.maihealthtracker.R;
import com.maihealthtracker.WaterBottomSheet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.CropHolder> {

    private Context mContext;
    private List<Record> mRecord;

    private ProgressDialog mProgressDialog;

    private MainActivity activity;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public RecordAdapter(Context context, MainActivity activity, List<Record> records){
        mContext = context;
        mRecord = records;
        this.activity = activity;
    }

    @Override
    public CropHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_record_layout, parent, false);
        return new CropHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CropHolder holder, final int position) {

        final Record record = mRecord.get(position);

        SimpleDateFormat day_pattern = new SimpleDateFormat("EEEE");
        SimpleDateFormat date_pattern = new SimpleDateFormat("dd-MMM-yyyy");

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DATE, record.getDay());
        calendar.set(Calendar.MONTH, record.getMonth());
        calendar.set(Calendar.YEAR, record.getYear());

        holder.day.setText(day_pattern.format(calendar.getTime()));
        holder.date.setText(date_pattern.format(calendar.getTime()));

        if (record.getWater_count()==0){
            holder.water_title.setVisibility(View.GONE);
            holder.water_count.setVisibility(View.GONE);
            holder.water_box.setVisibility(View.GONE);
            holder.water_div.setVisibility(View.GONE);

        }else{
            holder.water_title.setTextColor(mContext.getResources().getColor(R.color.water));
            holder.water_title.setVisibility(View.VISIBLE);
            holder.water_count.setVisibility(View.VISIBLE);
            holder.water_box.setVisibility(View.VISIBLE);
            holder.water_div.setVisibility(View.VISIBLE);
            if (record.getWater_count()==1 || record.getWater_count() == 0)
                holder.water_count.setText(record.getWater_count()+" Cup");
            else
                holder.water_count.setText(record.getWater_count()+" Cups");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDeleteBottomSheet bottomSheet = new EditDeleteBottomSheet(activity, calendar, record.getUser_id(), record.getRecord_id());
                bottomSheet.show(activity.getSupportFragmentManager(),
                        "ModalBottomSheet");
            }
        });

        holder.water_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WaterBottomSheet bottomSheet = new WaterBottomSheet(activity, calendar, record.getUser_id(), record.getRecord_id());
                bottomSheet.show(activity.getSupportFragmentManager(),
                        "ModalBottomSheet");
                notifyDataSetChanged();
            }
        });

        if (record.getWeight()==0){
            holder.weight_box.setVisibility(View.GONE);
            holder.weight_div.setVisibility(View.GONE);
        }else{
            holder.weight_box.setVisibility(View.VISIBLE);
            holder.weight_div.setVisibility(View.VISIBLE);
            holder.weight.setText(record.getWeight()+" Kg");
        }

        if (record.getNotes().isEmpty()){
            holder.note_box.setVisibility(View.GONE);
            holder.note_div.setVisibility(View.GONE);
        }else{
            holder.note_box.setVisibility(View.VISIBLE);
            holder.note_div.setVisibility(View.VISIBLE);
            holder.note.setText(record.getNotes());
        }

        if (record.getBreakfast()!=null){

            if (record.getBreakfast().getFoods().isEmpty() && record.getBreakfast().getDrinks().isEmpty()
                    && record.getBreakfast().getSymptoms().isEmpty() && record.getBreakfast().getHealth()==0){
                holder.breakfast_title.setVisibility(View.GONE);
                holder.b_div.setVisibility(View.GONE);
            }else{
                holder.breakfast_title.setTextColor(mContext.getResources().getColor(R.color.accent));
                holder.breakfast_title.setVisibility(View.VISIBLE);
                holder.b_div.setVisibility(View.VISIBLE);
            }


            if (!record.getBreakfast().getFoods().isEmpty()){
                holder.b_food_title.setVisibility(View.VISIBLE);
                holder.b_food_list.setVisibility(View.VISIBLE);
                holder.b_food_title.setTextColor(mContext.getResources().getColor(R.color.food));
                holder.b_food_list.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
                holder.b_food_list.setHasFixedSize(false);
                holder.b_food_list.setNestedScrollingEnabled(false);
                IconTextAdapter foodAdapter = new IconTextAdapter(mContext, record.getBreakfast().getFoods());
                holder.b_food_list.setAdapter(foodAdapter);
            }else{
                holder.b_food_title.setVisibility(View.GONE);
                holder.b_food_list.setVisibility(View.GONE);
            }


            if (!record.getBreakfast().getDrinks().isEmpty()){
                holder.b_drink_title.setVisibility(View.VISIBLE);
                holder.b_drink_list.setVisibility(View.VISIBLE);
                holder.b_drink_title.setTextColor(mContext.getResources().getColor(R.color.drink));
                holder.b_drink_list.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
                holder.b_drink_list.setHasFixedSize(false);
                holder.b_drink_list.setNestedScrollingEnabled(false);
                IconTextAdapter drinkAdapter = new IconTextAdapter(mContext, record.getBreakfast().getDrinks());
                holder.b_drink_list.setAdapter(drinkAdapter);
            }else{
                holder.b_drink_title.setVisibility(View.GONE);
                holder.b_drink_list.setVisibility(View.GONE);
            }

            if (!record.getBreakfast().getSymptoms().isEmpty()){
                holder.b_symptom_title.setVisibility(View.VISIBLE);
                holder.b_symptom_list.setVisibility(View.VISIBLE);
                holder.b_symptom_title.setTextColor(mContext.getResources().getColor(R.color.symptom));
                holder.b_symptom_list.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
                holder.b_symptom_list.setHasFixedSize(false);
                holder.b_symptom_list.setNestedScrollingEnabled(false);
                IconTextAdapter symptomAdapter = new IconTextAdapter(mContext, record.getBreakfast().getSymptoms());
                holder.b_symptom_list.setAdapter(symptomAdapter);
            }else{
                holder.b_symptom_title.setVisibility(View.GONE);
                holder.b_symptom_list.setVisibility(View.GONE);
            }

            if (record.getBreakfast().getHealth()!=0){
                holder.b_health_title.setVisibility(View.VISIBLE);
                holder.b_health_status.setVisibility(View.VISIBLE);
                holder.b_health_seek_bar.setVisibility(View.VISIBLE);
                holder.b_health_title.setTextColor(mContext.getResources().getColor(R.color.health));
                getHealthStatus(record.getBreakfast().getHealth(), holder.b_health_status, holder.b_health_seek_bar);
            }else{
                holder.b_health_title.setVisibility(View.GONE);
                holder.b_health_status.setVisibility(View.GONE);
                holder.b_health_seek_bar.setVisibility(View.GONE);
            }

        }else{
            holder.breakfast_title.setVisibility(View.GONE);
            holder.b_food_title.setVisibility(View.GONE);
            holder.b_drink_title.setVisibility(View.GONE);
            holder.b_symptom_title.setVisibility(View.GONE);
            holder.b_food_list.setVisibility(View.GONE);
            holder.b_drink_list.setVisibility(View.GONE);
            holder.b_symptom_list.setVisibility(View.GONE);
            holder.b_div.setVisibility(View.GONE);
            holder.b_health_title.setVisibility(View.GONE);
            holder.b_health_status.setVisibility(View.GONE);
            holder.b_health_seek_bar.setVisibility(View.GONE);
        }

        if (record.getLunch()!=null){

            if (record.getLunch().getFoods().isEmpty() && record.getLunch().getDrinks().isEmpty() &&
                    record.getLunch().getSymptoms().isEmpty() && record.getLunch().getHealth()==0){
                holder.lunch_title.setVisibility(View.GONE);
                holder.l_div.setVisibility(View.GONE);
            }else{
                holder.lunch_title.setTextColor(mContext.getResources().getColor(R.color.accent));
                holder.lunch_title.setVisibility(View.VISIBLE);
                holder.l_div.setVisibility(View.VISIBLE);
            }


            if (!record.getLunch().getFoods().isEmpty()){
                holder.l_food_title.setVisibility(View.VISIBLE);
                holder.l_food_list.setVisibility(View.VISIBLE);
                holder.l_food_title.setTextColor(mContext.getResources().getColor(R.color.food));
                holder.l_food_list.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
                holder.l_food_list.setHasFixedSize(false);
                holder.l_food_list.setNestedScrollingEnabled(false);
                IconTextAdapter foodAdapter = new IconTextAdapter(mContext, record.getLunch().getFoods());
                holder.l_food_list.setAdapter(foodAdapter);
            }else{
                holder.l_food_title.setVisibility(View.GONE);
                holder.l_food_list.setVisibility(View.GONE);
            }


            if (!record.getLunch().getDrinks().isEmpty()){
                holder.l_drink_title.setVisibility(View.VISIBLE);
                holder.l_drink_list.setVisibility(View.VISIBLE);
                holder.l_drink_title.setTextColor(mContext.getResources().getColor(R.color.drink));
                holder.l_drink_list.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
                holder.l_drink_list.setHasFixedSize(false);
                holder.l_drink_list.setNestedScrollingEnabled(false);
                IconTextAdapter drinkAdapter = new IconTextAdapter(mContext, record.getLunch().getDrinks());
                holder.l_drink_list.setAdapter(drinkAdapter);
            }else{
                holder.l_drink_title.setVisibility(View.GONE);
                holder.l_drink_list.setVisibility(View.GONE);
            }

            if (!record.getLunch().getSymptoms().isEmpty()){
                holder.l_symptom_title.setVisibility(View.VISIBLE);
                holder.l_symptom_list.setVisibility(View.VISIBLE);
                holder.l_symptom_title.setTextColor(mContext.getResources().getColor(R.color.symptom));
                holder.l_symptom_list.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
                holder.l_symptom_list.setHasFixedSize(false);
                holder.l_symptom_list.setNestedScrollingEnabled(false);
                IconTextAdapter symptomAdapter = new IconTextAdapter(mContext, record.getLunch().getSymptoms());
                holder.l_symptom_list.setAdapter(symptomAdapter);
            }else{
                holder.l_symptom_title.setVisibility(View.GONE);
                holder.l_symptom_list.setVisibility(View.GONE);
            }


            if (record.getLunch().getHealth()!=0){
                holder.l_health_title.setVisibility(View.VISIBLE);
                holder.l_health_status.setVisibility(View.VISIBLE);
                holder.l_health_seek_bar.setVisibility(View.VISIBLE);
                holder.l_health_title.setTextColor(mContext.getResources().getColor(R.color.health));
                getHealthStatus(record.getLunch().getHealth(), holder.l_health_status, holder.l_health_seek_bar);
            }else{
                holder.l_health_title.setVisibility(View.GONE);
                holder.l_health_status.setVisibility(View.GONE);
                holder.l_health_seek_bar.setVisibility(View.GONE);
            }

        }else{
            holder.lunch_title.setVisibility(View.GONE);
            holder.l_food_title.setVisibility(View.GONE);
            holder.l_drink_title.setVisibility(View.GONE);
            holder.l_symptom_title.setVisibility(View.GONE);
            holder.l_food_list.setVisibility(View.GONE);
            holder.l_drink_list.setVisibility(View.GONE);
            holder.l_symptom_list.setVisibility(View.GONE);
            holder.l_div.setVisibility(View.GONE);
            holder.l_health_title.setVisibility(View.GONE);
            holder.l_health_status.setVisibility(View.GONE);
            holder.l_health_seek_bar.setVisibility(View.GONE);
        }


        if (record.getSnack()!=null){
            if (record.getSnack().getFoods().isEmpty() && record.getSnack().getDrinks().isEmpty() &&
                    record.getSnack().getSymptoms().isEmpty() && record.getSnack().getHealth()==0){
                holder.snack_title.setVisibility(View.GONE);
                holder.s_div.setVisibility(View.GONE);
            }else{
                holder.snack_title.setTextColor(mContext.getResources().getColor(R.color.accent));
                holder.snack_title.setVisibility(View.VISIBLE);
                holder.s_div.setVisibility(View.VISIBLE);
            }


            if (!record.getSnack().getFoods().isEmpty()){
                holder.s_food_title.setVisibility(View.VISIBLE);
                holder.s_food_list.setVisibility(View.VISIBLE);
                holder.s_food_title.setTextColor(mContext.getResources().getColor(R.color.food));
                holder.s_food_list.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
                holder.s_food_list.setHasFixedSize(false);
                holder.s_food_list.setNestedScrollingEnabled(false);
                IconTextAdapter foodAdapter = new IconTextAdapter(mContext, record.getSnack().getFoods());
                holder.s_food_list.setAdapter(foodAdapter);
            }else{
                holder.s_food_title.setVisibility(View.GONE);
                holder.s_food_list.setVisibility(View.GONE);
            }


            if (!record.getSnack().getDrinks().isEmpty()){
                holder.s_drink_title.setVisibility(View.VISIBLE);
                holder.s_drink_list.setVisibility(View.VISIBLE);
                holder.s_drink_title.setTextColor(mContext.getResources().getColor(R.color.drink));
                holder.s_drink_list.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
                holder.s_drink_list.setHasFixedSize(false);
                holder.s_drink_list.setNestedScrollingEnabled(false);
                IconTextAdapter drinkAdapter = new IconTextAdapter(mContext, record.getSnack().getDrinks());
                holder.s_drink_list.setAdapter(drinkAdapter);
            }else{
                holder.s_drink_title.setVisibility(View.GONE);
                holder.s_drink_list.setVisibility(View.GONE);
            }

            if (!record.getSnack().getSymptoms().isEmpty()){
                holder.s_symptom_title.setVisibility(View.VISIBLE);
                holder.s_symptom_list.setVisibility(View.VISIBLE);
                holder.s_symptom_title.setTextColor(mContext.getResources().getColor(R.color.symptom));
                holder.s_symptom_list.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
                holder.s_symptom_list.setHasFixedSize(false);
                holder.s_symptom_list.setNestedScrollingEnabled(false);
                IconTextAdapter symptomAdapter = new IconTextAdapter(mContext, record.getSnack().getSymptoms());
                holder.s_symptom_list.setAdapter(symptomAdapter);
            }else{
                holder.s_symptom_title.setVisibility(View.GONE);
                holder.s_symptom_list.setVisibility(View.GONE);
            }

            if (record.getSnack().getHealth()!=0){
                holder.s_health_title.setVisibility(View.VISIBLE);
                holder.s_health_status.setVisibility(View.VISIBLE);
                holder.s_health_seek_bar.setVisibility(View.VISIBLE);
                holder.s_health_title.setTextColor(mContext.getResources().getColor(R.color.health));
                getHealthStatus(record.getSnack().getHealth(), holder.s_health_status, holder.s_health_seek_bar);
            }else{
                holder.s_health_title.setVisibility(View.GONE);
                holder.s_health_status.setVisibility(View.GONE);
                holder.s_health_seek_bar.setVisibility(View.GONE);
            }

        }else{
            holder.snack_title.setVisibility(View.GONE);
            holder.s_food_title.setVisibility(View.GONE);
            holder.s_drink_title.setVisibility(View.GONE);
            holder.s_symptom_title.setVisibility(View.GONE);
            holder.s_food_list.setVisibility(View.GONE);
            holder.s_drink_list.setVisibility(View.GONE);
            holder.s_symptom_list.setVisibility(View.GONE);
            holder.s_div.setVisibility(View.GONE);
            holder.s_health_title.setVisibility(View.GONE);
            holder.s_health_status.setVisibility(View.GONE);
            holder.s_health_seek_bar.setVisibility(View.GONE);
        }

        if (record.getDinner()!=null){
            if (record.getDinner().getFoods().isEmpty() && record.getDinner().getDrinks().isEmpty()
                    && record.getDinner().getSymptoms().isEmpty() && record.getSnack().getHealth()==0){
                holder.dinner_title.setVisibility(View.GONE);
                holder.d_div.setVisibility(View.GONE);
            }else{
                holder.dinner_title.setTextColor(mContext.getResources().getColor(R.color.accent));
                holder.dinner_title.setVisibility(View.VISIBLE);
                holder.d_div.setVisibility(View.VISIBLE);
            }


            if (!record.getDinner().getFoods().isEmpty()){
                holder.d_food_title.setVisibility(View.VISIBLE);
                holder.d_food_list.setVisibility(View.VISIBLE);
                holder.d_food_title.setTextColor(mContext.getResources().getColor(R.color.food));
                holder.d_food_list.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
                holder.d_food_list.setHasFixedSize(false);
                holder.d_food_list.setNestedScrollingEnabled(false);
                IconTextAdapter foodAdapter = new IconTextAdapter(mContext, record.getDinner().getFoods());
                holder.d_food_list.setAdapter(foodAdapter);
            }else{
                holder.d_food_title.setVisibility(View.GONE);
                holder.d_food_list.setVisibility(View.GONE);
            }


            if (!record.getDinner().getDrinks().isEmpty()){
                holder.d_drink_title.setVisibility(View.VISIBLE);
                holder.d_drink_list.setVisibility(View.VISIBLE);
                holder.d_drink_title.setTextColor(mContext.getResources().getColor(R.color.drink));
                holder.d_drink_list.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
                holder.d_drink_list.setHasFixedSize(false);
                holder.d_drink_list.setNestedScrollingEnabled(false);
                IconTextAdapter drinkAdapter = new IconTextAdapter(mContext, record.getDinner().getDrinks());
                holder.d_drink_list.setAdapter(drinkAdapter);
            }else{
                holder.d_drink_title.setVisibility(View.GONE);
                holder.d_drink_list.setVisibility(View.GONE);
            }

            if (!record.getDinner().getSymptoms().isEmpty()){
                holder.d_symptom_title.setVisibility(View.VISIBLE);
                holder.d_symptom_list.setVisibility(View.VISIBLE);
                holder.d_symptom_title.setTextColor(mContext.getResources().getColor(R.color.symptom));
                holder.d_symptom_list.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
                holder.d_symptom_list.setHasFixedSize(false);
                holder.d_symptom_list.setNestedScrollingEnabled(false);
                IconTextAdapter symptomAdapter = new IconTextAdapter(mContext, record.getDinner().getSymptoms());
                holder.d_symptom_list.setAdapter(symptomAdapter);
            }else{
                holder.d_symptom_title.setVisibility(View.GONE);
                holder.d_symptom_list.setVisibility(View.GONE);
            }

            if (record.getDinner().getHealth()!=0){
                holder.d_health_title.setVisibility(View.VISIBLE);
                holder.d_health_status.setVisibility(View.VISIBLE);
                holder.d_health_seek_bar.setVisibility(View.VISIBLE);
                holder.d_health_title.setTextColor(mContext.getResources().getColor(R.color.health));
                getHealthStatus(record.getDinner().getHealth(), holder.d_health_status, holder.d_health_seek_bar);
            }else{
                holder.d_health_title.setVisibility(View.GONE);
                holder.d_health_status.setVisibility(View.GONE);
                holder.d_health_seek_bar.setVisibility(View.GONE);
            }


        }else{
            holder.dinner_title.setVisibility(View.GONE);
            holder.d_food_title.setVisibility(View.GONE);
            holder.d_drink_title.setVisibility(View.GONE);
            holder.d_symptom_title.setVisibility(View.GONE);
            holder.d_food_list.setVisibility(View.GONE);
            holder.d_drink_list.setVisibility(View.GONE);
            holder.d_symptom_list.setVisibility(View.GONE);
            holder.d_div.setVisibility(View.GONE);
            holder.d_health_title.setVisibility(View.GONE);
            holder.d_health_status.setVisibility(View.GONE);
            holder.d_health_seek_bar.setVisibility(View.GONE);
        }


        mProgressDialog = new ProgressDialog(mContext);

    }

    private void getHealthStatus(int health, TextView health_status, View health_seek_bar) {

        switch (health){
            case 1:
                health_seek_bar.setBackgroundColor(mContext.getResources().getColor(R.color.health_1));
                health_status.setText("Juck");
                health_status.setTextColor(mContext.getResources().getColor(R.color.health_1));
                break;
            case 2:
                health_seek_bar.setBackgroundColor(mContext.getResources().getColor(R.color.health_2));
                health_status.setText("Unhealthy");
                health_status.setTextColor(mContext.getResources().getColor(R.color.health_2));
                break;
            case 3:
                health_seek_bar.setBackgroundColor(mContext.getResources().getColor(R.color.health_3));
                health_status.setText("Ok");
                health_status.setTextColor(mContext.getResources().getColor(R.color.health_3));
                break;
            case 4:
                health_seek_bar.setBackgroundColor(mContext.getResources().getColor(R.color.health_4));
                health_status.setText("Healthy");
                health_status.setTextColor(mContext.getResources().getColor(R.color.health_4));
                break;
            case 5:
                health_seek_bar.setBackgroundColor(mContext.getResources().getColor(R.color.health_5));
                health_status.setText("Super Healthy");
                health_status.setTextColor(mContext.getResources().getColor(R.color.health_5));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mRecord.size();
    }


    public class CropHolder extends RecyclerView.ViewHolder {

        private TextView day, date, water_count, water_title, weight, note_title, note;
        private TextView breakfast_title, b_food_title, b_drink_title, b_symptom_title, b_health_status, b_health_title;
        private RecyclerView b_food_list, b_drink_list, b_symptom_list;

        private TextView lunch_title, l_food_title, l_drink_title, l_symptom_title, l_health_status, l_health_title;
        private RecyclerView l_food_list, l_drink_list, l_symptom_list;

        private TextView snack_title, s_food_title, s_drink_title, s_symptom_title, s_health_status, s_health_title;
        private RecyclerView s_food_list, s_drink_list, s_symptom_list;

        private TextView dinner_title, d_food_title, d_drink_title, d_symptom_title, d_health_status, d_health_title;
        private RecyclerView d_food_list, d_drink_list, d_symptom_list;

        private ImageView water_div, b_div, l_div, s_div, d_div, weight_div, note_div;
        private LinearLayout water_box;
        private RelativeLayout weight_box, note_box;

        private View b_health_seek_bar, l_health_seek_bar, s_health_seek_bar, d_health_seek_bar;

        public CropHolder(View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            water_count = itemView.findViewById(R.id.water_count);
            water_title = itemView.findViewById(R.id.water_text);

            note_title = itemView.findViewById(R.id.note_text);
            note = itemView.findViewById(R.id.note);
            note_box = itemView.findViewById(R.id.note_box);
            note_div = itemView.findViewById(R.id.note_div);

            water_box = itemView.findViewById(R.id.water_box);
            water_div = itemView.findViewById(R.id.water_div);

            weight_box = itemView.findViewById(R.id.weight_box);
            weight = itemView.findViewById(R.id.weight);
            weight_div = itemView.findViewById(R.id.weight_div);

            b_div = itemView.findViewById(R.id.b_div);
            l_div = itemView.findViewById(R.id.l_div);
            s_div = itemView.findViewById(R.id.s_div);
            d_div = itemView.findViewById(R.id.d_div);

            breakfast_title = itemView.findViewById(R.id.breakfast_text);
            b_food_title = itemView.findViewById(R.id.food_breakfast_text);
            b_drink_title = itemView.findViewById(R.id.drink_breakfast_text);
            b_symptom_title = itemView.findViewById(R.id.symptom_breakfast_text);
            b_health_seek_bar = itemView.findViewById(R.id.b_health_bar);
            b_health_status = itemView.findViewById(R.id.b_health_status);
            b_health_title = itemView.findViewById(R.id.b_health_text);

            b_food_list = itemView.findViewById(R.id.food_breakfast_list);
            b_drink_list = itemView.findViewById(R.id.drink_breakfast_list);
            b_symptom_list = itemView.findViewById(R.id.symptom_breakfast_list);

            lunch_title = itemView.findViewById(R.id.lunch_text);
            l_food_title = itemView.findViewById(R.id.food_lunch_text);
            l_drink_title = itemView.findViewById(R.id.drink_lunch_text);
            l_symptom_title = itemView.findViewById(R.id.symptom_lunch_text);
            l_health_seek_bar = itemView.findViewById(R.id.l_health_bar);
            l_health_status = itemView.findViewById(R.id.l_health_status);
            l_health_title = itemView.findViewById(R.id.l_health_text);

            l_food_list = itemView.findViewById(R.id.food_lunch_list);
            l_drink_list = itemView.findViewById(R.id.drink_lunch_list);
            l_symptom_list = itemView.findViewById(R.id.symptom_lunch_list);

            snack_title = itemView.findViewById(R.id.snack_text);
            s_food_title = itemView.findViewById(R.id.food_snack_text);
            s_drink_title = itemView.findViewById(R.id.drink_snack_text);
            s_symptom_title = itemView.findViewById(R.id.symptom_snack_text);
            s_health_seek_bar = itemView.findViewById(R.id.s_health_bar);
            s_health_status = itemView.findViewById(R.id.s_health_status);
            s_health_title = itemView.findViewById(R.id.s_health_text);

            s_food_list = itemView.findViewById(R.id.food_snack_list);
            s_drink_list = itemView.findViewById(R.id.drink_snack_list);
            s_symptom_list = itemView.findViewById(R.id.symptom_snack_list);

            dinner_title = itemView.findViewById(R.id.dinner_text);
            d_food_title = itemView.findViewById(R.id.food_dinner_text);
            d_drink_title = itemView.findViewById(R.id.drink_dinner_text);
            d_symptom_title = itemView.findViewById(R.id.symptom_dinner_text);
            d_health_seek_bar = itemView.findViewById(R.id.d_health_bar);
            d_health_status = itemView.findViewById(R.id.d_health_status);
            d_health_title = itemView.findViewById(R.id.d_health_text);

            d_food_list = itemView.findViewById(R.id.food_dinner_list);
            d_drink_list = itemView.findViewById(R.id.drink_dinner_list);
            d_symptom_list = itemView.findViewById(R.id.symptom_dinner_list);


        }
    }
}