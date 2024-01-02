package com.codeinfinity.homescopebusiness;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellerHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;

    private static final String CHANNEL_ID = "notification_channel";
    private static final int NOTIFICATION_ID = 1;

    TextView listed, worth, seeAll;
    private PropertyAdapter adapter;

    private DatabaseReference propertiesRef;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();


    String uid = user.getUid();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SellerHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerHomeFragment newInstance(String param1, String param2) {
        SellerHomeFragment fragment = new SellerHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_seller_home, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewHome);
        listed = rootView.findViewById(R.id.propertyNumber);
        worth = rootView.findViewById(R.id.investedNumber);
        seeAll = rootView.findViewById(R.id.seeAllText);

        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SeeAllActivity.class));
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new PropertyAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);




        propertiesRef = FirebaseDatabase.getInstance().getReference("userUploads").child(uid);

        retrieveDataFromFirebase();

        CardView cardView = rootView.findViewById(R.id.addHomeBtn);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the activity here
                Intent intent = new Intent(getActivity(), AddPropertyActivity.class);
                startActivity(intent);
            }
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("sellers").child(uid);
        userRef.child("listed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the current listed value
                    int currentListed = dataSnapshot.getValue(Integer.class);
                    listed.setText(String.valueOf(currentListed));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userRef.child("worth").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the current listed value
                    int currentWorth = dataSnapshot.getValue(Integer.class);
                    worth.setText(String.valueOf(currentWorth));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        showNotification("chandu");

        return rootView;
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_seller_home, container, false);

        // Inside your Fragment's onCreateView() method or any appropriate place


    }

    private void showNotification(String name) {
        // Create an intent for the action when the notification is clicked
        // For example, open a specific activity or perform a specific action
        // Replace YourActivity.class with your desired activity class
        Intent intent = new Intent(getActivity(), StatFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.register_top)
                .setContentTitle("Data Change Notification")
                .setContentText("Name has changed to: " + name)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
        .setContentIntent(pendingIntent); // Uncomment this line to set an intent for the notification action

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
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