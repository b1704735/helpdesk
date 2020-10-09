package com.example.helpdesk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class tmpActivity extends AppCompatActivity {
    private EditText mail,name,position,contacts;
    private Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp);

        contacts = (EditText) findViewById(R.id.edtcontacts);
        mail = (EditText) findViewById(R.id.edtmail);
        name = (EditText) findViewById(R.id.edtname);
        position = (EditText) findViewById(R.id.edtposition);
/*
        String cts = contacts.getText().toString();
        String email = mail.getText().toString();
        final String fullname = name.getText().toString();
        final String position1 = position.getText().toString();
*/
        add = (Button) findViewById(R.id.btnadd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //Toast.makeText(getApplicationContext(),contacts.getText().toString()+" "+mail.getText().toString()+" "+name.getText().toString()+" "+position.getText().toString(),Toast.LENGTH_LONG).show();

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //Kết nối tới node có tên là users (node này do ta định nghĩa trong CSDL Firebase)
                    DatabaseReference myRef = database.getReference("users");

                    myRef.child(contacts.getText().toString()).child("E-mail").setValue(mail.getText().toString());
                    myRef.child(contacts.getText().toString()).child("name").setValue(name.getText().toString());
                    myRef.child(contacts.getText().toString()).child("position").setValue(position.getText().toString());
                    Intent intent = new Intent(tmpActivity.this, DataActivity.class);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"loi",Toast.LENGTH_LONG).show();
                }

            }
        });



    }
}
