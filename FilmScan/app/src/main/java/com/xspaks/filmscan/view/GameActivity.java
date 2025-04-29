package com.xspaks.filmscan.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.xspaks.filmscan.GoogleVision;
import com.xspaks.filmscan.R;
import com.xspaks.filmscan.database.PhotoDatabase;

public class GameActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap; // To hold the captured image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        PhotoDatabase db = new PhotoDatabase();

        Button boutonScan = findViewById(R.id.buttonScan);
        boutonScan.setOnClickListener(v -> {
            // Launch the camera to take a picture
            Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Toast.makeText(GameActivity.this, "Impossible d'ouvrir la caméra", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Get the image bitmap from the intent
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            GoogleVision.request(imageBitmap);
        }
    }
}

