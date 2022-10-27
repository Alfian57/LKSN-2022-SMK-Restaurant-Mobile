package com.postingan.esemka_restaurant.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postingan.esemka_restaurant.Model.Food;
import com.postingan.esemka_restaurant.R;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Food> data;

    public FoodAdapter(List<Food> data){
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_row, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FoodViewHolder){
            FoodViewHolder foodViewHolder = (FoodViewHolder) holder;

            foodViewHolder.tvName.setText(data.get(position).getName());
            foodViewHolder.tvPrice.setText(data.get(position).getPrice());
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    private class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;

        public FoodViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvPrice = view.findViewById(R.id.tvPrice);
        }
    }
}
