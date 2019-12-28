package com.mihir.test;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;


import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    ExtendedFloatingActionButton extendedFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        extendedFAB = findViewById(R.id.addPhotosButton);
    }


    static final int REQUEST_IMAGE_OPEN = 1;

    public void addPhotos(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
        startActivityForResult(intent, REQUEST_IMAGE_OPEN);
    }

    private File file;
    private File sourceFile;
    private File destFile;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            // Do work with full size photo saved at fullPhotoUri
            System.out.println("Yayyy!");
            try {
                ParcelFileDescriptor parcelFileDescriptor =
                        getContentResolver().openFileDescriptor(imageUri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);


                System.out.println("This is our path: " + imageUri.getPath());
                String[] compressedImagePath = imageUri.getPath().split(":");
                FileOutputStream outFile = openFileOutput(compressedImagePath[1] + "_compressed.jpeg",
                        MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.JPEG, 0, outFile);
                System.out.println("Compressed!!!");
                outFile.close();

                parcelFileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}


