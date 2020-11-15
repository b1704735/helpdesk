package com.example.helpdesk;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QL_faqsActivity extends AppCompatActivity {
    TextView question,answer;
    FirebaseDatabase database;
    DatabaseReference myRef, myRef1, myRef2;
    String key_question, khoa;
    Boolean have_faq = false;
    Button btnsua, btnxoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_faqs);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        btnsua = (Button) findViewById(R.id.btnedit);
        btnxoa = (Button) findViewById(R.id.btnhuy);
        question = (TextView)findViewById(R.id.txtquestion);
        answer = (TextView)findViewById(R.id.txtanswer);

        Intent intent = getIntent();
        key_question = intent.getStringExtra("key1");
        khoa = intent.getStringExtra("key2");

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

        myRef1 = database.getReference().child("works");

        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot data: dataSnapshot.getChildren()){
                    String key = data.getKey();

                    myRef2 = database.getReference().child("works").child(key);

                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data1: dataSnapshot.getChildren()){
                                if(data1.getKey().equals("faq") && data1.getValue().toString().equals(key_question)){
                                    have_faq = true;

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!have_faq){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //Kết nối tới node có tên là contacts (node này do ta định nghĩa trong CSDL Firebase)
                    DatabaseReference myRef = database.getReference("faqs");
                    myRef.child(key_question).removeValue();
                    Toast.makeText(QL_faqsActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(QL_faqsActivity.this, "Không thể xóa FAQ đang được sử dụng.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QL_faqsActivity.this, QL_editfaqsActivity.class);
                intent.putExtra("key1",key_question);
                startActivity(intent);
                finish();
            }
        });
    }
}
