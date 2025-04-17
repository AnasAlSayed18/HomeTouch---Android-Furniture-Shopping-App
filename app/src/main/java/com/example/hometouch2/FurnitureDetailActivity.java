package com.example.hometouch2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hometouch2.FurnitureItem.FurnitureItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FurnitureDetailActivity extends AppCompatActivity {

    ImageView itemImage;
    TextView itemName, itemPrice, itemQuantity, itemType, itemStatus,quantityPicker;
    Button backButton, addToCartButton;

    FurnitureItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furniture_detail);


        setUpViews();
        loadBundleData();
        onAddToCart();

    }

    private  void setUpViews(){
        itemImage = findViewById(R.id.detailImage);
        itemName = findViewById(R.id.detailName);
        itemPrice = findViewById(R.id.detailPrice);
        itemQuantity = findViewById(R.id.detailQuantity);
        itemType = findViewById(R.id.detailType);
        itemStatus = findViewById(R.id.detailStatus);
        backButton = findViewById(R.id.backButton);
        quantityPicker = findViewById(R.id.quantityPicker);
        addToCartButton = findViewById(R.id.addToCartButton);


        backButton.setOnClickListener(v -> finish()); // go back to previous screen

    }

    private void loadBundleData(){
        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentItem = new FurnitureItem(
                    extras.getString("name"),
                    extras.getString("price"),
                    extras.getInt("image"),
                    extras.getInt("quantity"),
                    extras.getString("type"),
                    extras.getString("status")
            );

            itemImage.setImageResource(currentItem.getImageResId());
            itemName.setText(currentItem.getName());
            itemPrice.setText(currentItem.getPrice());
            itemQuantity.setText("Quantity: " + currentItem.getQuantity());
            itemType.setText("Type: " + currentItem.getType());
            itemStatus.setText("Status: " + currentItem.getStatus());
        }
    }

    private void onAddToCart() {
        addToCartButton.setOnClickListener(v -> {
            if (currentItem != null) {
                int selectedQty=1;
                if (quantityPicker.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please select a quantity", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedQty = Integer.parseInt(quantityPicker.getText().toString());

                if (selectedQty <= 0) {
                    Toast.makeText(this, "Please select a valid quantity", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new item copy with selected quantity
                FurnitureItem selectedItem = new FurnitureItem(
                        currentItem.getName(),
                        currentItem.getPrice(),
                        currentItem.getImageResId(),
                        selectedQty,
                        currentItem.getType(),
                        currentItem.getStatus()
                );


                for (int i = 0; i < selectedQty; i++) {
                    addToCart(selectedItem);
                }
                Toast.makeText(this, selectedQty+" Item "+"added to cart 🛒", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error: Item is null", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToCart(FurnitureItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences("FurnitureAppPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String existingCart = sharedPreferences.getString("cartItems", null);

        Type type = new TypeToken<List<FurnitureItem>>() {}.getType();
        List<FurnitureItem> cartList = existingCart != null ? gson.fromJson(existingCart, type) : new ArrayList<>();


        cartList.add(item);
        Toast.makeText(this, "Added to cart 🛒", Toast.LENGTH_SHORT).show();


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cartItems", gson.toJson(cartList));
        editor.apply();

    }
}
