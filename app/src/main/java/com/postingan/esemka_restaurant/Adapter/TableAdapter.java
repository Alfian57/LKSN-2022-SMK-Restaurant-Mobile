package com.postingan.esemka_restaurant.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postingan.esemka_restaurant.Model.Table;
import com.postingan.esemka_restaurant.R;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Table> data;
    OnItemClickListener onItemClickListener;

    public TableAdapter(List<Table> data, OnItemClickListener onItemClickListener) {
        this.data = data;
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_row, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof TableViewHolder){
            TableViewHolder tableViewHolder = (TableViewHolder) holder;

            tableViewHolder.tvTableNumber.setText("Table " + data.get(position).getNumber());
            tableViewHolder.tvTableCode.setText(data.get(position).getCode());
            tableViewHolder.tvTotalPrice.setText("Rp " + data.get(position).getTotal());
            tableViewHolder.llTable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onDetailClick(data.get(position).getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public interface OnItemClickListener {
        void onDetailClick(String id);
    }

    private class TableViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llTable;
        TextView tvTableNumber, tvTableCode, tvTotalPrice;

        public TableViewHolder(View view) {
            super(view);

            llTable = view.findViewById(R.id.llTable);
            tvTableNumber = view.findViewById(R.id.tvTableNumber);
            tvTableCode = view.findViewById(R.id.tvTableCode);
            tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        }
    }
}
