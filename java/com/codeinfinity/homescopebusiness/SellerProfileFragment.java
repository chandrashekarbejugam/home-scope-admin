package com.codeinfinity.homescopebusiness;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellerProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerProfileFragment extends Fragment {


    TextView nameProfile, emailProfile, numberProfile;

    CardView logout;

    ImageView profile;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    String uid = user.getUid();



    String email = user.getEmail();

    String image;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SellerProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerProfileFragment newInstance(String param1, String param2) {
        SellerProfileFragment fragment = new SellerProfileFragment();
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
        View root =  inflater.inflate(R.layout.fragment_seller_profile, container, false);

        nameProfile = root.findViewById(R.id.userNameProfile);
        emailProfile = root.findViewById(R.id.emailProfile);
        numberProfile = root.findViewById(R.id.numberProfile);
        profile = root.findViewById(R.id.profile_pic);
        logout =root.findViewById(R.id.logoutCard);
        TextView edit = root.findViewById(R.id.editprofile);


        emailProfile.setText(email);

        DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference("sellerDetails").child(uid);
        sellerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("name").getValue(String.class);
                    String number = snapshot.child("number").getValue(String.class);
                    String profile = snapshot.child("image").getValue(String.class);

                    nameProfile.setText(name);
                    //numberProfile.setText(name);
                    //image = profile;
                    // Toast.makeText(getActivity(), nameS, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });

        Glide.with(getActivity())
                .load("https://todayhoroscopeinhindi.com/wp-content/uploads/2023/04/Rashi-Khanna-at-Srinivasa-Kalyanam-Success-Meet-_8_.jpg")
                .into(profile);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });



        return root;
    }
}