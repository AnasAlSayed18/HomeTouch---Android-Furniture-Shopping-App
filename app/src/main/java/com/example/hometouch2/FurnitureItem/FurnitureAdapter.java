package com.example.hometouch2.FurnitureItem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hometouch2.FurnitureDetailActivity;
import com.example.hometouch2.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FurnitureAdapter extends RecyclerView.Adapter<FurnitureAdapter.ViewHolder> {
    private Context context;
    private List<FurnitureItem> itemList;

    public FurnitureAdapter(Context context, List<FurnitureItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemPrice, itemQuantity, itemStatus;
        Button addToCartButton;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemStatus = itemView.findViewById(R.id.itemStatus);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_furniture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FurnitureItem item = itemList.get(position);

        holder.itemImage.setImageResource(item.getImageResId());
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText(item.getPrice());
        holder.itemQuantity.setText("Quantity: " + item.getQuantity());
        holder.itemStatus.setText("Status: " + item.getStatus());


        holder.addToCartButton.setOnClickListener(v -> {

            if (item.getQuantity() <= 0 || item.getStatus().equals("Sold Out")) {
                Toast.makeText(context, "Item is out of stock", Toast.LENGTH_SHORT).show();
                return;
            }

            addToCart(item);
            Toast.makeText(context, item.getName() + " added to cart 🛒", Toast.LENGTH_SHORT).show();
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FurnitureDetailActivity.class);
            intent.putExtra("name", item.getName());
            intent.putExtra("price", item.getPrice());
            intent.putExtra("image", item.getImageResId());
            intent.putExtra("quantity", item.getQuantity());
            intent.putExtra("type", item.getType());
            intent.putExtra("status", item.getStatus());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateList(List<FurnitureItem> newList) {
        this.itemList = newList;
        notifyDataSetChanged();
    }

    private void addToCart(FurnitureItem item) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("FurnitureAppPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String existingCart = sharedPreferences.getString("cartItems", null);
        Type type = new TypeToken<List<FurnitureItem>>() {}.getType();
        List<FurnitureItem> cartList = existingCart != null ? gson.fromJson(existingCart, type) : new ArrayList<>();

        cartList.add(item); // Add item

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cartItems", gson.toJson(cartList));
        editor.apply();
    }
}
