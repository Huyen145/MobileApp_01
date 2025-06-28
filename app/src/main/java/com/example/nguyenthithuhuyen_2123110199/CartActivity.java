package com.example.nguyenthithuhuyen_2123110199;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem giỏ hàng!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_cart);

        Button checkoutButton = findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(v -> {
            Toast.makeText(this, "Đang chuyển đến trang thanh toán...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeActivity.class)); // hoặc CheckoutActivity.class nếu bạn có trang riêng
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_cart);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(this, HomeActivity1.class));
                } else if (itemId == R.id.nav_cart) {
                    return true;
                } else if (itemId == R.id.nav_account) {
                    startActivity(new Intent(this, AccountActivity.class));
                } else {
                    return false;
                }

                overridePendingTransition(0, 0);
                return true;
            });
        }
        RecyclerView recyclerView = findViewById(R.id.checkoutRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

// Bước 2: Khởi tạo danh sách và adapter
        List<Product> relatedList = new ArrayList<>();
        relatedList.add(new Product("Ốp lưng iPhone", "", "₫300,000", R.drawable.op));
        relatedList.add(new Product("Tai nghe Bluetooth", "", "₫150,000", R.drawable.tainghe));
// ... thêm sản phẩm nếu muốn

        ProductAdapter adapter = new ProductAdapter(this, relatedList);

// Bước 3: Gán adapter vào RecyclerView
        recyclerView.setAdapter(adapter);
    }

}
