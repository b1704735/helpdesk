package com.example.helpdesk;

// Cái giao diện chi tiết của các công việc cần giải quyết của KTV

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

public class Detail_work_name_Activity extends AppCompatActivity
{
    private Button btnSolution;
    private Button btnFail;
    private TextView txt;
    private Button btnBack;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_work_name);

        txt = (TextView) findViewById(R.id.txt);
        btnBack = (Button)findViewById(R.id.btnBack);

        final TextView txtWork_name = (TextView) findViewById(R.id.txtWork_name);
        final TextView txtStatus = (TextView)findViewById(R.id.txtStatus);
        final TextView txtDeadline = (TextView) findViewById(R.id.txtDeadline);
        btnSolution = (Button) findViewById(R.id.btnSolution);
        btnFail = (Button)findViewById(R.id.btnFail);

        Bundle bundle = getIntent().getExtras();
        final String title = bundle.getString("key1"); // Lấy key của sự cố đã chọn

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("works");  // Sự kiện TextView Tình trạng và sự cố
        database.child(title).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    for (DataSnapshot snapshot1 :dataSnapshot.getChildren())
                    {
                        for (DataSnapshot snapshot2 : dataSnapshot.getChildren()) {
                            if (snapshot.getKey().equals("work_name") && snapshot1.getKey().equals("deadline") && snapshot2.getKey().equals("status")) {
                                txtWork_name.setText(snapshot.getValue().toString());
                                txtDeadline.setText(snapshot1.getValue().toString());

                                if (snapshot2.getValue().toString().equals("0")) {
                                    txtStatus.setText("Đã gửi");
                                }
                                else if (snapshot2.getValue().toString().equals("1"))
                                {
                                    txtStatus.setText("Đang điều tra");
                                }
                                else if (snapshot2.getValue().toString().equals("2"))
                                {
                                    txtStatus.setText("Không thể hoàn thành");
                                }
                                else {
                                    txtStatus.setText("Đã hoàn thành");
                                    btnSolution.setVisibility(View.INVISIBLE);
                                    btnFail.setVisibility(View.INVISIBLE);
                                    btnBack.setVisibility(View.VISIBLE);
                                    txt.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        btnSolution.setOnClickListener(new View.OnClickListener() { // Sự kiện nút tạo Giải pháp
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail_work_name_Activity.this,KTV_makeSolutionActivity.class);
                Bundle bundle1 = new Bundle();

                bundle1.putString("key2",title);
                intent.putExtras(bundle1);

                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail_work_name_Activity.this,KTVActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child(title).child("status").setValue("2");
                Toast.makeText(Detail_work_name_Activity.this,"Vậy cũng sửa không được. ĐỒ VÔ DỤNG",Toast.LENGTH_SHORT).show();

            }
        });
    }
}
