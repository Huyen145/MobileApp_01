package com.example.nguyenthithuhuyen_2123110199;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, addToCartIcon;
        TextView productName, productSellPrice, productRentPrice;

        public ViewHolder(View view) {
            super(view);
            productImage = view.findViewById(R.id.product_image);
            addToCartIcon = view.findViewById(R.id.add_to_cart_icon);
            productName = view.findViewById(R.id.product_name);
            productSellPrice = view.findViewById(R.id.product_sell_price);
            productRentPrice = view.findViewById(R.id.product_rent_price);
        }
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(product.getPrice()) + "₫";
        String formattedDiscount = formatter.format(product.getDiscount()) + "₫";

        holder.productName.setText(product.getProductName());
        holder.productSellPrice.setText("Giá bán: " + formattedPrice);
        holder.productRentPrice.setText("Giảm giá: " + formattedDiscount);

        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.back)
                .into(holder.productImage);

        holder.addToCartIcon.setOnClickListener(v -> {
            addToCart(productList.get(holder.getAdapterPosition()));
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Product_Detail.class);
            intent.putExtra("name", product.getProductName());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("discount", product.getDiscount());
            intent.putExtra("image", product.getImage());
            intent.putExtra("category", product.getCategory());
            intent.putExtra("description", product.getDescription());
            intent.putExtra("quantity", product.getQuantity());
            intent.putExtra("view", product.getView());
            context.startActivity(intent);
        });
    }

    private void addToCart(Product productToAdd) {
        SharedPreferences prefs = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        String cartJson = prefs.getString("cart_items", "[]");

        List<Product> currentCart = new ArrayList<>();

        try {
            JSONArray cartArray = new JSONArray(cartJson);
            for (int i = 0; i < cartArray.length(); i++) {
                JSONObject obj = cartArray.getJSONObject(i);
                Product p = Product.fromJson(obj);
                currentCart.add(p);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean found = false;
        for (Product p : currentCart) {
            if (p.getProductName().equals(productToAdd.getProductName())) {
                // Nếu sản phẩm đã có thì cộng thêm số lượng
                p.setQuantity(p.getQuantity() + 1);
                found = true;
                break;
            }
        }

        if (!found) {
            productToAdd.setQuantity(1); // thêm mới
            currentCart.add(productToAdd);
        }

        // Lưu lại SharedPreferences
        JSONArray newCartArray = new JSONArray();
        for (Product p : currentCart) {
            try {
                newCartArray.put(p.toJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cart_items", newCartArray.toString());
        editor.apply();

        android.util.Log.d("CartData", "Đã thêm giỏ hàng: " + newCartArray.toString());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
