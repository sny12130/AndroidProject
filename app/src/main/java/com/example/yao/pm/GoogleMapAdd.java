package com.example.yao.pm;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class GoogleMapAdd extends AppCompatActivity {

    Double locale = 0.0;

    Double latlng = 0.0;

    private Bitmap bitmap;
    //預覽照片的全域變數

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_googlemapadd);

        initial();

        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(OnMapReady);

        Intent intent = getIntent();

        Bundle bund = intent.getExtras();

        locale = Double.parseDouble(bund.getString(CDictionary.GOOGLE_LOCALE));
        //經度

        latlng = Double.parseDouble(bund.getString(CDictionary.GOOGLE_LATLNG));
        //緯度
    }

    private OnMapReadyCallback OnMapReady = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {

            LatLng marker = new LatLng(locale,latlng);

            googleMap.addMarker(new MarkerOptions().position(marker));

            googleMap.getUiSettings().setZoomControlsEnabled(true);

            googleMap.getUiSettings().setCompassEnabled(true);

            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(marker, 16) );
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case 0:
                //開啟手機相本
                try{
                    Uri uri = data.getData();
                    ContentResolver resolver = getContentResolver();
                    bitmap = MediaStore.Images.Media.getBitmap(resolver,uri);
                    imgGM圖片.setImageBitmap(bitmap);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            case 1:
                //開啟相機拍照
                try{
                    Bundle bundle = data.getExtras();
                    bitmap = (Bitmap) bundle.get("data");
                    imgGM圖片.setImageBitmap(bitmap);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
        }
    }

    private View.OnClickListener btnGM拍照_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent 開相機 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            final int 拍照 = 1;

            startActivityForResult(開相機,拍照);

        }
    };

    private View.OnClickListener btnGM新增預覽照片_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final String 圖片格式 = "image/*";

            final int 開啟手機相簿 = 0;

            Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);

            getAlbum.setType(圖片格式);

            startActivityForResult(getAlbum,開啟手機相簿);
        }
    };


    private View.OnClickListener btnGM傳送資料_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try{
                ByteArrayOutputStream bytesteam = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG,50,bytesteam);

                byte[] b = bytesteam.toByteArray();

                String multipart_form_data = "multipart/form-data";
                String twoHyphens = "--";
                String boundary = "****************fD4fH3gL0hK7aI6";
                //邊界
                String lineEnd =  "\r\n";

                try{
                    URL url = new URL("http://ec2-54-250-199-234.ap-northeast-1.compute.amazonaws.com/spot/uploadspotimage?accessid=10");
                    URLConnection con = url.openConnection();
                    HttpURLConnection httpUrlConnection = (HttpURLConnection) con;
                    httpUrlConnection.setDoOutput(true);
                    httpUrlConnection.setDoInput(true);
                    httpUrlConnection.setUseCaches(false);
                    httpUrlConnection.setRequestMethod("POST");
                    httpUrlConnection.setRequestProperty("Connection", "keep-alive");
                    httpUrlConnection.setRequestProperty("Content-Type", multipart_form_data + "; boundary=" + boundary);
                    httpUrlConnection.connect();
                    OutputStream outStrm = httpUrlConnection.getOutputStream();

                    String location = "5b265914ba40440f8fa7e6c85e5c1215";
                    StringBuilder split1 = new StringBuilder();
                    split1.append(twoHyphens + boundary + lineEnd);
                    split1.append("Content-Disposition: form-data;  name=\"LocationId\"" + lineEnd);
                    split1.append(lineEnd);
                    split1.append(location);
                    outStrm.write(split1.toString().getBytes());
                    outStrm.write(lineEnd.getBytes());
                    outStrm.write(twoHyphens.getBytes());
                    outStrm.write(boundary.getBytes());
                    outStrm.write(lineEnd.getBytes());

                    StringBuilder split = new StringBuilder();
                    split.append(twoHyphens + boundary + lineEnd);
                    split.append("Content-Disposition: form-data; name=\"File\"; filename=\"hihihihihi.jpg\"" + lineEnd);
                    split.append("Content-Type: "+".jpg" + lineEnd);
                    split.append(lineEnd);
                    outStrm.write(split.toString().getBytes());
                    outStrm.write(b);
                    outStrm.write(lineEnd.getBytes());
                    outStrm.write(twoHyphens.getBytes());
                    outStrm.write(boundary.getBytes());
                    outStrm.write(twoHyphens.getBytes());



                    StringBuilder html = new StringBuilder();
                    InputStream streamIn = con.getInputStream();
                    //接收回來的訊息
                    BufferedReader r = new BufferedReader(new InputStreamReader(streamIn));
                    String line;
                    while ((line = r.readLine()) != null) {
                        html.append(line);
                    }
                    Log.d("Test1",html.toString());

                }catch(Exception e)
                {
                }

                edit景點名稱.setText("");

                edit景點描述.setText("");

            }catch (Exception e)
            {
            }
            Toast.makeText(GoogleMapAdd.this,"您以傳送成功",Toast.LENGTH_LONG).show();
        }
    };




    public void initial() {

        btnGM拍照 = findViewById(R.id.btnGM拍照);
        btnGM拍照.setOnClickListener(btnGM拍照_click);

        btnGM新增預覽照片 = findViewById(R.id.btnGM新增照片);
        btnGM新增預覽照片.setOnClickListener(btnGM新增預覽照片_click);

        imgGM圖片 = findViewById(R.id.imgGM圖片);

        btnGM傳送資料 = findViewById(R.id.btnGM傳送資料);
        btnGM傳送資料.setOnClickListener(btnGM傳送資料_click);

        edit景點描述 = findViewById(R.id.edit景點名稱);

        edit景點名稱 = findViewById(R.id.edit景點描述);

    }

    Button    btnGM傳送資料;
    Button    btnGM新增預覽照片;
    Button    btnGM拍照;
    ImageView imgGM圖片;
    EditText edit景點描述;
    EditText edit景點名稱;

}
