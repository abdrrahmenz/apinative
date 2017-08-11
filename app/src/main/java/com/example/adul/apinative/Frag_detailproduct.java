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
import android.widget.TextView;

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

public class Frag_detailproduct extends Fragment {

    View rootView;
    TextView tvId;
    EditText etName, etPrice, etDesc;
    Button btDelete, btUpdate;

    int id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = container;
        View v = inflater.inflate(R.layout.frag_detail,container, false);

        tvId = (TextView) v.findViewById(R.id.tvId);
        etName = (EditText) v.findViewById(R.id.edtName);
        etPrice = (EditText) v.findViewById(R.id.edtPrice);
        etDesc = (EditText) v.findViewById(R.id.edtDesc);
        btUpdate = (Button) v.findViewById(R.id.btUpdate);
        btDelete = (Button) v.findViewById(R.id.btDelete);

        id = getArguments().getInt("id");

        fetchData();

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduk();
            }
        });

        return v;
    }

    private void fetchData() {
        final ProgressDialog progress = ProgressDialog.show(getActivity(), "Loading","Please wait ...");
        FormBody body = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .build();

        Request request = new Request.Builder().url(MainActivity.BASE_URL+"/read.php").post(body).build();
        MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Snackbar.make(rootView,"Internet Error",Snackbar.LENGTH_SHORT).show();
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
                                JSONObject data = jsonResponse.getJSONObject("message");
                                tvId.setText(data.getString("id"));
                                etName.setText(data.getString("nama"));
                                etPrice.setText(data.getString("harga"));
                                etDesc.setText(data.getString("deskripsi"));
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

    private void updateData(){
        final ProgressDialog progress = ProgressDialog.show(getActivity(), "Loading","Please wait ...");
        FormBody body = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .add("nama", etName.getText().toString())
                .add("harga", etPrice.getText().toString())
                .add("deskripsi", etDesc.getText().toString())
                .build();

        Request request = new Request.Builder().url(MainActivity.BASE_URL+"/update.php").post(body).build();
        MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Snackbar.make(rootView,"Internet Error",Snackbar.LENGTH_SHORT).show();
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
                                Snackbar.make(rootView, "Success Update Produk",Snackbar.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().popBackStack();
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

    private void deleteProduk(){

        final ProgressDialog progress = ProgressDialog.show(getActivity(), "Loading","Please wait ...");
        FormBody body = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .build();

        Request request = new Request.Builder().url(MainActivity.BASE_URL+"/delete.php").post(body).build();
        MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Snackbar.make(rootView,"Internet Error",Snackbar.LENGTH_SHORT).show();
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
                                Snackbar.make(rootView, "Success Delete Produk",Snackbar.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().popBackStack();
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
}
