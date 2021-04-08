package com.maihealthtracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.maihealthtracker.Models.Icon;
import com.maihealthtracker.R;

import java.util.ArrayList;
import java.util.List;

public class IconTextAdapter extends RecyclerView.Adapter<IconTextAdapter.CropHolder> {

    private Context mContext;
    private List<Icon> mIcon;

    public IconTextAdapter(Context context, List<Icon> icons){
        mContext = context;
        mIcon = icons;
    }


    @Override
    public CropHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_icon_text_layout, parent, false);
        return new CropHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CropHolder holder, final int position) {

        final Icon icon = mIcon.get(position);

        holder.name.setText(icon.getName());
//        holder.count.setText(icon.getCount()+"");
        holder.image.setImageResource(icon.getIcon_id());

        if (icon.getTag().equals("food")){
            holder.image.setImageTintList(mContext.getResources().getColorStateList(R.color.food));
        }else if (icon.getTag().equals("drink")){
            holder.image.setImageTintList(mContext.getResources().getColorStateList(R.color.drink));
        }else if (icon.getTag().equals("symptom")){
            holder.image.setImageTintList(mContext.getResources().getColorStateList(R.color.symptom));
        }

    }

    @Override
    public int getItemCount() {
        return mIcon.size();
    }


    public class CropHolder extends RecyclerView.ViewHolder {

        private TextView name, count;
        private ImageView image;

        public CropHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            //count = itemView.findViewById(R.id.count);
        }
    }
}