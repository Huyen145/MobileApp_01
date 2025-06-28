package com.example.nguyenthithuhuyen_2123110199;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Xử lý padding system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Nút Back quay về HomeActivity1
        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> {
            Intent it = new Intent(getApplicationContext(), HomeActivity1.class);
            startActivity(it);
        });


        // Bước 1: Gắn RecyclerView
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
