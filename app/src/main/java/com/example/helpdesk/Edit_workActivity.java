package com.example.helpdesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Edit_workActivity extends AppCompatActivity {

    String key_problem, key_work;
    EditText tvwork_name, tvtime, tvstatus, tvsolution, tvuser, tvquestion, tvanswer;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef, myRef1, myRef2, myRef3, myRef4, myRef5, myRef6;
    Spinner snsolution, snuser, snfaq;
    ArrayAdapter<String> adapter, adapter1, adapter2;
    Button btnhuy, btncapnhat;

    String []key_solution = new String[1000];
    int solution_i = 0;

    String []key_ktv = new String[1000];
    int ktv_i = 0;

    String []key_faq = new String[1000];
    int faq_i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editwork);

        Intent intent = getIntent();
        key_problem = intent.getStringExtra("key1");
        key_work = intent.getStringExtra("key2");
        //key_work = "-MKYZepc1dtqRthXmB4K";

        tvwork_name = (EditText) findViewById(R.id.tvwork_name);
        tvtime = (EditText) findViewById(R.id.tvtime);

        snsolution = (Spinner) findViewById(R.id.snsolution);
        snuser = (Spinner) findViewById(R.id.snuser);
        snfaq = (Spinner) findViewById(R.id.snfaq);

        btnhuy = (Button) findViewById(R.id.btnhuy);
        btncapnhat = (Button) findViewById(R.id.btnok);

        myRef = database.getReference().child("works").child(key_work);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getKey().equals("work_name")){
                        tvwork_name.setText(data.getValue().toString());
                    }
                    if(data.getKey().equals("deadline")){
                        tvtime.setText(data.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//----------------------------------------------DS solution------------------------------------------------------------
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        snsolution.setAdapter(adapter);
        myRef1 = database.getReference().child("solutions");
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                solution_i = 0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String key = data.getKey();
                    key_solution[solution_i] = key;
                    solution_i+=1;
                    myRef2 = database.getReference().child("solutions").child(key);
                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data1: dataSnapshot.getChildren()){
                                if(data1.getKey().equals("content")){
                                    adapter.add(data1.getValue().toString());
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
//-------------------------------------------------DS faq-----------------------------------------------------
        adapter1=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        snfaq.setAdapter(adapter1);
        myRef3 = database.getReference().child("faqs");
        myRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter1.clear();
                faq_i = 0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String key = data.getKey();
                    key_faq[faq_i] = key;
                    faq_i+=1;
                    myRef4 = database.getReference().child("faqs").child(key);
                    myRef4.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data1: dataSnapshot.getChildren()){
                                if(data1.getKey().equals("question")){
                                    adapter1.add(data1.getValue().toString());
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
//------------------------------------------------DS kỹ thuật viên-----------------------------------------------------
        adapter2=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        snuser.setAdapter(adapter2);
        myRef5 = database.getReference().child("users");
        myRef5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter2.clear();
                ktv_i = 0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    final String key = data.getKey();
                    myRef6 = database.getReference().child("users").child(key);
                    myRef6.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data1: dataSnapshot.getChildren()){
                                if(data1.getKey().equals("position") && data1.getValue().toString().equals("technician")){
                                    key_ktv[ktv_i] = key;
                                    ktv_i+=1;
                                    for(DataSnapshot data2: dataSnapshot.getChildren()){
                                        if(data2.getKey().equals("name")){
                                            adapter2.add(data2.getValue().toString());
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

        btncapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Edit_workActivity.this, "solution:"+key_solution[snsolution.getSelectedItemPosition()]+"\n user:"+key_ktv[snuser.getSelectedItemPosition()]+"\n faq"+key_faq[snfaq.getSelectedItemPosition()], Toast.LENGTH_SHORT).show();
                //FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef7 = database.getReference("works");

                myRef7.child(key_work).child("solution").setValue(key_solution[snsolution.getSelectedItemPosition()]);
                myRef7.child(key_work).child("work_name").setValue(tvwork_name.getText().toString());
                myRef7.child(key_work).child("user_fix").setValue(key_ktv[snuser.getSelectedItemPosition()]);
                myRef7.child(key_work).child("deadline").setValue(tvtime.getText().toString());
                myRef7.child(key_work).child("faq").setValue(key_faq[snfaq.getSelectedItemPosition()]);

                Toast.makeText(Edit_workActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //------------------------------------------vào chi tiết sự cố----------------------------------
    }
}
