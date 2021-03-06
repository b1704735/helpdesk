package com.example.helpdesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDataActivity extends AppCompatActivity {

    private EditText mail,name,position,contacts;
    private Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddata);

        contacts = (EditText) findViewById(R.id.edtcontacts);
        mail = (EditText) findViewById(R.id.edtmail);
        name = (EditText) findViewById(R.id.edtname);
        position = (EditText) findViewById(R.id.edtposition);

        add = (Button) findViewById(R.id.btnadd);

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                try{
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //Kết nối tới node có tên là users (node này do ta định nghĩa trong CSDL Firebase)
                    DatabaseReference myRef = database.getReference("users");

                    myRef.child(contacts.getText().toString()).child("E-mail").setValue(mail.getText().toString());
                    myRef.child(contacts.getText().toString()).child("name").setValue(name.getText().toString());
                    myRef.child(contacts.getText().toString()).child("position").setValue(position.getText().toString());

                    Intent intent = new Intent(AddDataActivity.this, DataActivity.class);
                    startActivity(intent);
                }catch (Exception ex){
                    finish();
                }
            }
        });


    }
}
