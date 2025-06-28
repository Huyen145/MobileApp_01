package com.example.nguyenthithuhuyen_2123110199;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Product_Detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ Kiểm tra đăng nhập
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem chi tiết sản phẩm!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // ✅ Nếu đã đăng nhập thì hiển thị giao diện
        setContentView(R.layout.activity_product_detail);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String sellPrice = intent.getStringExtra("sellPrice");
        String rentPrice = intent.getStringExtra("rentPrice");
        int imageResId = intent.getIntExtra("imageResId", 0);

        // Gán dữ liệu vào các view
        ImageView productImage = findViewById(R.id.product_image);
        TextView productName = findViewById(R.id.product_name);
        TextView productSellPrice = findViewById(R.id.product_sell_price);
        TextView productRentPrice = findViewById(R.id.product_rent_price);

        if (imageResId != 0) productImage.setImageResource(imageResId);
        if (name != null) productName.setText(name);
        if (sellPrice != null) productSellPrice.setText(sellPrice);
        if (rentPrice != null) productRentPrice.setText(rentPrice);

        // Nút quay lại
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Danh sách sản phẩm liên quan
        RecyclerView relatedProductsRecycler = findViewById(R.id.rv_related_products);
        relatedProductsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Product> relatedList = new ArrayList<>();
        relatedList.add(new Product("Ốp lưng iPhone", "₫350,000", "₫300,000", R.drawable.op));
        relatedList.add(new Product("Tai nghe Bluetooth", "₫2,500,000", "₫150,000", R.drawable.tainghe));
        // ... bạn có thể thêm nhiều sản phẩm tùy ý

        ProductAdapter adapter = new ProductAdapter(this, relatedList);
        relatedProductsRecycler.setAdapter(adapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_cart);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(this, HomeActivity1.class));
                } else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(this, CartActivity.class));
                } else if (itemId == R.id.nav_account) {
                    startActivity(new Intent(this, AccountActivity.class));
                } else {
                    return false;
                }

                overridePendingTransition(0, 0);
                return true;
            });
        }
    }
}
