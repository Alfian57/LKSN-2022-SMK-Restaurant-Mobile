package com.postingan.esemka_restaurant.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postingan.esemka_restaurant.Model.Order;
import com.postingan.esemka_restaurant.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Order> data;

    public OrderAdapter(List<Order> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OrderViewHolder){
            OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
            orderViewHolder.tvStatus.setText(data.get(position).getStatus());
            orderViewHolder.tvOrder.setText("Order " + (position + 1) + " - " + data.get(position).getCreatedAt());

//            StringBuilder stringItem = new StringBuilder();
//            for (int i = 0; i < data.get(position).getOrderDetails().size(); i++) {
//                int qty = data.get(position).getOrderDetails().get(i).getQuantity();
//                String name = data.get(position).getOrderDetails().get(i).getMenus().get(0).getName();
//                int price = data.get(position).getOrderDetails().get(i).getMenus().get(0).getPrice();
//                stringItem.append(qty + " " + name + "Rp. " + price);
//            }
//            orderViewHolder.tvItem.setText(stringItem.toString());
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrder, tvStatus, tvItem;

        public OrderViewHolder(View view) {
            super(view);

            tvOrder = view.findViewById(R.id.tvOrder);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvItem = view.findViewById(R.id.tvItem);
        }
    }
}
