package com.codeinfinity.homescopebusiness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeDetailsActivity extends AppCompatActivity {

    private String propertyName;
    public String priceTextView;

    public String descriptionTextView;
    public String addressTextView;

    public String homeAreas;
    public String beds;
    public String baths, facilities, dates;
    String living;
    String imagePath;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    String currentUserId = user.getUid();

    public ImageView imageView;

    TextView name, desc, address, price, area, bed, bath, livingRoom, facility, dateUpload;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_details);


        name = findViewById(R.id.nameHome);
        image = findViewById(R.id.homeDetailsImage);
        desc = findViewById(R.id.descriptionHome);
        address = findViewById(R.id.addressFlat);
        price = findViewById(R.id.priceHome);
        area = findViewById(R.id.areaValue);
        bed = findViewById(R.id.bedNoTxt);
        bath = findViewById(R.id.bathNoTxt);
        livingRoom = findViewById(R.id.livingNoTxt);
        facility = findViewById(R.id.facilitiesText);
        dateUpload = findViewById(R.id.dateText);

        Intent intent = getIntent();
        if (intent != null) {

            propertyName = intent.getStringExtra("property_name");
            priceTextView = intent.getStringExtra("property_price");
            descriptionTextView = intent.getStringExtra("property_description");
            addressTextView = intent.getStringExtra("property_address");
            homeAreas = intent.getStringExtra("property_area");
            beds = intent.getStringExtra("property_beds");
            baths = intent.getStringExtra("property_baths");
            living = intent.getStringExtra("property_living");
            facilities = intent.getStringExtra("property_facilities");
            imagePath = intent.getStringExtra("image_path");
            dates = intent.getStringExtra("property_date");


            Glide.with(this)
                    .load(imagePath)  //Assuming you have a getImageUrl() method in your Property class that returns the image URL
                    .into(image);


        }
        name.setText(propertyName);
        desc.setText(descriptionTextView);
        address.setText(addressTextView);
        price.setText("Rs. " + priceTextView);
        facility.setText(facilities);
        area.setText(homeAreas);
        bed.setText(beds);
        dateUpload.setText(dates);
        bath.setText(baths);
        livingRoom.setText(living);

    }
}