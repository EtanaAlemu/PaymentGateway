package com.dxvalley.paymentgateway.adapter;

import static com.dxvalley.paymentgateway.MainActivity.TIP_ADAPTER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dxvalley.paymentgateway.ItemClickListener;
import com.dxvalley.paymentgateway.MainActivity;
import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.db.TipDatabase;
import com.dxvalley.paymentgateway.models.Tip;

import java.util.List;

public class TipAdapter extends
        RecyclerView.Adapter<TipAdapter.ViewHolder> {

    Context context;

    private ItemClickListener clickListener;

    public void setClickListener(ItemClickListener tipClickListener) {
        this.clickListener = tipClickListener;
    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        TextView mTipValue;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            mTipValue = itemView.findViewById(R.id.tip_value);

            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)  clickListener.onClick(view, getAdapterPosition(), TIP_ADAPTER); // call the onClick in the OnItemClickListener
        }
    }

    // Store a member variable for the contacts
    private List<Tip> tipList;

    public TipAdapter() {
        ReloadData();
    }

    // Pass in the contact array into the constructor
    public TipAdapter(List<Tip> tips) {
        tipList = tips;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public TipAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.tip_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TipAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Tip tip = tipList.get(position);
        holder.itemView.setTag(position);
        // Set item views based on your views and data model
        boolean isByPercent = tip.isByPercent();
        String unit;
        if(isByPercent) unit = " %";
        else unit = " Birr";
        holder.mTipValue.setText(tipList.get(position).getValue() + unit);

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return tipList == null ? 0 : tipList.size();
    }

    public void ReloadData(){

        TipDatabase database = TipDatabase.getInstance(context);
        tipList = database.tipDao().getTipList();
        this.notifyDataSetChanged();
    }
}
