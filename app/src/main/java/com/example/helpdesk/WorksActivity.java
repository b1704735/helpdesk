package com.example.helpdesk;

import android.content.Intent;
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

public class WorksActivity extends AppCompatActivity {
    String key_problem, key_work, key_ktv;
    TextView tvwork_name, tvtime, tvstatus, tvsolution, tvuser, tvquestion, tvanswer;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef, myRef1, myRef2, myRef3;
    Button btndelete, btnedit, btnchuyen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works);

        Intent intent = getIntent();
        key_problem = intent.getStringExtra("key1");
        key_work = intent.getStringExtra("key2");

        tvwork_name = (TextView) findViewById(R.id.tvwork_name);
        tvtime = (TextView) findViewById(R.id.tvtime);
        tvstatus = (TextView) findViewById(R.id.tvstatus);
        tvsolution = (TextView) findViewById(R.id.tvsolution);
        tvuser = (TextView) findViewById(R.id.tvuser);
        tvquestion = (TextView) findViewById(R.id.tvquestion);
        tvanswer = (TextView) findViewById(R.id.tvanswer);

        btndelete = (Button) findViewById(R.id.btnhuy);
        btndelete.setEnabled(false);
        btnedit = (Button) findViewById(R.id.btnedit);
        btnedit.setEnabled(false);
        btnchuyen = (Button) findViewById(R.id.btnok);
        btnchuyen.setEnabled(false);

        myRef = database.getReference().child("works").child(key_work);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getKey().equals("work_name")){
                        tvwork_name.setText(data.getValue().toString());
                    }
                    if(data.getKey().equals("deadline")){
                        tvtime.setText("Hạn cuối: "+data.getValue().toString());
                    }
                    if(data.getKey().equals("status")){
                        if(data.getValue().toString().equals("0")){
                            btndelete.setEnabled(true);
                            btnedit.setEnabled(true);
                            tvstatus.setText("Trạng thái: đã gửi");
                        }
                        if(data.getValue().toString().equals("1")){
                            tvstatus.setText("Trạng thái: Đang điều tra");
                        }
                        if(data.getValue().toString().equals("3")){
                            tvstatus.setText("Trạng thái: Đã hoàn thành");
                        }
                        if(data.getValue().toString().equals("2")){
                            btnchuyen.setEnabled(true);
                            tvstatus.setText("Trạng thái: Không thể hoàn thành");
                        }
                    }
                    if(data.getKey().equals("solution")){
                        myRef1 = database.getReference().child("solutions").child(data.getValue().toString());
                        myRef1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot data1: dataSnapshot.getChildren()){
                                    if(data1.getKey().equals("content")){
                                        tvsolution.setText("Solution: "+data1.getValue().toString());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    if(data.getKey().equals("user_fix")){
                        myRef2 = database.getReference().child("users").child(data.getValue().toString());
                        key_ktv = data.getValue().toString();
                        myRef2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot data1: dataSnapshot.getChildren()){
                                    if(data1.getKey().equals("name")){
                                        tvuser.setText("Người nhận: "+data1.getValue().toString());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    if(data.getKey().equals("faq")){
                        myRef3 = database.getReference().child("faqs").child(data.getValue().toString());
                        myRef3.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot data1: dataSnapshot.getChildren()){
                                    if(data1.getKey().equals("question")){
                                        tvquestion.setText("Câu hỏi: "+data1.getValue().toString());
                                    }
                                    if(data1.getKey().equals("answer")){
                                        tvanswer.setText("Câu trả lời: "+data1.getValue().toString());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("works");
                myRef.child(key_work).removeValue();
                Toast.makeText(WorksActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                finish();
    /*            Intent intent = new Intent(WorksActivity.this, QL_problemworkActivity.class);
                intent.putExtra("key1",key_problem);
                startActivity(intent);

     */
            }
        });

        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorksActivity.this, Edit_workActivity.class);
                intent.putExtra("key1",key_problem);
                intent.putExtra("key2",key_work);
                startActivity(intent);
            }
        });

        btnchuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorksActivity.this, ChuyenKTVActivity.class);
                intent.putExtra("key1",key_work);
                intent.putExtra("key2",key_ktv);
                startActivity(intent);
            }
        });
    }
}
