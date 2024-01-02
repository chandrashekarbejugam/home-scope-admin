package com.codeinfinity.homescopebusiness;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
 * Use the {@link StatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    TextView listed, worth;

    private static final String CHANNEL_ID = "notification_channel";
    private static final int NOTIFICATION_ID = 1;

    DatabaseReference propertiesRef;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();


    String uid = user.getUid();

    private RequestAdapter adapter;

    private RecyclerView recyclerView;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatFragment newInstance(String param1, String param2) {
        StatFragment fragment = new StatFragment();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_stat, container, false);

        recyclerView = rootView.findViewById(R.id.recycelerRequests);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RequestAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        propertiesRef = FirebaseDatabase.getInstance().getReference("sellers").child(uid).child("requests");

        retrieveDataFromFirebase();


        return rootView;

    }

    private void retrieveDataFromFirebase() {
        propertiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Requests> propertyList = new ArrayList<>();
                for (DataSnapshot propertySnapshot : dataSnapshot.getChildren()) {
                    Requests requests = propertySnapshot.getValue(Requests.class);
                    propertyList.add(requests);
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

