package com.example.nguyenthithuhuyen_2123110199;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity1 extends AppCompatActivity {
    RecyclerView recyclerView;
    ProductAdapter adapter;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);

        // KHẮC PHỤC: Bỏ comment dòng này để khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerView); // Đảm bảo bạn có một RecyclerView với id là "recyclerView" trong activity_home1.xml

        productList = getSampleProducts();

        // Kiểm tra nếu recyclerView không null trước khi sử dụng (tốt nhất là bạn đã khởi tạo nó ở trên)
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            adapter = new ProductAdapter(this, productList);
            recyclerView.setAdapter(adapter);
        } else {
            // In thông báo lỗi hoặc xử lý phù hợp nếu không tìm thấy RecyclerView
            // Ví dụ: Log.e("HomeActivity1", "RecyclerView with ID recyclerView not found in layout.");
            // Hoặc hiển thị một Toast message cho người dùng.
        }
    }

    private List<Product> getSampleProducts() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("Điện thoại Iphone", "Giá bán: 15,000,000 ₫", "Giảm giá: Không áp dụng", R.drawable.dienthoai));
        list.add(new Product("Tai nghe Bluetooth Pro", "Giá bán: 2,500,000 ₫", "Giảm giá: 150,000 ₫", R.drawable.tainghe));
        list.add(new Product("Sạc dự phòng siêu tốc", "Giá bán: 800,000 ₫", "Giảm giá: Không áp dụng", R.drawable.sac));
        list.add(new Product("Ốp lưng  iPhone 15", "Giá bán: 350,000 ₫", "Giảm giá: Không áp dụng", R.drawable.op));
        return list;
    }

}
