package com.example.helpdesk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
    private TextView txtXinChao;
    private String key_DN;
    DatabaseReference databaseXinChao;
    boolean [] works_i; // Dùng để lấy Số của vị trí Tên người dùng ở trên Firebase


    String []key_works = new String [100];
    int work_i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ktv);

        btnDangXuat = (Button) findViewById(R.id.btnDangXuat);
        lvWorks = findViewById(R.id.lvWorks);
        btnMain = findViewById(R.id.btnMain);

        final ArrayList<Works> list = new ArrayList<>(); //Mảng nhận các giá trị trong Work
        final WorksAdapter adapter = new WorksAdapter(this,R.layout.item_work_name,list);
        lvWorks.setAdapter(adapter);

        works_i = new boolean [20]; // Lấy vị trí trên Firebase;
        for (int i=0;i<works_i.length;i++)
        {
            works_i[i]=true;  //Khởi tạo giá trị mảng
        }

        txtXinChao = findViewById(R.id.txtXinChao);
        Intent intent = getIntent(); //Lấy Key truyền từ Login để hiện tên chỗ kế Đăng xuất
        key_DN = intent.getStringExtra("key2"); //Key users

        databaseXinChao = FirebaseDatabase.getInstance().getReference().child("users").child(key_DN); //Lấy chỗ node : "key_DN"
        databaseXinChao.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot data: dataSnapshot.getChildren())
                {
                    if (data.getKey().equals("name"))
                    {
                         txtXinChao.setText("Xin chào " + data.getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
            Log.w("FIREBASE", "loadPost:onCancelled", databaseError.toException());
        }
        });

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
                                    for (DataSnapshot snapshot3 : dataSnapshot.getChildren())
                                    {
                                        for (DataSnapshot snapshot4 :dataSnapshot.getChildren()) // Lấy những công việc ứng với key Users tương ứng
                                        {
                                            if (snapshot1.getKey().equals("work_name") && snapshot2.getKey().equals("deadline") && snapshot3.getKey().equals("status") && snapshot4.getKey().equals("user_fix")) {

                                                if (snapshot4.getValue().toString().equals(key_DN)) // Lấy những công việc ứng với key Users tương ứng
                                                {
                                                    String key = dataSnapshot.getKey();

                                                    // works_i [work_i] = true;

                                                    if (snapshot3.getValue().toString().equals("0")) // Chưa nhận công việc
                                                    {
                                                        list.add(new Works(snapshot1.getValue().toString(), snapshot2.getValue().toString(), R.drawable.red));
                                                        key_works[work_i] = key; //Lấy KEY của bảng WORDS để dùng cho sau này.
                                                        work_i += 1;
                                                    } else if (snapshot3.getValue().toString().equals("1")) // Đã nhận công việc, chờ hoàn thành
                                                    {
                                                        list.add(new Works(snapshot1.getValue().toString(), snapshot2.getValue().toString(), R.drawable.vang));
                                                        key_works[work_i] = key; //Lấy KEY của bảng WORDS để dùng cho sau này.
                                                        work_i += 1;
                                                    }
                                                    /*else if (snapshot3.getValue().toString().equals("2")) //Công việc: Không thể giải quyết được
                                                    {
                                                        list.add(new Works(snapshot1.getValue().toString(), snapshot2.getValue().toString(), R.drawable.status2));
                                                    }
                                             else if (snapshot3.getValue().toString().equals("3")) // Ra số 3 nghĩa là công việc đã hoàn thành. Không cần in ra
                                                {
                                                    list.add(new Works(snapshot1.getValue().toString(), snapshot2.getValue().toString(), R.drawable.xanh));
                                                }*/
                                                }
//                                                else Toast.makeText(KTVActivity.this,"Danh sách trống",Toast.LENGTH_SHORT).show();
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

        final DatabaseReference databaseWorks = FirebaseDatabase.getInstance().getReference().child("works");



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
                Bundle bundle1 = new Bundle();
                bundle.putString("key_works",key_works[i]); //key của works
                bundle1.putString("key_user",key_DN); // key user
                intent.putExtras(bundle);
                intent.putExtras(bundle1);
                startActivity(intent);

            }
        });
    }
}

