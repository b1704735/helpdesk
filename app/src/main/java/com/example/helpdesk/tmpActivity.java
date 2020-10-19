package com.example.helpdesk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class tmpActivity extends AppCompatActivity {

    ImageView img;
    String url;
    Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp);

        img = (ImageView) findViewById(R.id.imgview);
        btn = (Button) findViewById(R.id.button);

        url = "https://2sao.vietnamnetjsc.vn/images/2020/03/14/12/30/hot-girl-1.jpg";

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadImageInternet().execute("https://2sao.vietnamnetjsc.vn/images/2020/03/14/12/30/hot-girl-1.jpg");
            }
        });

    }

    private  class LoadImageInternet extends AsyncTask<String, Void, Bitmap>{
        Bitmap bitmapHinh = null;
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                InputStream inputStream = url.openConnection().getInputStream();
                bitmapHinh = BitmapFactory.decodeStream(inputStream);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmapHinh;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            img.setImageBitmap(bitmap);
        }
    }
}