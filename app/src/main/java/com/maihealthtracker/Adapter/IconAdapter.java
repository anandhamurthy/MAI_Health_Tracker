package com.maihealthtracker.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.maihealthtracker.AddRecordActivity;
import com.maihealthtracker.Models.Icon;
import com.maihealthtracker.R;

import java.util.ArrayList;
import java.util.List;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.CropHolder> {

    private Context mContext;
    private List<Icon> mIcon;
    private List<String> food_selected_item, drink_selected_item, symptom_selected_item;
    private ArrayList<Icon> selected_food, selected_drink, selected_symptom;

    public IconAdapter(Context context, List<Icon> icons, ArrayList<Icon> selected_food, ArrayList<Icon> selected_drink, ArrayList<Icon> selected_symptom, List<String> food_selected_details, List<String> drink_selected_details, List<String> symptom_selected_details){
        mContext = context;
        mIcon = icons;
        this.food_selected_item = food_selected_details;
        this.drink_selected_item = drink_selected_details;
        this.symptom_selected_item = symptom_selected_details;
        this.selected_food = selected_food;
        this.selected_drink = selected_drink;
        this.selected_symptom = selected_symptom;
    }


    @Override
    public CropHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_icon_layout, parent, false);
        return new CropHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CropHolder holder, final int position) {

        final Icon icon = mIcon.get(position);

        holder.name.setText(icon.getName());
        holder.image.setImageResource(icon.getIcon_id());

        if (icon.isSelected()){
            holder.count.setVisibility(View.VISIBLE);
            holder.remove.setVisibility(View.VISIBLE);

            if (icon.getTag().equals("food")){
                holder.image.setImageTintList(mContext.getResources().getColorStateList(R.color.white));
                holder.image.setBackgroundColor(ContextCompat.getColor(mContext, R.color.food));
                holder.image_layout.setBackgroundResource(R.drawable.food_bg);
                holder.count.setText(icon.getCount()+"");
            }else if (icon.getTag().equals("drink")){
                holder.image.setImageTintList(mContext.getResources().getColorStateList(R.color.white));
                holder.image.setBackgroundColor(ContextCompat.getColor(mContext, R.color.drink));
                holder.image_layout.setBackgroundResource(R.drawable.drink_bg);
                holder.count.setText(icon.getCount()+"");
            }else if (icon.getTag().equals("symptom")){
                holder.image.setImageTintList(mContext.getResources().getColorStateList(R.color.white));
                holder.image.setBackgroundColor(ContextCompat.getColor(mContext, R.color.symptom));
                holder.image_layout.setBackgroundResource(R.drawable.symptom_bg);
                holder.count.setText(icon.getCount()+"");
            }
        }else{



            if (icon.getTag().equals("food")){
                holder.remove.setVisibility(View.GONE);
                holder.count.setVisibility(View.GONE);
                holder.image.setImageTintList(mContext.getResources().getColorStateList(R.color.food));
                holder.image.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                holder.image_layout.setBackgroundResource(R.drawable.white_bg);
            }else if (icon.getTag().equals("drink")){
                holder.remove.setVisibility(View.GONE);
                holder.count.setVisibility(View.GONE);
                holder.image.setImageTintList(mContext.getResources().getColorStateList(R.color.drink));
                holder.image.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                holder.image_layout.setBackgroundResource(R.drawable.white_bg);
            }else if (icon.getTag().equals("symptom")){
                holder.remove.setVisibility(View.GONE);
                holder.count.setVisibility(View.GONE);
                holder.image.setImageTintList(mContext.getResources().getColorStateList(R.color.symptom));
                holder.image.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                holder.image_layout.setBackgroundResource(R.drawable.white_bg);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (icon.getTag().equals("food")){

                    if (!icon.isSelected()){
                        icon.setSelected(true);
                        icon.setCount(1);
                        holder.count.setVisibility(View.VISIBLE);
                        holder.remove.setVisibility(View.VISIBLE);
                        notifyDataSetChanged();

                        if (!food_selected_item.contains(String.valueOf(icon.getIcon_id()))){
                            food_selected_item.add(String.valueOf(icon.getIcon_id()));
                        }

                        holder.image.setImageTintList(mContext.getResources().getColorStateList(R.color.white));
                        holder.image.setBackgroundColor(ContextCompat.getColor(mContext, R.color.food));
                        holder.image_layout.setBackgroundResource(R.drawable.food_bg);
                    }else{
                        icon.setCount(icon.getCount()+1);
                        holder.count.setText(icon.getCount()+"");
                    }
                }else if (icon.getTag().equals("drink")){
                    if (!icon.isSelected()){
                        icon.setSelected(true);
                        icon.setCount(1);
                        holder.count.setVisibility(View.VISIBLE);
                        holder.remove.setVisibility(View.VISIBLE);
                        notifyDataSetChanged();

                        if (!drink_selected_item.contains(String.valueOf(icon.getIcon_id()))){
                            drink_selected_item.add(String.valueOf(icon.getIcon_id()));
                        }

                        holder.image.setImageTintList(mContext.getResources().getColorStateList(R.color.white));
                        holder.image.setBackgroundColor(ContextCompat.getColor(mContext, R.color.drink));
                        holder.image_layout.setBackgroundResource(R.drawable.drink_bg);
                    }else{
                        icon.setCount(icon.getCount()+1);
                        holder.count.setText(icon.getCount()+"");
                    }
                }else{
                    if (!icon.isSelected()){
                        icon.setSelected(true);
                        icon.setCount(1);
                        holder.count.setVisibility(View.VISIBLE);
                        holder.remove.setVisibility(View.VISIBLE);
                        notifyDataSetChanged();

                        if (!symptom_selected_item.contains(String.valueOf(icon.getIcon_id()))){
                            symptom_selected_item.add(String.valueOf(icon.getIcon_id()));
                        }

                        holder.image.setImageTintList(mContext.getResources().getColorStateList(R.color.white));
                        holder.image.setBackgroundColor(ContextCompat.getColor(mContext, R.color.symptom));
                        holder.image_layout.setBackgroundResource(R.drawable.symptom_bg);
                    }else{
                        icon.setCount(icon.getCount()+1);
                        holder.count.setText(icon.getCount()+"");
                    }
                }


            }
        });


        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (icon.getTag().equals("food")){
                    if (icon.isSelected()){
                        if (food_selected_item.contains(String.valueOf(icon.getIcon_id()))){
                            food_selected_item.remove(String.valueOf(icon.getIcon_id()));
                        }
                        holder.remove.setVisibility(View.GONE);
                        icon.setCount(0);
                        holder.count.setVisibility(View.GONE);
                        icon.setSelected(false);
                        holder.image.setImageTintList(mContext.getResources().getColorStateList(R.color.food));
                        holder.image.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                        holder.image_layout.setBackgroundResource(R.drawable.white_bg);
                    }
                }else if (icon.getTag().equals("drink")){
                    if (icon.isSelected()){
                        if (drink_selected_item.contains(String.valueOf(icon.getIcon_id()))){
                            drink_selected_item.remove(String.valueOf(icon.getIcon_id()));
                        }
                        holder.remove.setVisibility(View.GONE);
                        icon.setCount(0);
                        holder.count.setVisibility(View.GONE);
                        icon.setSelected(false);
                        holder.image.setImageTintList(mContext.getResources().getColorStateList(R.color.drink));
                        holder.image.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                        holder.image_layout.setBackgroundResource(R.drawable.white_bg);
                    }
                }else {
                    if (icon.isSelected()){
                        if (symptom_selected_item.contains(String.valueOf(icon.getIcon_id()))){
                            symptom_selected_item.remove(String.valueOf(icon.getIcon_id()));
                        }
                        holder.remove.setVisibility(View.GONE);
                        icon.setCount(0);
                        holder.count.setVisibility(View.GONE);
                        icon.setSelected(false);
                        holder.image.setImageTintList(mContext.getResources().getColorStateList(R.color.symptom));
                        holder.image.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                        holder.image_layout.setBackgroundResource(R.drawable.white_bg);
                    }
                }

            }
        });

    }

    public ArrayList<Icon> getFoodSelectedDetails() {

        ArrayList<Icon> arrayList = new ArrayList<>();

        for (Icon icon : mIcon){
            if (food_selected_item.contains(String.valueOf(icon.getIcon_id())))
                arrayList.add(icon);
        }
        return arrayList;
    }

    public ArrayList<Icon> getDrinkSelectedDetails() {
        ArrayList<Icon> arrayList = new ArrayList<>();
        for (Icon icon : mIcon){
            if (drink_selected_item.contains(String.valueOf(icon.getIcon_id())))
                arrayList.add(icon);
        }
        return arrayList;
    }

    public ArrayList<Icon> getSymptomSelectedDetails() {
        ArrayList<Icon> arrayList = new ArrayList<>();
        for (Icon icon : mIcon){
            if (symptom_selected_item.contains(String.valueOf(icon.getIcon_id())))
                arrayList.add(icon);
        }
        return arrayList;
    }

    @Override
    public int getItemCount() {
        return mIcon.size();
    }


    public class CropHolder extends RecyclerView.ViewHolder {

        private TextView name, count;
        private ImageView image, remove;

        private RelativeLayout image_layout;

        public CropHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            count = itemView.findViewById(R.id.count);
            remove = itemView.findViewById(R.id.remove);
            image_layout = itemView.findViewById(R.id.image_layout);
        }
    }
}