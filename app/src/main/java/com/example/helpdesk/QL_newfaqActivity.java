package com.example.helpdesk;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QL_newfaqActivity extends AppCompatActivity {

    private EditText edtquestion, edtanswer;
    private Button add;
    String khoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_newfaq);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        edtanswer = (EditText) findViewById(R.id.edtanswer);
        edtquestion = (EditText) findViewById(R.id.edtquestion);

        Intent intent = getIntent();
        khoa = intent.getStringExtra("key1");

        add = (Button) findViewById(R.id.btnadd);

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                try {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("faqs");

                    // Creating new user node, which returns the unique key value
                    // new user node would be /users/$userid/
                    String prId = mDatabase.push().getKey();

                    // creating user object
                    QL_newfaqActivity.faq fa = new QL_newfaqActivity.faq(edtquestion.getText().toString(), edtanswer.getText().toString());
                    // pushing user to 'users' node using the userId
                    mDatabase.child(prId).setValue(fa);

                    Toast.makeText(QL_newfaqActivity.this, "Successful!", Toast.LENGTH_SHORT).show();

                    Intent intent1 = new Intent(QL_newfaqActivity.this, QLActivity.class);
                    intent1.putExtra("key2",khoa);
                    startActivity(intent1);
                    finish();

                }catch (Exception e){
                    Toast.makeText(QL_newfaqActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public static class faq {
        public String question;
        public String answer;


        public  faq (){

        }

        public faq(String question, String answer){
            this.answer = answer;
            this.question = question;

        }
    }
}
