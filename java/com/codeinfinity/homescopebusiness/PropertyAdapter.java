package com.codeinfinity.homescopebusiness;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.BreakIterator;
import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {
    private List<Property> propertyList;

    public PropertyAdapter(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_featured, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Property property = propertyList.get(position);

        // Set the property data to the appropriate views in the item layout
        holder.nameTextView.setText(property.getName());
        holder.priceTextView.setText("Rs. "+String.valueOf(property.getPrice()));
        holder.addressTextView.setText(property.getAddress());


        holder.beds.setText(String.valueOf(property.getBedrooms()));
        holder.baths.setText(String.valueOf(property.getBathrooms()));

        Glide.with(holder.itemView.getContext())
                .load(property.getImage())  // Assuming you have a getImageUrl() method in your Property class that returns the image URL
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity and pass the property data
                Intent intent = new Intent(v.getContext(), HomeDetailsActivity.class);
                //intent.putExtra("property_id", property.getId());
                intent.putExtra("property_name", property.getName());
                intent.putExtra("property_price", property.getPrice());
                intent.putExtra("property_description", property.getDescription());
                intent.putExtra("property_address", property.getAddress());
                intent.putExtra("image_path", property.getImage());
                intent.putExtra("property_area", property.getAreaSqft());
                intent.putExtra("property_beds", property.getBedrooms());
                intent.putExtra("property_baths", property.getBathrooms());
                intent.putExtra("property_living", property.getLivingRooms());
                intent.putExtra("property_facilities", property.getFacilities());
                intent.putExtra("property_uid", property.getUid());
                intent.putExtra("property_date", property.getDate());
                v.getContext().startActivity(intent);
            }
        });
        // Set other property fields
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView priceTextView;
        public TextView addressTextView;

        public TextView description;

        public TextView facilities;

        public TextView homeAreas;
        public TextView beds;
        public TextView baths;

        public TextView date;

        public ImageView imageView;


        // Add other views for property fields

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.homeName1);
            priceTextView = itemView.findViewById(R.id.homePrice);
            addressTextView = itemView.findViewById(R.id.homeAddress);


            beds = itemView.findViewById(R.id.bedCount);
            baths = itemView.findViewById(R.id.bathCount);
            imageView = itemView.findViewById(R.id.flatImage1);

            // Initialize other views
        }
    }
}



