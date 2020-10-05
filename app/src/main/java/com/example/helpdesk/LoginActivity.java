package com.example.helpdesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    Button btnDangNhap;
    EditText txtTaiKhoan, txtMatKhau;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnDangNhap = (Button)findViewById(R.id.btnDangNhap);
        txtTaiKhoan = (EditText)findViewById(R.id.txtTaiKhoan);
        txtMatKhau = (EditText)findViewById(R.id.txtMatKhau);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtTaiKhoan.getText().toString().equals("admin") && txtMatKhau.getText().toString().equals("admin")){
                    Toast.makeText(getApplicationContext(),"Dung r",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Ngu qua!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}