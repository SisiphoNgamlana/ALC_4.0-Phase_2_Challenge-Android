package com.sisipho.ngamlana.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class DealActivity extends AppCompatActivity {

    private static final int PICTURE_RESULT = 123;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText textTitle;
    EditText textDescription;
    EditText textPrice;
    Button buttonImage;
    ImageView imageView;
    private TravelDeal travelDeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);

        firebaseDatabase = FireBaseUtil.firebaseDatabase;
        databaseReference = FireBaseUtil.databaseReference;

        textTitle = findViewById(R.id.txt_title);
        textDescription = findViewById(R.id.txt_description);
        textPrice = findViewById(R.id.txt_price);
        buttonImage = findViewById(R.id.button_image);
        imageView = findViewById(R.id.imageView_deal_image);
        Intent intent = getIntent();
        TravelDeal travelDeal = (TravelDeal) intent.getSerializableExtra("Deal");
        if (travelDeal == null) {
            travelDeal = new TravelDeal();
        }
        this.travelDeal = travelDeal;
        textTitle.setText(travelDeal.getTitle());
        textDescription.setText(travelDeal.getDescription());
        textPrice.setText(travelDeal.getPrice());
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "Insert Picture"), PICTURE_RESULT);
            }
        });
        showImage(travelDeal.getImageUrl());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this, "Deal saved", Toast.LENGTH_LONG).show();
                clean();
                backToListActivity();
                return true;
            case R.id.delete_menu:
                deleteTravelDeal();
                Toast.makeText(this, "Travel Deal Deleted", Toast.LENGTH_LONG).show();
                backToListActivity();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void clean() {
        textTitle.setText("");
        textDescription.setText("");
        textPrice.setText("");
        textTitle.requestFocus();
    }

    private void saveDeal() {
        travelDeal.setTitle(textTitle.getText().toString());
        travelDeal.setDescription(textDescription.getText().toString());
        travelDeal.setPrice(textPrice.getText().toString());
        if (travelDeal.getId() == null) {
            databaseReference.push().setValue(travelDeal);
        } else {
            databaseReference.child(travelDeal.getId()).setValue(travelDeal);
        }
    }

    private void deleteTravelDeal() {
        if (travelDeal == null) {
            Toast.makeText(this, "Please save the deal before deleting", Toast.LENGTH_LONG).show();
            return;
        }
        databaseReference.child(travelDeal.getId()).removeValue();
        if (travelDeal.getImageName() != null && !travelDeal.getImageName().isEmpty()) {
            StorageReference pictureReference = FireBaseUtil.firebaseStorage.getReference().child(travelDeal.getImageName());
            pictureReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getBaseContext(), "Deleted Image Successfully", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Delete Image", e.getMessage());
                }
            });
        }
    }

    private void backToListActivity() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        if (FireBaseUtil.isAdmin) {
            menu.findItem(R.id.delete_menu).setVisible(true);
            menu.findItem(R.id.save_menu).setVisible(true);
            enableEditTexts(true);
            findViewById(R.id.button_image).setEnabled(true);
        } else {
            menu.findItem(R.id.delete_menu).setVisible(false);
            menu.findItem(R.id.save_menu).setVisible(false);
            enableEditTexts(false);
            findViewById(R.id.button_image).setEnabled(false);
        }
        return true;
    }

    private void enableEditTexts(boolean isEnabled) {
        textTitle.setEnabled(isEnabled);
        textDescription.setEnabled(isEnabled);
        textPrice.setEnabled(isEnabled);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            StorageReference reference = FireBaseUtil.storageReference.child(imageUri.getLastPathSegment());
            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getBaseContext(), "Image Uploaded", Toast.LENGTH_LONG).show();
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    String imageUrl = urlTask.getResult().toString();
                    String pictureName = taskSnapshot.getStorage().getPath();
                    travelDeal.setImageUrl(imageUrl);
                    travelDeal.setImageName(pictureName);
                    showImage(imageUrl);

                }
            });
        }
    }

    private void showImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(this)
                    .load(imageUrl)
                    .resize(width, width * 2 / 3)
                    .centerCrop()
                    .into(imageView);

        }
    }
}
