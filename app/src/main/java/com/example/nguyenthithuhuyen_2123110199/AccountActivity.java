package com.example.nguyenthithuhuyen_2123110199;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        boolean isLoggedIn = getSharedPreferences("user", MODE_PRIVATE)
                .getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Toast.makeText(this, "Vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        findViewById(R.id.logoutSection).setOnClickListener(v -> {
            // Xóa trạng thái đăng nhập
            SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
            prefs.edit().clear().apply();

            // Chuyển về màn hình đăng nhập
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_account);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(this, HomeActivity1.class));
                } else if (itemId == R.id.nav_account) {
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(this, CartActivity.class));
                } else {
                    return false;
                }

                overridePendingTransition(0, 0);
                return true;
            });
        }
    }
}