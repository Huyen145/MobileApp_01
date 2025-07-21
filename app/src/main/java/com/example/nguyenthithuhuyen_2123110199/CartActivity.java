package com.example.nguyenthithuhuyen_2123110199;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private TextView tvTotalPrice;
    private Button checkoutButton;

    private ArrayList<Product> cartList;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_cart);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(CartActivity.this, HomeActivity1.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_cart) {
                return true;
            } else if (id == R.id.nav_account) {
                startActivity(new Intent(CartActivity.this, AccountActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        checkoutButton = findViewById(R.id.checkoutButton);

        cartList = new ArrayList<>();
        loadCartFromSharedPreferences();

        cartAdapter = new CartAdapter(cartList, this, new CartAdapter.OnItemActionListener() {
            @Override
            public void onItemDelete(int position) {
                cartList.remove(position);
                cartAdapter.notifyItemRemoved(position);
                cartAdapter.notifyItemRangeChanged(position, cartList.size());
                updateTotalPrice();
                saveCartToSharedPreferences();
            }

            @Override
            public void onQuantityChanged() {
                updateTotalPrice();
                saveCartToSharedPreferences();
            }
        });



        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter);

        updateTotalPrice();

        checkoutButton.setOnClickListener(v -> {
            saveCartToSharedPreferences();
            Intent intent = new Intent(CartActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }

    private void updateTotalPrice() {
        int subtotal = 0;
        int totalDiscount = 0;
        int shippingFee = 21000;

        for (Product product : cartList) {
            int quantity = product.getQuantity();
            int price = product.getPrice();
            int discount = product.getDiscount();

            subtotal += price * quantity;
            totalDiscount += discount * quantity;
        }

        int finalTotal = subtotal - totalDiscount + shippingFee;

        TextView tvSubtotal = findViewById(R.id.tvSubtotal);
        TextView tvDiscount = findViewById(R.id.tvDiscount);
        TextView tvShippingFee = findViewById(R.id.tvShippingFee);

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        tvSubtotal.setText("Thành tiền: " + formatter.format(subtotal) + "₫");
        tvDiscount.setText("- Giảm giá: " + formatter.format(totalDiscount) + "₫");
        tvShippingFee.setText("+ Phí giao hàng: " + formatter.format(shippingFee) + "₫");
        tvTotalPrice.setText("Tổng cộng: " + formatter.format(finalTotal) + "₫");
    }

    private void loadCartFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("cart", MODE_PRIVATE);
        String cartJson = prefs.getString("cart_items", "[]");

        cartList.clear(); // XÓA DỮ LIỆU CŨ TRÁNH TRÙNG LẶP
        android.util.Log.d("CartData", "JSON từ SharedPreferences: " + cartJson);

        try {
            JSONArray cartArray = new JSONArray(cartJson);
            for (int i = 0; i < cartArray.length(); i++) {
                JSONObject obj = cartArray.getJSONObject(i);
                Product product = new Product(
                        obj.getString("productName"),
                        obj.getInt("price"),
                        obj.getInt("discount"),
                        obj.getString("image"),
                        obj.getString("category"),
                        obj.getString("description"),
                        obj.getInt("quantity"),
                        obj.getInt("view")
                );
                cartList.add(product);

                android.util.Log.d("CartData", "Sản phẩm: " + product.getProductName() + " - SL: " + product.getQuantity());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveCartToSharedPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences("cart", MODE_PRIVATE).edit();
        JSONArray jsonArray = new JSONArray();
        for (Product product : cartList) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("productName", product.getProductName());
                obj.put("price", product.getPrice());
                obj.put("discount", product.getDiscount());
                obj.put("image", product.getImage());
                obj.put("category", product.getCategory());
                obj.put("description", product.getDescription());
                obj.put("quantity", product.getQuantity());
                obj.put("view", product.getView());
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString("cart_items", jsonArray.toString());
        editor.apply();
    }
}
