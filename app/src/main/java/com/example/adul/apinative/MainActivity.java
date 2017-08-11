package com.example.adul.apinative;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    public static OkHttpClient okHttpClient;
    public static final String BASE_URL = "http://192.168.1.15/project_katalog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        okHttpClient = new OkHttpClient();

        Frag_listproduct frag = new Frag_listproduct();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_container, frag)
                .commit();

    }
}
