package com.example.helpdesk;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ChatActivity extends AppCompatActivity {

    String key_reply, key_user;
    TextView time, content, user;
    ImageView img;
    DatabaseReference myRef, myRef1;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        Intent intent = getIntent();
        key_reply = intent.getStringExtra("key1");
        key_user = intent.getStringExtra("key2");

        time = (TextView) findViewById(R.id.txttime);
        content = (TextView) findViewById(R.id.txtcontent);
        user = (TextView)findViewById(R.id.txtuser);
        img = (ImageView) findViewById(R.id.img);

        user.setText(key_user);
        content.setText(key_reply);

        database = FirebaseDatabase.getInstance();

        myRef = database.getReference().child("users").child(key_user);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getKey().equals("name")){
                        user.setText("Người gửi: "+data.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef1 = database.getReference().child("replies").child(key_reply);
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){

                    if(data.getKey().equals("content")){
                        content.setText("Tin nhắn: "+data.getValue().toString());
                    }

                    if(data.getKey().equals("time")){
                        time.setText("Thời gian: "+data.getValue().toString());
                    }

                    if(data.getKey().equals("image_url")) {
                        try {
                            Picasso.with(ChatActivity.this).load(data.getValue().toString()).into(img);
                        }catch (Exception e){
                            img.setImageResource(R.drawable.no_image);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
