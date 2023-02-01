package com.dxvalley.paymentgateway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends
        RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    Context context;

    private ItemClickListener clickListener;

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        ImageView itemImage;
        TextView itemName;
        TextView itemDescription;
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
            if (clickListener != null)  clickListener.onClick(view, getAdapterPosition()); // call the onClick in the OnItemClickListener
        }
    }

    // Store a member variable for the contacts
    private List<Item> itemList;

    public ItemAdapter() {
        ReloadData();
    }

    // Pass in the contact array into the constructor
    public ItemAdapter(List<Item> items) {
        itemList = items;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_list, parent, false);

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
