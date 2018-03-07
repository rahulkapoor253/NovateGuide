package com.example.rahulkapoor.novateguide.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rahulkapoor.novateguide.R;
import com.example.rahulkapoor.novateguide.interfaces.DirectionCallback;

import java.util.ArrayList;

/**
 * Created by rahulkapoor on 06/03/18.
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    private Context mContext;
    private ArrayList<String> placesList = new ArrayList<>();
    private DirectionCallback directionCallback;

    public PlacesAdapter(final Context context, final ArrayList<String> list, final DirectionCallback callback) {

        this.mContext = context;
        this.placesList = list;
        this.directionCallback = callback;

    }

    @Override
    public PlacesAdapter.PlaceViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.places_list_item, parent, false);

        return new PlacesAdapter.PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlacesAdapter.PlaceViewHolder holder, final int position) {

        holder.tvPlaceItem.setText(placesList.get(position));

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //return the index and string for setting up marker and lat/lng;
                directionCallback.getPosition(holder.getAdapterPosition());

            }
        });

    }

    @Override
    public int getItemCount() {

        return placesList.size();
    }


    public class PlaceViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llItem;
        private TextView tvPlaceItem;

        public PlaceViewHolder(final View itemView) {
            super(itemView);

            llItem = (LinearLayout) itemView.findViewById(R.id.ll_item);
            tvPlaceItem = (TextView) itemView.findViewById(R.id.tv_item_place);

        }
    }
}
