package com.maihealthtracker.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maihealthtracker.EditDeleteBottomSheet;
import com.maihealthtracker.MainActivity;
import com.maihealthtracker.Models.Predictor;
import com.maihealthtracker.Models.Record;
import com.maihealthtracker.R;
import com.maihealthtracker.WaterBottomSheet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class PredictorAdapter extends RecyclerView.Adapter<PredictorAdapter.PredictorHolder> {

    private Context mContext;
    private List<Predictor> mPredictor;

    private ProgressDialog mProgressDialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public PredictorAdapter(Context context, List<Predictor> predictors){
        mContext = context;
        mPredictor = predictors;
    }

    @Override
    public PredictorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_predictor_layout, parent, false);
        return new PredictorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PredictorHolder holder, final int position) {

        final Predictor predictor = mPredictor.get(position);

        holder.name.setText(predictor.getName());
        Glide.with(mContext).load(predictor.getImage()).into(holder.image);
        

    }

    @Override
    public int getItemCount() {
        return mPredictor.size();
    }


    public class PredictorHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView image;

        public PredictorHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);


        }
    }
}