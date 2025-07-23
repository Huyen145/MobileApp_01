package com.example.nguyenthithuhuyen_2123110199;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    EditText etFullName, etPhone, etAddress;
    RadioGroup paymentMethodGroup;
    RadioButton rbCOD, rbBankTransfer;
    Button btnConfirmOrder;
    RecyclerView recyclerView;
    ArrayList<Product> productList;
    PayAdapter payAdapter;

    TextView tvSubtotal, tvDiscount, tvShippingFee, tvTotalPrice;
    LinearLayout layoutBankInfo;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Ánh xạ view
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        rbCOD = findViewById(R.id.rbCOD);
        rbBankTransfer = findViewById(R.id.rbBankTransfer);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        recyclerView = findViewById(R.id.recyclerViewCart);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        layoutBankInfo = findViewById(R.id.layoutBankInfo);

        // Lấy thông tin người dùng đã đăng nhập
        SharedPreferences userPref = getSharedPreferences("user", MODE_PRIVATE);
        String fullName = userPref.getString("fullName", "");
        String phone = userPref.getString("phone", "");
        String address = userPref.getString("address", "");

        etFullName.setText(fullName);
        etPhone.setText(phone);
        etAddress.setText(address);

        paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbBankTransfer) {
                layoutBankInfo.setVisibility(View.VISIBLE);
            } else {
                layoutBankInfo.setVisibility(View.GONE);
            }
        });

        sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        String json = sharedPreferences.getString("cart_items", null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Product>>() {}.getType();
            productList = gson.fromJson(json, type);
        } else {
            productList = new ArrayList<>();
        }

        // Sử dụng PayAdapter thay vì CartAdapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        payAdapter = new PayAdapter(productList, this);
        recyclerView.setAdapter(payAdapter);

        updateTotalPrice();

        btnConfirmOrder.setOnClickListener(v -> {
            String name = etFullName.getText().toString().trim();
            String phoneStr = etPhone.getText().toString().trim();
            String addressStr = etAddress.getText().toString().trim();
            int selectedId = paymentMethodGroup.getCheckedRadioButtonId();

            if (name.isEmpty() || phoneStr.isEmpty() || addressStr.isEmpty() || selectedId == -1) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            String paymentMethod = (selectedId == R.id.rbCOD)
                    ? "Thanh toán khi nhận hàng"
                    : "Chuyển khoản ngân hàng";

            Toast.makeText(this, "Đặt hàng thành công!\nPhương thức: " + paymentMethod, Toast.LENGTH_LONG).show();

            // Xóa giỏ hàng
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("cart_items");
            editor.apply();

            productList.clear();
            payAdapter.notifyDataSetChanged();
            updateTotalPrice();

            // Quay về trang chủ
            Intent intent = new Intent(HomeActivity.this, HomeActivity1.class);
            startActivity(intent);
            finish();
        });
    }

    private void updateTotalPrice() {
        int subtotal = 0;
        int totalDiscount = 0;
        int shippingFee = 21000;

        for (Product product : productList) {
            subtotal += product.getPrice() * product.getQuantity();
            totalDiscount += product.getDiscount() * product.getQuantity();
        }

        int finalTotal = subtotal - totalDiscount + shippingFee;

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvSubtotal.setText("Thành tiền: " + formatter.format(subtotal) + "₫");
        tvDiscount.setText("- Giảm giá: " + formatter.format(totalDiscount) + "₫");
        tvShippingFee.setText("+ Phí giao hàng: " + formatter.format(shippingFee) + "₫");
        tvTotalPrice.setText("Tổng cộng: " + formatter.format(finalTotal) + "₫");
    }
}
