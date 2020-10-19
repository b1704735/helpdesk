package com.example.helpdesk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NV_problemsActivity extends AppCompatActivity {

    TextView txtproblem,txtstate,txtimagename;
    ImageView imgproblem;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String key_problem,key_user;
    String image_name = "";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Button btnxoa, btnsua;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nv_problems);

        txtproblem = (TextView) findViewById(R.id.txtproblem);
        txtstate = (TextView) findViewById(R.id.txtstate);
        imgproblem = (ImageView) findViewById(R.id.imgproblem);
        txtimagename = (TextView) findViewById(R.id.txtimagename);
        btnsua = (Button) findViewById(R.id.btnedit);
        btnxoa = (Button) findViewById(R.id.btnDelete);
        btnxoa.setEnabled(false);
        btnsua.setEnabled(false);

        Intent intent = getIntent();
        key_problem = intent.getStringExtra("key1");
        key_user = intent.getStringExtra("key2");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("problems").child(key_problem);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getKey().equals("content")){
                        txtproblem.setText(data.getValue().toString());
                    }
                    if(data.getKey().equals("status")){
                        txtstate.setText(data.getValue().toString());
                        if(data.getValue().toString().equals("0")){
                            btnxoa.setEnabled(true);
                            btnsua.setEnabled(true);
                        }
                    }
                    if(data.getKey().equals("image_name")){
                        txtimagename.setText(data.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("problems");
                myRef.child(key_problem).removeValue();

                Toast.makeText(NV_problemsActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(NV_problemsActivity.this, NVActivity.class);
                intent1.putExtra("key2",key_user);
                startActivity(intent1);
                finish();


            }
        });

        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
