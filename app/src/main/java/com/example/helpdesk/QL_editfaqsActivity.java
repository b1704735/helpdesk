package com.example.helpdesk;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QL_editfaqsActivity extends AppCompatActivity {

    EditText edtquestion, edtanswer;
    Button btncapnhat;
    String key_faq;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_editfaqs);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        edtquestion = (EditText) findViewById(R.id.edtquestion);
        edtanswer = (EditText) findViewById(R.id.edtanswer);
        btncapnhat = (Button) findViewById(R.id.btnedit);

        Intent intent = getIntent();
        key_faq = intent.getStringExtra("key1");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("faqs").child(key_faq);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getKey().equals("question")){
                        edtquestion.setText(data.getValue().toString());
                    }
                    if(data.getKey().equals("answer")){
                        edtanswer.setText(data.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btncapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("faqs");

                myRef.child(key_faq).child("question").setValue(edtquestion.getText().toString());
                myRef.child(key_faq).child("answer").setValue(edtanswer.getText().toString());

                Toast.makeText(QL_editfaqsActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(QL_editfaqsActivity.this, QL_faqsActivity.class);
                intent.putExtra("key1",key_faq);
                startActivity(intent);
                finish();
            }
        });

    }
}
