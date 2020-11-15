package com.example.helpdesk;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class QL_newworksActivity extends AppCompatActivity {

    String TAG="FIREBASE", key_problem, key_solution = "", key_user = "", key_faq = "", khoa;
    Spinner lvuser_fix, lvfaq;
    EditText edtwork_name, edtdeadline;
    Button btnreport;
    ArrayAdapter<String> adapter1, adapter2, adapter3;
    DatabaseReference myRef1, myRef2, myRef3;
    FirebaseDatabase database;

    String []key_faqs = new String[1000];
    String []key_users = new String[1000];

    int users_i = 0;
    int faqs_i = 0;

protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ql_newworks);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

    edtwork_name = (EditText) findViewById(R.id.edtwork_name);
    edtdeadline = (EditText) findViewById(R.id.edtdeadline);
    btnreport = (Button) findViewById(R.id.btnreport);
    database = FirebaseDatabase.getInstance();
    Intent intent = getIntent();
    key_problem = intent.getStringExtra("key1");
    khoa = intent.getStringExtra("key2");
/*
    //-------------------------danh sách solution------------------------------------
    adapter1=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
    lvsolution = (ListView) findViewById(R.id.lvsolution);
    lvsolution.setAdapter(adapter1);
    myRef1 = database.getReference().child("solutions");
    myRef1.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            adapter1.clear();
            for (DataSnapshot data: dataSnapshot.getChildren())
            {
                String key=data.getKey();
                myRef1 = database.getReference().child("solutions").child(key);
                key_solutions[solutions_i]=key;
                solutions_i+=1;
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


    lvsolution.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            myRef1 = database.getReference().child("solutions").child(key_solutions[i]);
            key_solution = key_solution.concat(key_solutions[i]);
            myRef1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        if(data.getKey().toString().equals("content")){
                            txtsolution.setText("Bạn chọn giải pháp: "+data.getValue().toString());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    });

 */

    //-----------------------------------------danh sách user_fix-----------------

    adapter2=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
    lvuser_fix = (Spinner) findViewById(R.id.lvuser_fix);
    lvuser_fix.setAdapter(adapter2);
    myRef2 = database.getReference().child("users");
    myRef2.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            adapter2.clear();
            users_i = 0;
            for (DataSnapshot data: dataSnapshot.getChildren())
            {
                final String key=data.getKey();
                myRef2 = database.getReference().child("users").child(key);
                myRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data2: dataSnapshot.getChildren()){
                            if(data2.getKey().equals("position") && data2.getValue().toString().equals("technician")){
                                key_users[users_i]=key;
                                users_i+=1;
                                for(DataSnapshot data3: dataSnapshot.getChildren()){
                                    if(data3.getKey().toString().equals("name")){
                                        adapter2.add(data3.getValue().toString());
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

/*
    lvuser_fix.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            myRef2 = database.getReference().child("users").child(key_users[i]);
            key_user = key_user.concat(key_users[i]);
            myRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        if(data.getKey().toString().equals("name")){
                            txtuser_fix.setText("Bạn chọn nhân viên: "+data.getValue().toString());

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    });

 */

    //---------------------------------------danh sách faqs---------------------------

    adapter3=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
    lvfaq = (Spinner) findViewById(R.id.lvfaq);
    lvfaq.setAdapter(adapter3);
    myRef3 = database.getReference().child("faqs");
    myRef3.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            adapter3.clear();
            faqs_i = 0;
            for (DataSnapshot data: dataSnapshot.getChildren())
            {
                String key=data.getKey();
                myRef3 = database.getReference().child("faqs").child(key);
                key_faqs[faqs_i]=key;
                faqs_i+=1;
                myRef3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data2: dataSnapshot.getChildren()){
                            if(data2.getKey().toString().equals("question")){
                                adapter3.add(data2.getValue().toString());
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

/*
    lvfaq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            myRef3 = database.getReference().child("faqs").child(key_faqs[i]);
            key_faq = key_faq.concat(key_faqs[i]);
            myRef3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        if(data.getKey().toString().equals("question")){
                            txtfaq.setText("Bạn chọn faq: "+data.getValue().toString());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    });

 */

    //--------------------------------------------click report------------------------------------

    btnreport.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("works");

                // Creating new user node, which returns the unique key value
                // new user node would be /users/$userid/
                String prId = mDatabase.push().getKey();

                QL_newworksActivity.work wo = new QL_newworksActivity.work(key_problem, "", edtwork_name.getText().toString(), key_users[lvuser_fix.getSelectedItemPosition()], edtdeadline.getText().toString(), key_faqs[lvfaq.getSelectedItemPosition()], "0" );
                // pushing user to 'users' node using the userId
                mDatabase.child(prId).setValue(wo);

                Toast.makeText(QL_newworksActivity.this, "Successful!", Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent(QL_newworksActivity.this, QLActivity.class);
                intent1.putExtra("key2",khoa);
                startActivity(intent1);
                finish();

            }catch (Exception e){
                Toast.makeText(QL_newworksActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    });

}


    public static class work {
        public String problem;
        public String solution;
        public String work_name;
        public String user_fix;
        public String deadline;
        public String faq;
        public String status;

        public  work (){

        }

        public work(String problem, String solution, String work_name, String user_fix, String deadline, String faq, String status){

            this.problem=problem;
            this.solution=solution;
            this.work_name=work_name;
            this.user_fix=user_fix;
            this.deadline=deadline;
            this.faq=faq;
            this.status=status;
        }


    }
}

