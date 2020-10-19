package com.example.helpdesk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class NVActivity extends AppCompatActivity {

    private TextView tennv;
    String xinchao = "Xin chào ";
    String khoa;
    ListView lvContact;
    String TAG="FIREBASE";
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button btnDX, btnthemproblems;
    ListView lvContact1;
    ArrayAdapter<String> adapter1;
    DatabaseReference myRef1;
    ListView lvContact2;
    ArrayAdapter<String> adapter2;
    DatabaseReference myRef2;
    String []key_question = new String[1000];
    String []key_problem = new String[1000];
    int question_i = 0;
    int problem_i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nv);
        tennv = (TextView) findViewById(R.id.txtnhanvien);
        btnDX = (Button) findViewById(R.id.btnlogout);
        btnthemproblems = (Button) findViewById(R.id.addproblems);
        Intent intent = getIntent();
        khoa = intent.getStringExtra("key2");
        lvContact=findViewById(R.id.lvContact);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("users").child(khoa);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data2: dataSnapshot.getChildren()){
                    if(data2.getKey().toString().equals("name")){
                        tennv.setText(xinchao+data2.getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        btnDX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NVActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //--------------------------------------------------------------------------------------

        adapter1=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        lvContact1=findViewById(R.id.lvContact1);
        lvContact1.setAdapter(adapter1);
        myRef1 = database.getReference().child("faqs");
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter1.clear();
                for (DataSnapshot data: dataSnapshot.getChildren())
                {
                    String key=data.getKey();
                    myRef1 = database.getReference().child("faqs").child(key);
                    key_question[question_i]=key;
                    question_i+=1;
                    myRef1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data2: dataSnapshot.getChildren()){
                                if(data2.getKey().toString().equals("question")){
                                    adapter1.add(data2.getValue().toString());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        }
                    });

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });


        lvContact1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NVActivity.this, NV_faqsActivity.class);
                intent.putExtra("key1",key_question[i]);
                startActivity(intent);

            }
        });

        //------------------------------------------------------------------------------------------

        adapter2=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        lvContact2=findViewById(R.id.lvContact2);
        lvContact2.setAdapter(adapter2);
        myRef2 = database.getReference().child("problems");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter2.clear();
                for (DataSnapshot data: dataSnapshot.getChildren())
                {
                    final String key=data.getKey();

                    myRef2 = database.getReference().child("problems").child(key);
                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data1: dataSnapshot.getChildren()){
                                if(data1.getKey().toString().equals("user_create") && data1.getValue().toString().equals(khoa)){
                                    key_problem[problem_i]=key;
                                    problem_i+=1;
                                    for(DataSnapshot data2: dataSnapshot.getChildren()){
                                        if(data2.getKey().toString().equals("content")){
                                            adapter2.add(data2.getValue().toString());
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        }
                    });

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        lvContact2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NVActivity.this, NV_problemsActivity.class);
                intent.putExtra("key1",key_problem[i]);
                intent.putExtra("key2",khoa);
                startActivity(intent);
            }
        });

        btnthemproblems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NVActivity.this, NewproblemsActivity.class);
                intent.putExtra("key1",khoa);
                startActivity(intent);
            }
        });

    }
}
