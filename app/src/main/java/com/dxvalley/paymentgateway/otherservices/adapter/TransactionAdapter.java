package com.dxvalley.paymentgateway.otherservices.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.otherservices.NedajItemClickListener;
import com.dxvalley.paymentgateway.otherservices.model.Fuel;
import com.dxvalley.paymentgateway.otherservices.model.NedajTransaction;

import java.util.List;

public class TransactionAdapter extends
        RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    Context context;

//    private NedajItemClickListener clickListener;
    private int row_index = -1;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        TextView amount;
        TextView fuelType;
        TextView transactionId;
        TextView messageId;
        TextView time;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            amount = itemView.findViewById(R.id.amount);
            fuelType = itemView.findViewById(R.id.fuelType);
            transactionId = itemView.findViewById(R.id.transactionId);
            messageId = itemView.findViewById(R.id.messageId);
            time = itemView.findViewById(R.id.time);
        }

    }




    // Store a member variable for the contacts
    private List<NedajTransaction> itemList;

    // Pass in the contact array into the constructor
    public TransactionAdapter(List<NedajTransaction> items) {
        this.itemList = items;

    }



    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_transaction, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TransactionAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Get the data model based on position
//        Item item = itemList.get(position);

        holder.itemView.setTag(position);

        // Set item views based on your views and data model
        holder.transactionId .setText(itemList.get(position).getTransactionId());
        holder.amount.setText(itemList.get(position).getAmount());
        holder.messageId.setText(itemList.get(position).getMessageId());
        holder.fuelType.setText(itemList.get(position).getFuelType());
        holder.time.setText(itemList.get(position).getTime());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

}
