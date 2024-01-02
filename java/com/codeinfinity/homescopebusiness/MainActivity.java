package com.codeinfinity.homescopebusiness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationseller, bottomNavigationbuyer;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();


    String uid = user.getUid();


    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationseller = findViewById(R.id.bottomNavBarSeller);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SellerHomeFragment())
                .commit();


        bottomNavigationseller.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getItemId() == R.id.homeSellerMenu) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new SellerHomeFragment())
                                    .commit();
                        } else if (item.getItemId() == R.id.notificationMenu) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new StatFragment())
                                    .commit();
                        } else if (item.getItemId() == R.id.profileSellerMenu) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new SellerProfileFragment())
                                    .commit();
                        }

                        return true;
                    }
                });


        // Set the default fragment to the HomeFragment

    }
}