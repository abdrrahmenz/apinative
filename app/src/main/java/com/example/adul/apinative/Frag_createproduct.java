package com.example.adul.apinative;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by adul on 23/04/17.
 */

public class Frag_createproduct extends Fragment {

    EditText etName, etPrice, etDesc;
    Button btSave;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = container;

        View v = inflater.inflate(R.layout.frag_create,container,false);

        etName = (EditText) v.findViewById(R.id.edtName);
        etPrice = (EditText) v.findViewById(R.id.edtPrice);
        etDesc = (EditText) v.findViewById(R.id.edtDesc);
        btSave = (Button) v.findViewById(R.id.btSave);

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progress = ProgressDialog.show(getActivity(), "Loading","Please wait ...");
                FormBody body = new FormBody.Builder()
                        .add("name",etName.getText().toString())
                        .add("price",etPrice.getText().toString())
                        .add("desc",etDesc.getText().toString())
                        .build();
                Request request = new Request.Builder().url(MainActivity.BASE_URL+"/create.php").post(body).build();
                MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                Snackbar.make(rootView, "internet Error",Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String strResponse = response.body().string();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                try {
                                    JSONObject jsonResponse = new JSONObject(strResponse);
                                    if (jsonResponse.getBoolean("result")){
                                        Snackbar.make(rootView, "Success",Snackbar.LENGTH_SHORT).show();
                                    }else {
                                        Snackbar.make(rootView, jsonResponse.getString("message"),Snackbar.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                });
            }
        });

        return v;
    }
}
