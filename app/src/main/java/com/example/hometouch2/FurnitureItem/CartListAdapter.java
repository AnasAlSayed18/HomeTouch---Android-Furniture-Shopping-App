package com.example.hometouch2.FurnitureItem;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hometouch2.R;
import com.google.gson.Gson;

import java.util.List;

public class CartListAdapter extends BaseAdapter {
    private Context context;
    private List<FurnitureItem> cartList;

    public CartListAdapter(Context context, List<FurnitureItem> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FurnitureItem item = cartList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        }

        ImageView itemImage = convertView.findViewById(R.id.itemImage);
        TextView itemName = convertView.findViewById(R.id.itemName);
        TextView itemPrice = convertView.findViewById(R.id.itemPrice);
        TextView itemStatus = convertView.findViewById(R.id.itemStatus);
        Button removeFromCartButton = convertView.findViewById(R.id.removeFromCartButton);

        itemImage.setImageResource(item.getImageResId());
        itemName.setText(item.getName());
        itemPrice.setText(item.getPrice());
        itemStatus.setText("Status: " + item.getStatus());

        removeFromCartButton.setOnClickListener(v -> {
            cartList.remove(position);
            notifyDataSetChanged();

            SharedPreferences prefs = context.getSharedPreferences("FurnitureAppPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            String updatedCartJson = gson.toJson(cartList);
            editor.putString("cartItems", updatedCartJson);
            editor.apply();

            Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();

            // Notify the activity
            if (cartUpdatedListener != null) {
                cartUpdatedListener.onCartUpdated();
            }
        });


        return convertView;
    }


    public interface OnCartUpdatedListener {
        void onCartUpdated();
    }
    private OnCartUpdatedListener cartUpdatedListener;

    public void setOnCartUpdatedListener(OnCartUpdatedListener listener) {
        this.cartUpdatedListener = listener;
    }

}

