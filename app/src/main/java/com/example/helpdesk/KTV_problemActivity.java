package com.example.helpdesk;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class KTV_problemActivity extends AppCompatActivity {

    TextView txtproblem,txtstate,txtuser_create, txtEmail;
    ImageView imgproblem, image;
    FirebaseDatabase database;
    DatabaseReference myRef3, myRef4, myRef5;
    DatabaseReference myRef, myRef2;
    String key_problem, user_create = "", user_reply = null, khoa, value, name_reply = "", TAG="FIREBASE";
    //khoa là key người tạo chat
    Button btnduyet, btnwork, btnimage,btnworks;
    ListView lvreply;
    ArrayAdapter<String> adapter3;
    EditText edtreply, edttmp;
    Button btnreply;
    String key_reply = "";
    int STT_reply = 0;
    String day_time = "";
    int REQUEST_CODE_IMAGE = 1, SELECT_PICTURE = 1;

    String []key_replies = new String[1000];
    int replies_i = 0;

    String name_imamge, prId;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    Boolean have_image = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ktv_problem);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        image = (ImageView) findViewById(R.id.image);
        btnimage = (Button) findViewById(R.id.btnimage);
        txtproblem = (TextView) findViewById(R.id.txtquestion);
        txtstate = (TextView) findViewById(R.id.txtstate);
        imgproblem = (ImageView) findViewById(R.id.imgproblem);
        txtuser_create = (TextView) findViewById(R.id.txtuser_create);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        edtreply = (EditText) findViewById(R.id.edtreply);
        btnreply = (Button) findViewById(R.id.btnreply);


        Intent intent = getIntent();
        key_problem = intent.getStringExtra("key1");
        khoa = intent.getStringExtra("key2");

//        Toast.makeText(this, key_problem+"\n"+khoa, Toast.LENGTH_SHORT).show();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("problems").child(key_problem);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getKey().equals("content")){
                        txtproblem.setText(data.getValue().toString());
                    }
                    if(data.getKey().equals("image_url") && !data.getValue().toString().equals("")){
                        Picasso.with(KTV_problemActivity.this).load(data.getValue().toString()).into(imgproblem);
                    }
                    if(data.getKey().equals("status")){
                        if(data.getValue().toString().equals("0")){
                            txtstate.setText("Đã gửi");
                        }else if(data.getValue().toString().equals("1")){
                            txtstate.setText("Trạng thái: Đang điều tra");
                        }else if(data.getValue().toString().equals("2")){
                            txtstate.setText("Trạng thái: Chờ duyệt");
                        }else{
                            txtstate.setText("Trạng thái: Đã hoàn thành");
                        }
                    }
                    if(data.getKey().equals("user_create")){
                        user_create = user_create.concat(data.getValue().toString());
                        //txtuser_create.setText(user_create);
                        myRef2 = database.getReference().child("users").child(user_create);

                        myRef2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot data2: dataSnapshot.getChildren()){
                                    if(data2.getKey().equals("name")){
                                        txtuser_create.setText("Người tạo : "+data2.getValue().toString());
                                    }
                                    if(data2.getKey().equals("email")){
                                        txtEmail.setText("Emai : "+data2.getValue().toString());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                //do something
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //do something
            }
        });

        //-----------------------------------------Hiện reply----------------------------------

        adapter3=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        lvreply = (ListView) findViewById(R.id.lvreply);
        lvreply.setAdapter(adapter3);
        myRef3 = database.getReference().child("replies");
        myRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter3.clear();
                for (final DataSnapshot data: dataSnapshot.getChildren())
                {
                    final String key=data.getKey();
                    key_reply = key;
                    replies_i = 0;
                    myRef4 = database.getReference().child("replies").child(key);
                    myRef4.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data2: dataSnapshot.getChildren()){
                                if(data2.getKey().toString().equals("problem") && data2.getValue().toString().equals(key_problem)){

                                    key_replies[replies_i] = key;
                                    replies_i += 1;

                                    String value2 = "";
                                    value = value2;
                                    for(DataSnapshot data1: dataSnapshot.getChildren()){
                                        if(data1.getKey().toString().equals("image_url")){
                                            if(data1.getValue().toString().length()>10){
                                                value = value.concat(" [kèm ảnh]");
                                            }
                                        }
                                        if(data1.getKey().toString().equals("content")){
                                            value = value.concat(data1.getValue().toString());
                                        }
                                    }
                                    adapter3.add(value);
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

        lvreply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                myRef4 = database.getReference().child("replies").child(key_replies[i]);
                myRef4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            if(data.getKey().equals("user_create")){
                                Intent intent = new Intent(KTV_problemActivity.this, ChatActivity.class);
                                intent.putExtra("key1",key_replies[i]);
                                intent.putExtra("key2",data.getValue().toString());
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

//-------------------------------------------------------------------------------------
        btnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });

//------------------------------------------------------------------------------------------------------------------
        btnreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(have_image){
                    Calendar calendar = Calendar.getInstance();
                    name_imamge = "image"+calendar.getTimeInMillis()+ ".png";
                    final StorageReference mountainsRef = storageRef.child(name_imamge);

                    // Get the data from an ImageView as bytes
                    image.setDrawingCacheEnabled(true);
                    image.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();

                    mountainsRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url_image = uri.toString();
                                    try{
                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("replies");

                                        // Creating new user node, which returns the unique key value
                                        // new user node would be /users/$userid/
                                        prId = mDatabase.push().getKey();

                                        DateFormat df = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                                        String date = df.format(Calendar.getInstance().getTime());

                                        QL_problemsActivity.reply rl = new QL_problemsActivity.reply(edtreply.getText().toString(),url_image,key_problem,date,khoa);
                                        // pushing user to 'users' node using the userId
                                        mDatabase.child(prId).setValue(rl);
                                        Toast.makeText(KTV_problemActivity.this, "Đã gửi", Toast.LENGTH_SHORT).show();
                                        edtreply.setText("");

                                    }catch (Exception e){
                                        Toast.makeText(KTV_problemActivity.this, "Lỗi "+e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }else{
                    try{
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("replies");

                        // Creating new user node, which returns the unique key value
                        // new user node would be /users/$userid/
                        prId = mDatabase.push().getKey();

                        DateFormat df = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                        String date = df.format(Calendar.getInstance().getTime());

                        QL_problemsActivity.reply rl = new QL_problemsActivity.reply(edtreply.getText().toString(),"",key_problem,date,khoa);
                        // pushing user to 'users' node using the userId
                        mDatabase.child(prId).setValue(rl);
                        Toast.makeText(KTV_problemActivity.this, "Đã gửi", Toast.LENGTH_SHORT).show();
                        edtreply.setText("");


                    }catch (Exception e){
                        Toast.makeText(KTV_problemActivity.this, "Lỗi "+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                key_replies[replies_i] = prId;
                replies_i += 1;
                image.setImageResource(R.drawable.no_image);
                have_image = false;
            }
        });
    }

    public static class reply {
        public String content;
        public String image_url;
        public String problem;
        public String time;
        public String user_create;

        public  reply (){

        }

        public reply(String content, String image_url, String problem, String time, String user_create){

            this.problem=problem;
            this.content=content;
            this.image_url=image_url;
            this.time=time;
            this.user_create=user_create;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null || requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null ){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);
            have_image = true;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
