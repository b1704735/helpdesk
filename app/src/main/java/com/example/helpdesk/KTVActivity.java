package com.example.helpdesk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class KTVActivity extends AppCompatActivity {
    private Button btnDangXuat;
    private ListView lvWorks;
    private Button btnMain;
    String []key_works = new String [100];
    int work_i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ktv);

        btnDangXuat = (Button) findViewById(R.id.btnDangXuat);
        lvWorks = findViewById(R.id.lvWorks);
        btnMain = findViewById(R.id.btnMain);

        final ArrayList<Works> list = new ArrayList<>(); //Mảng nhận các giá trị
        final WorksAdapter adapter = new WorksAdapter(this,R.layout.item_work_name,list);
        lvWorks.setAdapter(adapter);

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("works");
        database.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                list.clear();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                   String key = snapshot.getKey();

                    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("works").child(key); // Truy cập đến bảng "Works" trên firebases
                    database.addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            // 3 dòng for để có thể lần lượt cùng truy cập đến 3 dòng dữ liệu trên firebase
                            for(DataSnapshot snapshot1: dataSnapshot.getChildren())
                            {

                                // Dòng for truy cập đến cột "Deadline" thuộc bảng "Works"
                                for (DataSnapshot snapshot2 : dataSnapshot.getChildren())
                                {
                                    // Dòng for truy cập đến Cột: Status thuộc bảng "Works"
                                    for (DataSnapshot snapshot3 : dataSnapshot.getChildren()) {
                                        if (snapshot1.getKey().equals("work_name") && snapshot2.getKey().equals("deadline") && snapshot3.getKey().equals("status") )
                                        {
                                            String key = dataSnapshot.getKey();
                                            key_works[work_i] = key; //Lấy KEY của bảng WORDS để dùng cho sau này.
                                            work_i+=1;

                                            if (snapshot3.getValue().toString().equals("0"))
                                            {
                                                list.add(new Works(snapshot1.getValue().toString(), snapshot2.getValue().toString(), R.drawable.red));
                                            }
                                            else if (snapshot3.getValue().toString().equals("1"))
                                            {
                                                list.add(new Works(snapshot1.getValue().toString(), snapshot2.getValue().toString(), R.drawable.vang));
                                            }
                                            else if (snapshot3.getValue().toString().equals("2"))
                                            {
                                                list.add(new Works(snapshot1.getValue().toString(), snapshot2.getValue().toString(), R.drawable.status2));
                                            }
                                            else if (snapshot3.getValue().toString().equals("3"))
                                            {
                                                list.add(new Works(snapshot1.getValue().toString(), snapshot2.getValue().toString(), R.drawable.xanh));
                                            }
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {
                            Log.w("FIREBASE", "loadPost:onCancelled", databaseError.toException());
                        }
                    })         ;
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("FIREBASE", "loadPost:onCancelled", databaseError.toException());
            }
        });

        //Sự kiện nút đăng xuất
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (KTVActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KTVActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //Sự kiện kích chọn giá trị Item của danh sách công việc
        lvWorks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("works");
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(KTVActivity.this, Detail_work_name_Activity.class);
                Bundle bundle = new Bundle(); // Hàm truyền dữ liệu qua Activity khác

                bundle.putString("key1",key_works[i]);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }
}

