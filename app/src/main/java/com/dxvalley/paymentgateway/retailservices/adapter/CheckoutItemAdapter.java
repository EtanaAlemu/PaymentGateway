package com.dxvalley.paymentgateway.retailservices.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dxvalley.paymentgateway.retailservices.CheckoutItemInterface;
import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.retailservices.db.ItemDatabase;
import com.dxvalley.paymentgateway.retailservices.models.Item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CheckoutItemAdapter extends
        RecyclerView.Adapter<CheckoutItemAdapter.ViewHolder> {

    Context context;

    private CheckoutItemInterface itemInterface;

    public void setCount(CheckoutItemInterface count) {
        this.itemInterface = count;
    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        CircleImageView itemImage;
        TextView itemName;
        TextView itemCount;
        TextView itemPrice;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemCount = itemView.findViewById(R.id.item_count);
            itemPrice = itemView.findViewById(R.id.item_price);
        }

    }


    private void loadImageFromStorage(String imageName, ViewHolder holder)
    {

        try {
            File f=new File("/data/data/com.dxvalley.paymentgateway/app_image", imageName);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            Glide.with(holder.itemImage)
                    .asBitmap()
                    .load(b).into(holder.itemImage);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    // Store a member variable for the contacts
    private List<Item> itemList;

    public CheckoutItemAdapter() {
        ReloadData();
    }

    // Pass in the contact array into the constructor
    public CheckoutItemAdapter(List<Item> items) {
        this.itemList = items;
    }


    public void setItems(List<Item> items) {
        this.itemList = items;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public CheckoutItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_checkout, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(CheckoutItemAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
//        Item item = itemList.get(position);
        int count = itemInterface.setCount(position);
        holder.itemView.setTag(position);
        // Set item views based on your views and data model
        holder.itemName.setText(itemList.get(position).getName());
        holder.itemCount.setText(String.valueOf(count));
        holder.itemPrice.setText((Float.valueOf(itemList.get(position).getPrice()) * count) + " Birr");
        loadImageFromStorage(itemList.get(position).getImageName(), holder);

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    public void ReloadData(){

        ItemDatabase database = ItemDatabase.getInstance(context);
        itemList = database.itemDao().getItemList();

//        ItemDatabase database = Calculator.database.CalculatorDao().getAllCalculations();
        this.notifyDataSetChanged();
    }
}
