package com.codeinfinity.homescopebusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AgreeRejectActivity extends AppCompatActivity {

    TextView flatName, name, number;

    Button agree, disagree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_reject);

        flatName = findViewById(R.id.propertyName);
        name = findViewById(R.id.nameText);
        number = findViewById(R.id.numberText);

        agree = findViewById(R.id.agreeBtn);
        disagree = findViewById(R.id.disagreeBtn);

        String nameFlat = getIntent().getStringExtra("request_name");
        String nameUser = getIntent().getStringExtra("requested_name");
        String numberUser = getIntent().getStringExtra("request_number");

        String userUid = getIntent().getStringExtra("requested_uid");

        flatName.setText(nameFlat);
        name.setText(nameUser);
        number.setText(numberUser);

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference notifyRef = FirebaseDatabase.getInstance().getReference("userRequests/requests/").child(userUid).push();
                notifyRef.child("name").setValue(nameFlat);
                notifyRef.child("decision").setValue("agreed!");
            }
        });

        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference notifyRef = FirebaseDatabase.getInstance().getReference("userRequests/requests/").child(userUid).push();
                notifyRef.child("name").setValue(nameFlat);
                notifyRef.child("decision").setValue("disagreed!");
            }
        });


    }
}