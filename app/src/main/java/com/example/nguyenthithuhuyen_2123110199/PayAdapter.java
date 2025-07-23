package com.example.nguyenthithuhuyen_2123110199;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayViewHolder> {
    private ArrayList<Product> productList;
    private Context context;

    public PayAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public PayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pay, parent, false);
        return new PayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PayViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvProductName.setText(product.getProductName());
        holder.tvProductQuantity.setText("Số lượng: " + product.getQuantity());

        // Load hình ảnh (nếu có dùng Glide)
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            Glide.with(context).load(product.getImage()).into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.samsung); // ảnh mặc định
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class PayViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductQuantity;

        public PayViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
        }
    }
}

