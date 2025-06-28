package com.example.nguyenthithuhuyen_2123110199;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity1 extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);

        initRecyclerView();
        setupBottomNavigation();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        if (recyclerView == null) {
            // Bạn có thể bật log hoặc thông báo nếu recyclerView bị null
            return;
        }

        productList = getSampleProducts();
        adapter = new ProductAdapter(this, productList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView == null) return;

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
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

    private List<Product> getSampleProducts() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("Điện thoại Iphone", "Giá bán: 15,000,000 ₫", "Giảm giá: Không áp dụng", R.drawable.dienthoai));
        list.add(new Product("Tai nghe Bluetooth Pro", "Giá bán: 2,500,000 ₫", "Giảm giá: 150,000 ₫", R.drawable.tainghe));
        list.add(new Product("Sạc dự phòng siêu tốc", "Giá bán: 800,000 ₫", "Giảm giá: Không áp dụng", R.drawable.sac));
        list.add(new Product("Ốp lưng iPhone 15", "Giá bán: 350,000 ₫", "Giảm giá: Không áp dụng", R.drawable.op));
        return list;
    }
}
