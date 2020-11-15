package com.example.helpdesk;

// Cái giao diện chi tiết của các công việc cần giải quyết của KTV

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Detail_work_name_Activity extends AppCompatActivity
{
    private Button btnSolution;
    private Button btnFail;
    private TextView txt;
    private TextView txtUser_create;
    private ImageView image_url;
    private Button btnAccept;
    private Button btnDeny;
    private Button btnproblem;

    private TextView txtQuestion;
    private TextView txtAnswer;

    String key_problem, key_user;
    FirebaseDatabase database_problems = FirebaseDatabase.getInstance();
    DatabaseReference myRef, myRef1, myRef2, myRef3;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_work_name);

        txt = (TextView) findViewById(R.id.txt);
        btnAccept = (Button)findViewById(R.id.btnAccept);
        btnDeny = (Button) findViewById(R.id.btnDeny);

        final TextView txtWork_name = (TextView) findViewById(R.id.txtWork_name);
        final TextView txtStatus = (TextView)findViewById(R.id.txtStatus);
        final TextView txtDeadline = (TextView) findViewById(R.id.txtDeadline);
        txtUser_create = (TextView) findViewById(R.id.txtUser_create);
        image_url = (ImageView)findViewById(R.id.image_url);
        btnSolution = (Button) findViewById(R.id.btnSolution);
        btnFail = (Button)findViewById(R.id.btnFail);

        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        txtAnswer = (TextView) findViewById(R.id.txtAnswer);

        Bundle bundle = getIntent().getExtras();
        final String title = bundle.getString("key_works"); // Lấy key của sự cố đã chọn
        key_user = bundle.getString("key_user");
        //Title là key của sự cố đã chọn

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
                                    txtStatus.setText("Chờ phản hồi");
                                    btnSolution.setVisibility(View.INVISIBLE);
                                    btnFail.setVisibility(View.INVISIBLE);
                                    txt.setText("Ấn 'CHẤP NHẬN' để nhận công việc, 'TỪ CHỐI' nếu bạn không thể hoàn thành");
                                }
                                else if (snapshot2.getValue().toString().equals("1"))
                                {
                                    txtStatus.setText("Đang điều tra");
                                    btnAccept.setVisibility(View.INVISIBLE);
                                    btnDeny.setVisibility(View.INVISIBLE);
                                    txt.setText("Giải quyết công việc");
                                }
                                else if (snapshot2.getValue().toString().equals("2"))
                                {
                                    txtStatus.setText("Không thể hoàn thành");
                                    btnSolution.setVisibility(View.INVISIBLE);
                                    btnFail.setVisibility(View.INVISIBLE);
                                    btnAccept.setVisibility(View.INVISIBLE);
                                    btnDeny.setVisibility(View.INVISIBLE);
                                    txt.setText("Công việc không thể hoàn thành");
                                }
                                else {
                                    txtStatus.setText("Đã hoàn thành");
                                    btnSolution.setVisibility(View.INVISIBLE);
                                    btnFail.setVisibility(View.INVISIBLE);
                                    btnAccept.setVisibility(View.INVISIBLE);
                                    btnDeny.setVisibility(View.INVISIBLE);
                                    txt.setText("Công việc đã hoàn thành, cảm ơn bạn");
                                }
                            }
                            if (snapshot2.getKey().equals("problem")) //Nếu quét được cột problem của bảng WORKS thì lấy giá trị problem
                            {
                                // Giá trị problem ở trên sẽ dùng để tra cứu bảng PROBLEM
                                // Chạy thêm Firebase để tìm giá trị problem
                                final DatabaseReference databaseProblems = FirebaseDatabase.getInstance().getReference().child("problems").child(snapshot2.getValue().toString());
                                databaseProblems.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (final DataSnapshot snapshot3 : dataSnapshot.getChildren())
                                        {
                                            if (snapshot3.getKey().equals("user_create")) // Tra cứu cột user_create của bảng USER để lấy người tạo
                                            {
                                                final DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference().child("users").child(snapshot3.getValue().toString());
                                                databaseUser.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for (final DataSnapshot snapshot4 : dataSnapshot.getChildren()) {
                                                            if (snapshot4.getKey().equals("name")){
                                                                txtUser_create.setText(snapshot4.getValue().toString());
                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                            if (snapshot3.getKey().equals("image_url"))
                                                try{
                                                    Picasso.with(Detail_work_name_Activity.this).load(snapshot3.getValue().toString()).into(image_url);
                                                }catch (Exception E){

                                                }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            if (snapshot2.getKey().equals("faq"))
                            {
                                DatabaseReference databaseFAQs = FirebaseDatabase.getInstance().getReference().child("faqs").child(snapshot2.getValue().toString());
                                databaseFAQs.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot3 : dataSnapshot.getChildren())
                                        {
                                            if (snapshot3.getKey().equals("question"))
                                            {
                                                txtQuestion.setText(snapshot3.getValue().toString());
                                            }

                                            if (snapshot3.getKey().equals("answer"))
                                            {
                                                txtAnswer.setText(snapshot3.getValue().toString());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        btnSolution.setOnClickListener(new View.OnClickListener() { // Sự kiện nút tạo Giải pháp khi nhấn tạo giai pháp
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail_work_name_Activity.this,KTV_makeSolutionActivity.class);
                Bundle bundle1 = new Bundle();
                Bundle bundle2 = new Bundle();
                bundle1.putString("key_solution",title);
                bundle2.putString("key_problem",key_problem);
                intent.putExtras(bundle1);
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });


        btnFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child(title).child("status").setValue("2");
                Toast.makeText(Detail_work_name_Activity.this,"Công việc được chuyển về quản lý",Toast.LENGTH_SHORT).show();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child(title).child("status").setValue("1");
                Toast.makeText(Detail_work_name_Activity.this,"Bạn đã chấp nhận giải quyết",Toast.LENGTH_SHORT).show();
                btnSolution.setVisibility(View.VISIBLE);
                btnFail.setVisibility(View.VISIBLE);
            }
        });

        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child(title).child("status").setValue("2");
                Toast.makeText(Detail_work_name_Activity.this,"Công việc được chuyển về quản lý",Toast.LENGTH_SHORT).show();
            }
        });

        //--------------------------------------------------load key problem----------------------------------------
        myRef1 = database_problems.getReference().child("works").child(title);
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getKey().equals("problem")){
                        key_problem = data.getValue().toString();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//------------------------------------chi tiết sự cố------------------------------------------------------
        btnproblem = (Button)findViewById(R.id.btnproblem1);
        btnproblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail_work_name_Activity.this, KTV_problemActivity.class);
                intent.putExtra("key1",key_problem);
                intent.putExtra("key2",key_user);
                startActivity(intent);



                //Toast.makeText(Detail_work_name_Activity.this, key_problem+"\n"+key_user, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
