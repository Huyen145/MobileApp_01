package com.example.nguyenthithuhuyen_2123110199;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Product implements Parcelable {
    private String productName;
    private int price;
    private int discount;
    private String image;
    private String category;
    private String description;
    private int view;
    private int quantity;

    // Constructor đầy đủ
    public Product(String productName, int price, int discount, String image,
                   String category, String description, int view, int quantity) {
        this.productName = productName;
        this.price = price;
        this.discount = discount;
        this.image = image;
        this.category = category;
        this.description = description;
        this.view = view;
        this.quantity = quantity;
    }

    // Constructor từ Parcel
    protected Product(Parcel in) {
        productName = in.readString();
        price = in.readInt();
        discount = in.readInt();
        image = in.readString();
        category = in.readString();
        description = in.readString();
        view = in.readInt();
        quantity = in.readInt();
    }

    // Creator để tạo từ Parcel
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    // Ghi dữ liệu vào Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeInt(price);
        dest.writeInt(discount);
        dest.writeString(image);
        dest.writeString(category);
        dest.writeString(description);
        dest.writeInt(view);
        dest.writeInt(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters
    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public int getDiscount() {
        return discount;
    }

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public int getView() {
        return view;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    // So sánh theo tên sản phẩm
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product other = (Product) obj;
        return productName.equals(other.productName);  // So sánh theo tên
    }

    // Chuyển đối tượng Product thành JSONObject
    public JSONObject toJson() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("productName", productName);
        obj.put("price", price);
        obj.put("discount", discount);
        obj.put("image", image);
        obj.put("category", category);
        obj.put("description", description);
        obj.put("quantity", quantity);
        obj.put("view", view);
        return obj;
    }
    // Tạo đối tượng Product từ JSONObject
    public static Product fromJson(JSONObject obj) throws JSONException {
        String name = obj.getString("productName");
        int price = obj.getInt("price");
        int discount = obj.getInt("discount");
        String image = obj.getString("image");
        String category = obj.getString("category");
        String description = obj.getString("description");
        int view = obj.getInt("view");
        int quantity = obj.getInt("quantity");

        return new Product(name, price, discount, image, category, description, view, quantity);
    }

}
