package com.example.hometouch2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hometouch2.FurnitureItem.CartListAdapter;
import com.example.hometouch2.FurnitureItem.FurnitureItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {


    // Bottom navigation view
    private BottomNavigationView bottomNav;
    private ListView cartListView;
    private TextView totalSumText;
    private List<FurnitureItem> cartItems;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private double total;


    SharedPreferences itemPrefs ;
    SharedPreferences.Editor itemEditor;

    private Button checkoutButton;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);



        setUpUserData();
        loadCartItems();
        setUpItemData();
        calculateTotalPrice();
        onOptionsItemSelected();
        onCheckoutButton();
    }


    private void setUpUserData () {
        pref = getSharedPreferences("userData", MODE_PRIVATE);
        editor = pref.edit();
    }

    private void setUpItemData () {
        itemPrefs = getSharedPreferences("FurniturePrefs", MODE_PRIVATE);
        itemEditor = itemPrefs.edit();
    }

    public void calculateTotalPrice() {
        total = 0;
        for (FurnitureItem item : cartItems) {
            try {
                total += Double.parseDouble(item.getPrice().replaceAll("[^\\d.]", ""));
            } catch (Exception ignored) {}
        }
        totalSumText = findViewById(R.id.totalSumText);
        totalSumText.setText("Total: $" + String.format("%.2f", total));

    }

    public void loadCartItems() {
        cartListView = findViewById(R.id.cartListView);
        SharedPreferences sharedPreferences = getSharedPreferences("FurnitureAppPrefs", MODE_PRIVATE);
        String json = sharedPreferences.getString("cartItems", null);

        Gson gson = new Gson();
        Type type = new TypeToken<List<FurnitureItem>>() {}.getType();
        cartItems = json != null ? gson.fromJson(json, type) : new ArrayList<>();

        CartListAdapter adapter = new CartListAdapter(this, cartItems);

        adapter.setOnCartUpdatedListener(() -> calculateTotalPrice());

        cartListView.setAdapter(adapter);
    }


    private void onCheckoutButton() {
        double balance = pref.getFloat("balance", 0f);

        checkoutButton = findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to discount "+total+" from your blance"+balance+"$ ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {
                            if (total > balance) {
                                showDialog("Insufficient balance!");
                            } else {
                                double newBalance = balance - total;
                                editor.putFloat("balance", (float) newBalance);
                                editor.apply();

                                updateQuantity();
                                clearCart();
                                showDialog("Checkout successful! New balance: $" + String.format("%.2f", newBalance));
                            }

                        })
                        .setNegativeButton("No", (dialog, id) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void updateQuantity() {
        for (FurnitureItem item : cartItems) {
            int newQuantity = item.getQuantity() - 1;
            item.setQuantity(newQuantity);
        }
        List<FurnitureItem> furnitureList= SharedPreferencesHelper.getFurnitureList(this);

        for (FurnitureItem item : furnitureList) {
            for (FurnitureItem cartItem : cartItems) {
                if (item.getName().equals(cartItem.getName())) {
                    item.setQuantity(cartItem.getQuantity());
                }

                if(item.getQuantity() <= 0) {
                  item.setStatus("Sold Out");
                }
            }
        }

        SharedPreferencesHelper.saveFurnitureList(this, furnitureList);
    }

    private void clearCart() {
        cartItems.clear();
        SharedPreferences sharedPreferences = getSharedPreferences("FurnitureAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cartItems);
        editor.putString("cartItems", json);
        editor.apply();
        loadCartItems();
        calculateTotalPrice();
    }

   private  void showDialog(String message) {
        builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }




    public void onOptionsItemSelected() {
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_cart);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out); // Animation for home transition
                finish();
                return true;

            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out); // Animation for search transition
                finish();
                return true;

            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out); // Animation for cart transition
                finish();
                return true;

            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out); // Animation for profile transition
                finish();
                return true;
            }
            return false;

        });


    }





}