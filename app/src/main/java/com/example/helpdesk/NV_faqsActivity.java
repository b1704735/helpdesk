package com.example.helpdesk;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NV_faqsActivity extends AppCompatActivity {
    TextView question,answer;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String key_question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nv_faqs);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        question = (TextView)findViewById(R.id.txtquestion);
        answer = (TextView)findViewById(R.id.txtanswer);

        Intent intent = getIntent();
        key_question = intent.getStringExtra("key1");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("faqs").child(key_question);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getKey().equals("question")){
                        question.setText(data.getValue().toString());
                    }
                    if(data.getKey().equals("answer")){
                        answer.setText(data.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
