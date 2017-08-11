package com.example.adul.apinative;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.adul.apinative.Helper.Adapter_ListProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by adul on 23/04/17.
 */

public class Frag_listproduct extends Fragment {

    View rootView;
    Button bt_create;
    RecyclerView rv_list_product;
    Adapter_ListProduct adapterListProduct;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = container;
        View v = inflater.inflate(R.layout.frag_list, container, false);

        bt_create = (Button) v.findViewById(R.id.bt_create);
        bt_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag = new Frag_createproduct();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frag_container,frag)
                        .addToBackStack(null)
                        .commit();
            }
        });
        rv_list_product = (RecyclerView) v.findViewById(R.id.rv_listproduct);
        adapterListProduct = new Adapter_ListProduct(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_list_product.setLayoutManager(layoutManager);
        rv_list_product.setAdapter(adapterListProduct);

        final ProgressDialog progress = ProgressDialog.show(getActivity(), "Loading","Please wait ...");
        Request request = new Request.Builder().url(MainActivity.BASE_URL+"/listproduk.php").build();
        MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("aduull", "onResponse: "+e.getMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Snackbar.make(rootView, "Internet error", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String strResponse = response.body().string();
                Log.i("aduull", "onResponse: "+strResponse);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            progress.dismiss();
                            JSONObject jsonResponse = new JSONObject(strResponse);
                            if (jsonResponse.getBoolean("result")){
                                JSONArray array = jsonResponse.getJSONArray("message");
                                adapterListProduct.setJson_listproduct(array);

                            } else {
                                Snackbar.make(rootView, jsonResponse.getString("message"),Snackbar.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        return v;
    }
}
