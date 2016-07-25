package com.scu.housing.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scu.housing.R;
import com.scu.housing.activities.HouseDetailsActivity;
import com.scu.housing.other.House;

import java.util.ArrayList;

public class HouseListAdapter extends RecyclerView.Adapter<HouseListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<House> houses;

    public HouseListAdapter(Context context, ArrayList<House> houses) {
        this.context = context;
        this.houses = houses;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout houseListItem;
        public TextView houseAddressTextView;
        public CheckBox applyCheckBox;

        public ViewHolder(View view) {
            super(view);
            houseListItem = (RelativeLayout) view;
            houseAddressTextView = (TextView) view.findViewById(R.id.house_text_view);
            applyCheckBox = (CheckBox) view.findViewById(R.id.apply_check_box);
        }

    }

    @Override
    public HouseListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.house_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HouseListAdapter.ViewHolder holder, int position) {
        holder.houseAddressTextView.setText(houses.get(position).getAddress());
        final House house = houses.get(holder.getAdapterPosition());
        holder.houseListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Only pass house ID, download house from database in HouseDatailsActivity.
                Intent intent = new Intent(context, HouseDetailsActivity.class);
                intent.putExtra("address", house.getAddress());
                intent.putExtra("available", house.isAvailable());
                intent.putExtra("bath", house.getBath());
                intent.putExtra("bed", house.getBed());
                intent.putExtra("description", house.getDescription());
                intent.putExtra("price", house.getPrice());
                intent.putExtra("latitude", house.getLatitude());
                intent.putExtra("longitude", house.getLongitude());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return houses.size();
    }
}
