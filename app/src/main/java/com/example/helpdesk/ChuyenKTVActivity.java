package com.example.helpdesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChuyenKTVActivity extends AppCompatActivity {

    String key_work, key_ktv;
    Spinner snktv;
    Button btnhuy, btncapnhat;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef5, myRef6;
    ArrayAdapter<String> adapter;
    String []key_use = new String[1000];
    int use_i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuyenktv);

        Intent intent = getIntent();
        key_work = intent.getStringExtra("key1");
        key_ktv = intent.getStringExtra("key2");

        snktv = (Spinner) findViewById(R.id.snktv);

        btnhuy = (Button) findViewById(R.id.btnhuy);
        btncapnhat = (Button) findViewById(R.id.btnok);

        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        snktv.setAdapter(adapter);
        myRef5 = database.getReference().child("users");
        myRef5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                use_i = 0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    final String key = data.getKey();

                        myRef6 = database.getReference().child("users").child(key);
                        myRef6.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot data1: dataSnapshot.getChildren()){
                                    if(data1.getKey().equals("position") && data1.getValue().toString().equals("technician")){
                                        key_use[use_i] = key;
                                        use_i+=1;
                                        for(DataSnapshot data2: dataSnapshot.getChildren()){
                                            if(data2.getKey().equals("name")){
                                                adapter.add(data2.getValue().toString());
                                            }
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btncapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference myRef7 = database.getReference("works");
                myRef7.child(key_work).child("user_fix").setValue(key_use[snktv.getSelectedItemPosition()]);
                myRef7.child(key_work).child("status").setValue("0");

                Toast.makeText(ChuyenKTVActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

                finish();
            }
        });



    }
}
