package com.codeinfinity.homescopebusiness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 1;
    EditText name, number, email;

    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    TextView editPic;
    CardView save;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    String currentUserId = user.getUid();

    DatabaseReference propertiesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name = findViewById(R.id.nameProfileEdit);
        number = findViewById(R.id.numberProfileEdit);
        email = findViewById(R.id.emailProfileEdit);
        editPic = findViewById(R.id.changeImage);

        save =findViewById(R.id.saveCard);

        propertiesRef = FirebaseDatabase.getInstance().getReference("sellerDetails");


        save.setOnClickListener(v -> {
            // Upload data to Firebase
            uploadImageToFirebase();
        });

        editPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
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

            ImageView imageView = findViewById(R.id.profile_picEdit); // Update the ID to match your layout file
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



                                    // Upload the property details to Firebase Realtime Database
                                    DatabaseReference userUploads = FirebaseDatabase.getInstance().getReference("userUploads").child(currentUserId).push();
                                   // userUploads.setValue(property);

                                    DatabaseReference propertyRef = propertiesRef.push();
                                  //  propertyRef.setValue(property)
                                          //  .addOnSuccessListener(aVoid -> {
                                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("sellers").child(currentUserId);
                                                // userRef.child("worth").setValue(priceEditText.getText().toString());



                                                // Property details uploaded successfully
                                                Toast.makeText(EditProfileActivity.this, "Property added successfully", Toast.LENGTH_SHORT).show();
                                                // Update the listed value in the user's profile data


                                                finish(); // Close the activity
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle property upload failure
                                                Toast.makeText(EditProfileActivity.this, "Failed to add property", Toast.LENGTH_SHORT).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure to retrieve download URL
                                });


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

    private String generateFilename() {
        return "image_" + System.currentTimeMillis() + ".jpg";
    }

}