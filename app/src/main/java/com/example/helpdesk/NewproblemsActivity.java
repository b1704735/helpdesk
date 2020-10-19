package com.example.helpdesk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class NewproblemsActivity<uploadTask> extends AppCompatActivity {

    ImageView imagecam,imageview;
    int REQUEST_CODE_IMAGE = 1;
    Button btnupimage;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String name_imamge,url_image,khoa;
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newproblems);

        imagecam = (ImageView) findViewById(R.id.imacam);
        imageview = (ImageView) findViewById(R.id.imgview);
        btnupimage = (Button) findViewById(R.id.btnupimage);

        Intent intent = getIntent();
        khoa = intent.getStringExtra("key1");

        imagecam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });

        btnupimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                name_imamge = "image"+calendar.getTimeInMillis()+ ".png";
                final StorageReference mountainsRef = storageRef.child(name_imamge);

                // Get the data from an ImageView as bytes
                imageview.setDrawingCacheEnabled(true);
                imageview.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                mountainsRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                url_image = uri.toString();
                                Toast.makeText(NewproblemsActivity.this, "Đã up ảnh thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(NewproblemsActivity.this, NewproblemsActivity2.class);
                                intent.putExtra("key1",name_imamge);
                                intent.putExtra("key2",url_image);
                                intent.putExtra("key3",khoa);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });

            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
