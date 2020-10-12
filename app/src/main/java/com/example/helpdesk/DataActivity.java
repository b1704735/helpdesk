package com.example.helpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DataActivity extends AppCompatActivity {

    ListView lvContact;
    ArrayAdapter<String> adapter;
    String TAG="FIREBASE";
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        lvContact=findViewById(R.id.lvContact);
        lvContact.setAdapter(adapter);
//lấy đối tượng FirebaseDatabase
        database = FirebaseDatabase.getInstance();
//Kết nối tới node có tên là users (node này do ta định nghĩa trong CSDL Firebase)
        String keyyy = "-MJM-3XxCfpUgfSyxl9K";
        myRef = database.getReference().child("users");
//truy suất và lắng nghe sự thay đổi dữ liệu
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
//vòng lặp để lấy dữ liệu khi có sự thay đổi trên Firebase
                for (DataSnapshot data: dataSnapshot.getChildren())
                {
                    String key=data.getKey();
                    myRef = database.getReference().child("users").child(key);

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data2: dataSnapshot.getChildren()){
                                if(data2.getKey().toString().equals("position")){
                                    adapter.add(data2.getValue().toString());
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