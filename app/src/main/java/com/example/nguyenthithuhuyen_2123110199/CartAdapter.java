package com.example.nguyenthithuhuyen_2123110199;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final Context context;
    private final List<Product> cartList;
    private final OnItemActionListener listener;

    public interface OnItemActionListener {
        void onItemDelete(int position);
        void onQuantityChanged(); // Gọi mỗi khi số lượng thay đổi để cập nhật tổng tiền
    }

    public CartAdapter(List<Product> cartList, Context context, OnItemActionListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.listener = listener;
    }


    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView nameTextView, priceTextView, quantityTextView;
        ImageButton btnDelete, btnMinus, btnPlus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.cartItemImage);
            nameTextView = itemView.findViewById(R.id.cartItemName);
            priceTextView = itemView.findViewById(R.id.cartItemPrice);
            quantityTextView = itemView.findViewById(R.id.tvQuantity);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartList.get(position);

        holder.nameTextView.setText(product.getProductName());

        String formattedPrice = NumberFormat.getNumberInstance(Locale.US).format(product.getPrice()) + " đ";
        holder.priceTextView.setText(formattedPrice);

        holder.quantityTextView.setText(String.valueOf(product.getQuantity()));

        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.productImageView);

        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION && listener != null) {
                listener.onItemDelete(currentPos);
            }
        });

        holder.btnPlus.setOnClickListener(v -> {
            int quantity = product.getQuantity();
            quantity++;
            product.setQuantity(quantity);
            notifyItemChanged(holder.getAdapterPosition());

            if (listener != null) listener.onQuantityChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            int quantity = product.getQuantity();
            if (quantity > 1) {
                quantity--;
                product.setQuantity(quantity);
                notifyItemChanged(holder.getAdapterPosition());

                if (listener != null) listener.onQuantityChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}
