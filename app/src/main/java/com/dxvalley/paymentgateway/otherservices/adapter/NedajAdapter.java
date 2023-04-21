package com.dxvalley.paymentgateway.otherservices.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.otherservices.NedajItemClickListener;
import com.dxvalley.paymentgateway.otherservices.model.Fuel;
import com.dxvalley.paymentgateway.retailservices.ItemsItemClickListener;
import com.dxvalley.paymentgateway.retailservices.db.ItemDatabase;
import com.dxvalley.paymentgateway.retailservices.models.Item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NedajAdapter extends
        RecyclerView.Adapter<NedajAdapter.ViewHolder> {

    Context context;

    private NedajItemClickListener clickListener;
    private int row_index = -1;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        CardView cardView;
        TextView itemType;
        TextView itemPrice;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            cardView = itemView.findViewById(R.id.fuel_card);
            itemType = itemView.findViewById(R.id.fuel_type);
            itemPrice = itemView.findViewById(R.id.price);
        }

        public void bind(final Fuel item, final NedajItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                    row_index= getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }

    }



    // Store a member variable for the contacts
    private List<Fuel> itemList;

    // Pass in the contact array into the constructor
    public NedajAdapter(List<Fuel> items, NedajItemClickListener listener) {
        this.itemList = items;
        this.clickListener = listener;

    }



    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public NedajAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_fuel, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(NedajAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Get the data model based on position
//        Item item = itemList.get(position);

        holder.itemView.setTag(position);

        // Set item views based on your views and data model
        holder.itemType.setText(itemList.get(position).getType());
        holder.itemPrice.setText(itemList.get(position).getPrice() + " Birr/Liter");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                notifyDataSetChanged();
            }
        });

        if(row_index==position){

            holder.cardView.setCardBackgroundColor(Color.parseColor("#45b6fe"));
            holder.itemType.setTextColor(Color.parseColor("#ffffff"));
            holder.itemPrice.setTextColor(Color.parseColor("#ffffff"));
        }
        else
        {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
            holder.itemType.setTextColor(Color.parseColor("#000000"));
            holder.itemPrice.setTextColor(Color.parseColor("#000000"));
        }

        holder.bind(itemList.get(position), clickListener);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

}
