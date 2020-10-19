package com.example.helpdesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewproblemsActivity2 extends AppCompatActivity {

    EditText edtcontent;
    String khoa,name_image,url_image;
    Button btnreport;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newproblems2);

        edtcontent = (EditText) findViewById(R.id.edtcontent);
        btnreport = (Button) findViewById(R.id.btnreport);

        Intent intent = getIntent();
        name_image = intent.getStringExtra("key1");
        url_image = intent.getStringExtra("key2");
        khoa = intent.getStringExtra("key3");

        btnreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("problems");

                    // Creating new user node, which returns the unique key value
                    // new user node would be /users/$userid/
                    String prId = mDatabase.push().getKey();

                    // creating user object
                    Problem pr = new Problem(khoa,edtcontent.getText().toString(),name_image,url_image,0);

                    // pushing user to 'users' node using the userId
                    mDatabase.child(prId).setValue(pr);

                    Toast.makeText(NewproblemsActivity2.this, "Successful!", Toast.LENGTH_SHORT).show();

                    Intent intent1 = new Intent(NewproblemsActivity2.this, NVActivity.class);
                    intent1.putExtra("key2",khoa);
                    startActivity(intent1);
                    finish();

                }catch (Exception e){
                    Toast.makeText(NewproblemsActivity2.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    public static class Problem {
        public String user_create;
        public String content;
        public String image_name;
        public String image_url;
        public int status;

        public  Problem (){

        }

        public Problem(String user_create, String content, String image_name, String image_url, int status){

            this.content=content;
            this.image_name=image_name;
            this.image_url=image_url;
            this.user_create=user_create;
            this.status=status;

        }
    }
}
