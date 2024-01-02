package com.codeinfinity.homescopebusiness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SeeAllActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    TextView listed, worth;
    private PropertyAdapter adapter;

    private DatabaseReference propertiesRef;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();


    String uid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);


        recyclerView = findViewById(R.id.recyclerViewSeeAll);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PropertyAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);


        propertiesRef = FirebaseDatabase.getInstance().getReference("userUploads").child(uid);

        retrieveDataFromFirebase();

        /*CardView cardView = findViewById(R.id.addHomeBtn);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the activity here
                Intent intent = new Intent(SeeAllActivity.this, HomeDetailsActivity.class);
                startActivity(intent);
            }
        });*/

        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_seller_home, container, false);

        // Inside your Fragment's onCreateView() method or any appropriate place


    }

    private void retrieveDataFromFirebase() {
        propertiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Property> propertyList = new ArrayList<>();
                for (DataSnapshot propertySnapshot : dataSnapshot.getChildren()) {
                    Property property = propertySnapshot.getValue(Property.class);
                    propertyList.add(property);
                }
                adapter.setPropertyList(propertyList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }


}