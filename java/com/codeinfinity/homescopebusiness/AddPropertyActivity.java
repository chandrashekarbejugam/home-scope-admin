package com.codeinfinity.homescopebusiness;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPropertyActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 1;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    String currentUserId = user.getUid();
    String spinnerValue;

    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private EditText nameEditText;
    private EditText priceEditText;
    private EditText descriptionEditText;
    private EditText addressEditText;
    private EditText areaEditText;
    private EditText bedsEditText;
    private EditText livingEditText;
    private EditText bathEditText, facilityEditText;

    private DatabaseReference propertiesRef;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);


        nameEditText = findViewById(R.id.flatName);
        priceEditText = findViewById(R.id.flatPrice);
        descriptionEditText = findViewById(R.id.flatDescription);
        addressEditText = findViewById(R.id.flatAddress);
        areaEditText = findViewById(R.id.flatArea);
        bedsEditText = findViewById(R.id.flatBeds);
        livingEditText = findViewById(R.id.flatLiving);
        bathEditText = findViewById(R.id.flatBathrooms);
        facilityEditText = findViewById(R.id.flatFacliities);




        propertiesRef = FirebaseDatabase.getInstance().getReference("properties");



        Button addButton = findViewById(R.id.uploadButton);
        addButton.setOnClickListener(v -> {
            // Upload data to Firebase
            uploadImageToFirebase();
        });

        Button selectImageButton = findViewById(R.id.chooseBtn);
        selectImageButton.setOnClickListener(v -> {
            // Open the gallery picker to select an image
            openGallery();
        });



    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    // Handle the selected image result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            ImageView imageView = findViewById(R.id.imageName); // Update the ID to match your layout file
            imageView.setImageURI(imageUri);
            // Handle the image URI, upload it to Firebase
        }
    }

    // Method to upload image to Firebase Storage
    // Method to upload image to Firebase Storage and property details to Realtime Database
    private void uploadImageToFirebase() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String filename = generateFilename();// Generate a unique filename for the image
        String currentDate = dateFormat.format(date);

        // Get the image URI from the ImageView or any other source you are using to display the selected image
        ImageView imageView = findViewById(R.id.imageName);
        Uri imageUri = getImageUriFromImageView(imageView);

        if (imageUri != null) {
            StorageReference imageRef = storageRef.child("images/" + filename);
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully, retrieve the download URL
                        imageRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    // Now you have the image URL, along with other details
                                    // Create a Property object and upload all the details to Firebase Realtime Database or Firestore
                                    Property property = new Property();
                                    property.setName(nameEditText.getText().toString());
                                    property.setPrice(priceEditText.getText().toString());
                                    property.setDescription(descriptionEditText.getText().toString());
                                    property.setAddress(addressEditText.getText().toString());
                                    property.setAreaSqft(areaEditText.getText().toString());
                                    property.setBedrooms(bedsEditText.getText().toString());
                                    property.setLivingRooms(livingEditText.getText().toString());
                                    property.setBathrooms(bathEditText.getText().toString());
                                    property.setFacilities(facilityEditText.getText().toString());
                                    property.setDescription(descriptionEditText.getText().toString());
                                    property.setPropertyType(spinnerValue);
                                    property.setDate(currentDate);
                                    property.setUid(currentUserId);
                                    // Set other property fields
                                    property.setImage(imageUrl);

                                    // Upload the property details to Firebase Realtime Database
                                    DatabaseReference userUploads = FirebaseDatabase.getInstance().getReference("userUploads").child(currentUserId).push();
                                    userUploads.setValue(property);

                                    DatabaseReference propertyRef = propertiesRef.push();
                                    propertyRef.setValue(property)
                                            .addOnSuccessListener(aVoid -> {
                                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("sellers").child(currentUserId);
                                               // userRef.child("worth").setValue(priceEditText.getText().toString());


                                                userRef.child("worth").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            // Retrieve the current listed value
                                                            int currentWorth = dataSnapshot.getValue(Integer.class);

                                                            // Increase the listed value by one


                                                            //int finalWorth = Integer.parseInt(currentWorth);
                                                            int price = Integer.parseInt(priceEditText.getText().toString());
                                                            int updatedWorth = currentWorth + price;

                                                            // Update the listed value in the user's profile data
                                                            userRef.child("worth").setValue(updatedWorth)
                                                                    .addOnSuccessListener(aVoid -> {
                                                                        // Listed value updated successfully

                                                                        finish(); // Close the activity
                                                                    })
                                                                    .addOnFailureListener(e -> {
                                                                        // Handle failure to update the listed value
                                                                         });
                                                        }else {
                                                            int price = Integer.parseInt(priceEditText.getText().toString());
                                                            userRef.child("worth").setValue(price);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        // Handle any errors
                                                    }
                                                });



                                                userRef.child("listed").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            // Retrieve the current listed value
                                                            int currentListed = dataSnapshot.getValue(Integer.class);

                                                            // Increase the listed value by one
                                                            int updatedListed = currentListed + 1;

                                                            // Update the listed value in the user's profile data
                                                            userRef.child("listed").setValue(updatedListed)
                                                                    .addOnSuccessListener(aVoid -> {
                                                                        // Listed value updated successfully
                                                                        Toast.makeText(AddPropertyActivity.this, "Property added successfully", Toast.LENGTH_SHORT).show();


                                                                        finish(); // Close the activity
                                                                    })
                                                                    .addOnFailureListener(e -> {
                                                                        // Handle failure to update the listed value
                                                                        Toast.makeText(AddPropertyActivity.this, "Failed to update listed value", Toast.LENGTH_SHORT).show();
                                                                    });
                                                        }else {
                                                            userRef.child("listed").setValue(1);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        // Handle any errors
                                                    }
                                                });
                                                // Property details uploaded successfully
                                                Toast.makeText(AddPropertyActivity.this, "Property added successfully", Toast.LENGTH_SHORT).show();
                                                // Update the listed value in the user's profile data


                                                finish(); // Close the activity
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle property upload failure
                                                Toast.makeText(AddPropertyActivity.this, "Failed to add property", Toast.LENGTH_SHORT).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure to retrieve download URL
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Handle image upload failure
                    });
        } else {
            // Handle case when no image is selected
        }
    }

    // Method to get the URI of the image from an ImageView
    private Uri getImageUriFromImageView(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Image.jpg");
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri imageUri = FileProvider.getUriForFile(this, "com.codeinfinity.homescopebusiness.fileprovider", imageFile);
        return imageUri;
    }


    // Method to generate a unique filename for the image
    private String generateFilename() {
        return "image_" + System.currentTimeMillis() + ".jpg";
    }

}