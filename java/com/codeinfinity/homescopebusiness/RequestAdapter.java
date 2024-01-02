package com.codeinfinity.homescopebusiness;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<Requests> propertyList;

    public RequestAdapter(List<Requests> propertyList) {
        this.propertyList = propertyList;
    }

    public List<Requests> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<Requests> propertyList) {
        this.propertyList = propertyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.requests_recycle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Requests requests = propertyList.get(position);

        // Set the property data to the appropriate views in the item layout
        holder.nameRecycle.setText(requests.getName());
         holder.user.setText(requests.getUserName());

         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(v.getContext(), AgreeRejectActivity.class);
                 intent.putExtra("request_name", requests.getName());
                 intent.putExtra("request_price", requests.getPrice());
                 intent.putExtra("request_number", requests.getUserNumber());
                 intent.putExtra("requested_name", requests.getUserName());
                 intent.putExtra("requested_uid", requests.getUserUid());



                 v.getContext().startActivity(intent);
             }
         });
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameRecycle;
        public TextView user;


        // Add other views for property fields

        public ViewHolder(View itemView) {
            super(itemView);

            nameRecycle = itemView.findViewById(R.id.requestName);
            user = itemView.findViewById(R.id.requestedNames);



            // Initialize other views
        }
    }
}
