package com.example.helpdesk;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class QLActivity extends AppCompatActivity {

    ListView lvproblems, lvfaqs;
    ArrayAdapter<String> adapter1, adapter2;
    DatabaseReference myRef,myRef1, myRef2, myRef3, myRef4;
    FirebaseDatabase database;
    String []key_problems = new String[1000];
    String []key_faqs = new String[1000];
    int problems_i = 0;
    int faqs_i = 0;
    String TAG="FIREBASE", khoa;
    TextView name,status;
    Button DX, btnproblems1, btnproblems2, btnproblems3, btnfaq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        database = FirebaseDatabase.getInstance();
        btnproblems1 = (Button) findViewById(R.id.btnproblems1);
        btnproblems2 = (Button) findViewById(R.id.btnproblems2);
        btnproblems3 = (Button) findViewById(R.id.btnproblems3);
        btnfaq = (Button) findViewById(R.id.btnfaq);
        btnproblems3.setEnabled(false);
        name = (TextView) findViewById(R.id.txtname);

        Intent intent = getIntent();
        khoa = intent.getStringExtra("key2");

        myRef = database.getReference().child("users").child(khoa);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data2: dataSnapshot.getChildren()){
                    if(data2.getKey().toString().equals("name")){
                        name.setText("Xin chào "+data2.getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        DX = (Button) findViewById(R.id.btnlogout);
        DX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QLActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnfaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QLActivity.this, QL_newfaqActivity.class);
                intent.putExtra("key1",khoa);
                startActivity(intent);
                finish();
            }
        });

//------------------------------------------------------------------------------------------
        adapter1=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        lvproblems=findViewById(R.id.lvproblems);
        lvproblems.setAdapter(adapter1);
        myRef1 = database.getReference().child("problems");
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter1.clear();
                problems_i = 0;
                for (DataSnapshot data: dataSnapshot.getChildren())
                {
                    String key=data.getKey();
                    myRef1 = database.getReference().child("problems").child(key);
                    key_problems[problems_i]=key;
                    problems_i+=1;
                    myRef1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data2: dataSnapshot.getChildren()){
                                if(data2.getKey().toString().equals("content")){
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


        lvproblems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(QLActivity.this, QL_problemsActivity.class);
                intent.putExtra("key1",key_problems[i]);
                intent.putExtra("key2",khoa);
                startActivity(intent);
                //Toast.makeText(QLActivity.this, "Vị trí "+i, Toast.LENGTH_SHORT).show();
            }
        });

        //------------------------------------------------------------------------------------------

        adapter2=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        lvfaqs=findViewById(R.id.lvfaqs);
        lvfaqs.setAdapter(adapter2);
        myRef2 = database.getReference().child("faqs");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter2.clear();
                for (DataSnapshot data: dataSnapshot.getChildren())
                {
                    final String key=data.getKey();
                    key_faqs[faqs_i] = key;
                    faqs_i +=1;
                    myRef2 = database.getReference().child("faqs").child(key);
                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data2: dataSnapshot.getChildren()){
                                if(data2.getKey().toString().equals("question")){
                                    adapter2.add(data2.getValue().toString());
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

        lvfaqs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(QLActivity.this, QL_faqsActivity.class);
                intent.putExtra("key1",key_faqs[i]);
                intent.putExtra("key2",khoa);
                startActivity(intent);

            }
        });

//--------------------------------------------------------------------------------------------------
//Hiện sự cố chưa được duyệt

        btnproblems1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnproblems1.setEnabled(false);
                btnproblems2.setEnabled(true);
                btnproblems3.setEnabled(true);

                myRef3 = database.getReference().child("problems");
                myRef3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adapter1.clear();
                        problems_i = 0;
                        for (DataSnapshot data: dataSnapshot.getChildren())
                        {
                            final String key=data.getKey();
                            myRef3 = database.getReference().child("problems").child(key);
                            myRef3.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot data2: dataSnapshot.getChildren()){
                                        if(data2.getKey().equals("status") && data2.getValue().toString().equals("0")){
                                            key_problems[problems_i]=key;
                                            problems_i+=1;
                                            for(DataSnapshot data3: dataSnapshot.getChildren()){
                                                if(data3.getKey().equals("content")){
                                                    adapter1.add(data3.getValue().toString());
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
            }
        });



//-------------------------------------------------------------------------------------------------
// Hiện sự cố đã được duyệt

        btnproblems2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnproblems1.setEnabled(true);
                btnproblems2.setEnabled(false);
                btnproblems3.setEnabled(true);

                myRef3 = database.getReference().child("problems");
                myRef3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adapter1.clear();
                        problems_i = 0;
                        for (DataSnapshot data: dataSnapshot.getChildren())
                        {
                            final String key=data.getKey();
                            myRef3 = database.getReference().child("problems").child(key);
                            myRef3.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot data2: dataSnapshot.getChildren()){
                                        if(     data2.getKey().equals("status") && data2.getValue().toString().equals("1") ||
                                                data2.getKey().equals("status") && data2.getValue().toString().equals("2") ||
                                                data2.getKey().equals("status") && data2.getValue().toString().equals("3")){
                                            key_problems[problems_i]=key;
                                            problems_i+=1;
                                            for(DataSnapshot data3: dataSnapshot.getChildren()){
                                                if(data3.getKey().equals("content")){
                                                    adapter1.add(data3.getValue().toString());
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
            }
        });


//-------------------------------------------------------------------------------------------------
// Hiện tất cả sự cố

        btnproblems3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnproblems1.setEnabled(true);
                btnproblems2.setEnabled(true);
                btnproblems3.setEnabled(false);

                myRef1 = database.getReference().child("problems");
                myRef1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adapter1.clear();
                        problems_i = 0;
                        for (DataSnapshot data: dataSnapshot.getChildren())
                        {
                            String key=data.getKey();
                            myRef1 = database.getReference().child("problems").child(key);
                            key_problems[problems_i]=key;
                            problems_i+=1;
                            myRef1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot data2: dataSnapshot.getChildren()){
                                        if(data2.getKey().toString().equals("content")){
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
            }
        });
    }
}
