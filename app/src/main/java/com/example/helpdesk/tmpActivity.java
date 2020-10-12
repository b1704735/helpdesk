package com.example.helpdesk;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class tmpActivity extends AppCompatActivity {
    private Button btnDangNhap;
    private EditText txtTaiKhoan, txtMatKhau;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    String TAG="FIREBASE";
    String key;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp);
        auth = FirebaseAuth.getInstance();

        //bug in here
         // hàm kiểm tra trạng thái đăng nhập
        /*if (auth.getCurrentUser() != null) {
            startActivity(new Intent(tmpActivity.this, DataActivity.class));
            finish();
        } */

        setContentView(R.layout.activity_login);

        txtTaiKhoan = (EditText) findViewById(R.id.txtTaiKhoan);
        txtMatKhau = (EditText) findViewById(R.id.txtMatKhau);
        btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();


        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = txtTaiKhoan.getText().toString();
                final String password = txtMatKhau.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(tmpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                txtMatKhau.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(tmpActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference myRef = database.getReference().child("users");
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (final DataSnapshot data: dataSnapshot.getChildren())
                                    {
                                        key=data.getKey();
                                        //String value=data.getValue().toString();
                                        //adapter.add(key+" "+value);

                                        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef2 = database2.getReference().child("users").child(key).getRef();
                                        myRef2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                                for (final DataSnapshot data2: dataSnapshot2.getChildren()){
                                                    if(data2.getKey().equals("email")){
                                                        if(data2.getValue().toString().equals(txtTaiKhoan.getText().toString())){
                                                            for (DataSnapshot data3: dataSnapshot2.getChildren()){
                                                                if(data3.getKey().equals("position")){
                                                                    if(data3.getValue().toString().equals("staff")){
                                                                        Intent intent = new Intent(tmpActivity.this, NVActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                        Toast.makeText(tmpActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else if(data3.getValue().toString().equals("manager")){
                                                                        Intent intent = new Intent(tmpActivity.this, QLActivity.class);
                                                                        startActivity(intent);
                                                                        finish();

                                                                        Toast.makeText(tmpActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else {
                                                                        Intent intent = new Intent(tmpActivity.this, KTVActivity.class);
                                                                        startActivity(intent);
                                                                        finish();

                                                                        Toast.makeText(tmpActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
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
                    }
                });

            }
        });
    }
}