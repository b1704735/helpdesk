package com.example.helpdesk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class tmpActivity extends AppCompatActivity {


    FirebaseDatabase database_problems;
    DatabaseReference myRef1, myRef2, myRef3, myRef4, myRef5;
    String key_problem = "-MJq6Vz3IW76oRa7xD-W", value = "", TAG="FIREBASE";
    ListView lvreply;
    ArrayAdapter<String> adapter3;
    String []key_user = new String[1000];
    String []name = new String[1000];
    int key_i = 0;
    int name_i = 0;
    Button btn;
    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp);

        String title = "-MJM-yom0gUsj5HPHCE4";

        database_problems = FirebaseDatabase.getInstance();
        myRef1 = database_problems.getReference().child("works").child(title);
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getKey().equals("problem")){
                        key_problem = data.getValue().toString();
                        Toast.makeText(tmpActivity.this, key_problem, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}