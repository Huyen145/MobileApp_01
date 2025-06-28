package com.example.nguyenthithuhuyen_2123110199;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameView, sellPriceView, rentPriceView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            nameView = itemView.findViewById(R.id.product_name);
            sellPriceView = itemView.findViewById(R.id.product_sell_price);
            rentPriceView = itemView.findViewById(R.id.product_rent_price);
        }
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.imageView.setImageResource(product.getImageResId());
        holder.nameView.setText(product.getName());
        holder.sellPriceView.setText(product.getSellPrice());
        holder.rentPriceView.setText(product.getRentPrice());

        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Product_Detail.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("sellPrice", product.getSellPrice());
            intent.putExtra("rentPrice", product.getRentPrice());
            intent.putExtra("imageResId", product.getImageResId());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }
}
