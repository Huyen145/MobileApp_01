package com.example.nguyenthithuhuyen_2123110199;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

    EditText etFullName, etPhone, etAddress;
    RadioGroup paymentMethodGroup;
    RadioButton rbCOD, rbBankTransfer;
    Button btnConfirmOrder;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Fix hiển thị chèn status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        rbCOD = findViewById(R.id.rbCOD);
        rbBankTransfer = findViewById(R.id.rbBankTransfer);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        recyclerView = findViewById(R.id.checkoutRecyclerView);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Ốp lưng iPhone", "", "₫300,000", R.drawable.op));
        productList.add(new Product("Tai nghe Bluetooth", "", "₫150,000", R.drawable.tainghe));
        ProductAdapter adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        // Bắt sự kiện đặt hàng
        btnConfirmOrder.setOnClickListener(v -> {
            String name = etFullName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin giao hàng!", Toast.LENGTH_SHORT).show();
                return;
            }

            String paymentMethod = rbCOD.isChecked() ? "Thanh toán khi nhận hàng (COD)" : "Chuyển khoản ngân hàng";

            Toast.makeText(this,
                    "Đặt hàng thành công!\nTên: " + name + "\nSĐT: " + phone + "\nPTTT: " + paymentMethod,
                    Toast.LENGTH_LONG).show();

            // TODO: Thêm xử lý như: xóa giỏ hàng, lưu đơn hàng,...
        });
    }
}
