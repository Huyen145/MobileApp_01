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
    CartAdapter adapter;

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

        // Hiển thị hoặc ẩn thông tin ngân hàng
        paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbBankTransfer) {
                layoutBankInfo.setVisibility(View.VISIBLE);
            } else {
                layoutBankInfo.setVisibility(View.GONE);
            }
        });

        // Lấy dữ liệu từ SharedPreferences
        sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        String json = sharedPreferences.getString("cart_items", null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Product>>() {}.getType();
            productList = gson.fromJson(json, type);
        } else {
            productList = new ArrayList<>();
        }

        // Hiển thị danh sách sản phẩm
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(productList, this, new CartAdapter.OnItemActionListener() {
            @Override
            public void onItemDelete(int position) {
                // Trang thanh toán: không cho xoá sản phẩm
            }

            @Override
            public void onQuantityChanged() {
                updateTotalPrice();
                saveCartToSharedPreferences();
            }
        });

        recyclerView.setAdapter(adapter);
        updateTotalPrice();

        btnConfirmOrder.setOnClickListener(v -> {
            String name = etFullName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            int selectedId = paymentMethodGroup.getCheckedRadioButtonId();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || selectedId == -1) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            String paymentMethod = (selectedId == R.id.rbCOD)
                    ? "Thanh toán khi nhận hàng"
                    : "Chuyển khoản ngân hàng";

            Toast.makeText(this, "Đặt hàng thành công!\nPhương thức: " + paymentMethod, Toast.LENGTH_LONG).show();

            // Xoá giỏ hàng
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("cart_items");
            editor.apply();

            productList.clear();
            adapter.notifyDataSetChanged();
            updateTotalPrice();

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

    private void saveCartToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(productList);
        editor.putString("cart_items", json);
        editor.apply();
    }
}
