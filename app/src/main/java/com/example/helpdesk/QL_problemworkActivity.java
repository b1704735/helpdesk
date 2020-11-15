package com.example.helpdesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QL_problemworkActivity extends AppCompatActivity {

    String key_problem;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef, myRef1, myRef2;
    ListView lvworks;
    ArrayAdapter<String> adapter;
    String []key_works = new String[1000];
    int works_i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problemwork);

        Intent intent = getIntent();
        key_problem = intent.getStringExtra("key1");
        lvworks = (ListView) findViewById(R.id.lvworks);

        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        lvworks.setAdapter(adapter);
        myRef = database.getReference().child("works");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                works_i = 0;
                for(final DataSnapshot data: dataSnapshot.getChildren()){
                    final String key = data.getKey();
                    myRef = database.getReference().child("works").child(key);
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data: dataSnapshot.getChildren()){
                                if(data.getKey().equals("problem") && data.getValue().toString().equals(key_problem)){
                                    key_works[works_i] = key;
                                    works_i +=1;
                                    for(DataSnapshot data1: dataSnapshot.getChildren()){
                                        if(data1.getKey().equals("work_name")){
                                            adapter.add(data1.getValue().toString());
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

        lvworks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(QL_problemworkActivity.this, WorksActivity.class);
                intent.putExtra("key1",key_problem);
                intent.putExtra("key2",key_works[i]);
                startActivity(intent);
            }
        });

    }
}
