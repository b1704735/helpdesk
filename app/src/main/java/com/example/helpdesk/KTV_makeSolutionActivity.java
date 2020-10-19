package com.example.helpdesk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;

public class KTV_makeSolutionActivity extends AppCompatActivity {

    private Spinner spinTime_fix;
    private TextView txtWork_name1;
    private EditText txtContent;
    private Button btnReport;
    private Button btnQuayDauLaBo;
    private String time_fix_text = new String(); // biến để lấy giá trị Spinner thành kiểu String
    final String []problem_text = {"0"};
    final String []user_fix_text = {"0"};
    FirebaseDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ktv_makesolution);

        txtWork_name1 = (TextView)findViewById(R.id.txtWork_name1);
        txtContent = (EditText)findViewById(R.id.txtContent);
        btnReport = (Button)findViewById(R.id.btnReport);
        btnQuayDauLaBo =(Button)findViewById(R.id.btnQuayDauLaBo);

        spinTime_fix = (Spinner) findViewById(R.id.spinTime_fix); //Định nghĩa Spinmer
        final ArrayList<String> ListTime_fix = new ArrayList<String>(); // Thêm giá trị vào Spinner time fix
        ListTime_fix.add("1");
        ListTime_fix.add("2");
        ListTime_fix.add("3");
        ListTime_fix.add("4");
        ListTime_fix.add("5");
        ListTime_fix.add("10");
        ListTime_fix.add("15");
        ListTime_fix.add("20");
        ListTime_fix.add("25");
        ListTime_fix.add("30");
        ListTime_fix.add("45");
        ListTime_fix.add("60");
        ListTime_fix.add("90");
        ListTime_fix.add("120");
        ListTime_fix.add("150");
        ListTime_fix.add("180");
        ListTime_fix.add("240");
        ListTime_fix.add("12 hours");
        ListTime_fix.add("1 day");
        ListTime_fix.add("2 days");
        ListTime_fix.add(">2 days");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,ListTime_fix); // Tạo Adapter để có thể thấy Spinner
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTime_fix.setAdapter(arrayAdapter);

        // Lập trình sự kiện khi chọn đối tượng item của Spinner thì hiện lên biết chọn thằng nào
        spinTime_fix.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(KTV_makeSolutionActivity.this, "Hoàn thành trong "+ ListTime_fix.get(i) + " phút", Toast.LENGTH_SHORT).show();
                time_fix_text= spinTime_fix.getSelectedItem().toString(); // Lấy giá trị trong Spinner thành String
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Bundle bundle = getIntent().getExtras(); //Lấy dự liệu từ Activity truyền từ Intent
        final String title = bundle.getString("key2");

        final DatabaseReference databaseWorks = FirebaseDatabase.getInstance().getReference().child("works"); // Lấy dữ liệu từ bảng work trên Firebase)
        databaseWorks.child(title).addValueEventListener(new ValueEventListener() { // Lấy dự liều từ works mà có từ khóa là title
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    if(snapshot.getKey().equals("work_name"))
                    {
                        txtWork_name1.setText(snapshot.getValue().toString()); //Truyền dữ liệu cho thằng Work_name ở KTV_Make Solution
                    }
                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren())
                    {
                        for (DataSnapshot snapshot2 : dataSnapshot.getChildren())
                        {
                            if (snapshot1.getKey().equals("problem") && snapshot2.getKey().equals("user_fix"))
                            {
                                problem_text[0] = snapshot1.getValue().toString();
                                user_fix_text[0]=snapshot2.getValue().toString();
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        database = FirebaseDatabase.getInstance();

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtContent.getText().toString().equals(null))
                {
                    Toast.makeText(KTV_makeSolutionActivity.this, "Mày chưa ghi cách giải quyết mà báo cáo cái quần gì", Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseReference myRef = database.getReference().child("solutions");
                    Solutions solutions = new Solutions(user_fix_text[0], problem_text[0], title, txtContent.getText().toString(), time_fix_text);
                    myRef.push().setValue(solutions);

                    databaseWorks.child(title).child("status").setValue("0");
                    Toast.makeText(KTV_makeSolutionActivity.this, "Đã thêm thành công", Toast.LENGTH_SHORT).show();
                    btnReport.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnQuayDauLaBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(KTV_makeSolutionActivity.this,Detail_work_name_Activity.class);
                startActivity(intent1);
                finish();
            }
        });

    }
}
