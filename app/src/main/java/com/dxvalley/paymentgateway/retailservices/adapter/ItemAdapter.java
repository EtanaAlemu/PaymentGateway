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
import com.dxvalley.paymentgateway.retailservices.ItemsItemClickListener;
import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.retailservices.db.ItemDatabase;
import com.dxvalley.paymentgateway.retailservices.models.Item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemAdapter extends
        RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    Context context;

    private ItemsItemClickListener clickListener;

    public void setClickListener(ItemsItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        CircleImageView itemImage;
        TextView itemName;
        TextView itemPrice;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)  clickListener.onItemClick(view, getAdapterPosition()); // call the onClick in the OnItemClickListener
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

    public ItemAdapter() {
        ReloadData();
    }

    // Pass in the contact array into the constructor
    public ItemAdapter(List<Item> items) {
        this.itemList = items;
    }


    public void setItems(List<Item> items) {
        this.itemList = items;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.items_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
//        Item item = itemList.get(position);
        holder.itemView.setTag(position);
        // Set item views based on your views and data model
        holder.itemName.setText(itemList.get(position).getName());
        holder.itemPrice.setText(itemList.get(position).getPrice() + " Birr");
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
