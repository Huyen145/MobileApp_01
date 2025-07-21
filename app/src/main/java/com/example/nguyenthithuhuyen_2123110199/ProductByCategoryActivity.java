package com.example.nguyenthithuhuyen_2123110199;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductByCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter adapter;
    List<Product> filteredList;
    RequestQueue requestQueue;

    private final String PRODUCT_API_URL = "https://6868e205d5933161d70cb9e2.mockapi.io/products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_by_category);

        recyclerView = findViewById(R.id.recyclerViewByCategory);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        filteredList = new ArrayList<>();
        adapter = new ProductAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);

        String selectedCategory = getIntent().getStringExtra("category");
        requestQueue = Volley.newRequestQueue(this);

        fetchProductsByCategory(selectedCategory);
    }

    private void fetchProductsByCategory(String category) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                PRODUCT_API_URL,
                null,
                response -> {
                    filteredList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String productCategory = obj.getString("category");

                            if (productCategory.equalsIgnoreCase(category)) {
                                String name = obj.getString("productName");
                                int price = obj.getInt("price");
                                int discount = obj.getInt("discount");
                                String image = obj.getString("image");
                                String description = obj.getString("description");
                                int view = obj.getInt("view");
                                int quantity = obj.getInt("quantity");

                                Product product = new Product(name, price, discount, image, productCategory, description, view, quantity);
                                filteredList.add(product);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> Log.e("CategoryAPI", "Lỗi API: " + error.toString())
        );

        requestQueue.add(request);
    }
}
