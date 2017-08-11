package com.example.adul.apinative.Helper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adul.apinative.Frag_detailproduct;
import com.example.adul.apinative.MainActivity;
import com.example.adul.apinative.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by adul on 23/04/17.
 */

public class Adapter_ListProduct extends RecyclerView.Adapter<Holder_ListProduct> {

    Context context;
    JSONArray json_listproduct;

    public void setJson_listproduct(JSONArray json_listproduct) {
        this.json_listproduct = json_listproduct;
        notifyDataSetChanged();
    }

    public Adapter_ListProduct(Context context) {
        this.context = context;
    }

    @Override
    public Holder_ListProduct onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_product, parent, false);
        return new Holder_ListProduct(v);
    }

    @Override
    public void onBindViewHolder(Holder_ListProduct holder, int position) {

        try {
            final JSONObject productData = json_listproduct.getJSONObject(position);
            holder.tvId.setText("#ID : "+productData.getString("id"));
            holder.tvName.setText("Name : "+productData.getString("nama"));
            holder.tvPrice.setText("Price : "+productData.getString("harga"));
            holder.tvDesc.setText("Description : "+productData.getString("deskripsi"));
            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Bundle args = new Bundle();
                        args.putInt("id",productData.getInt("id"));

                        Fragment frag = new Frag_detailproduct();
                        frag.setArguments(args);
                        ((MainActivity)context).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frag_container,frag)
                                .addToBackStack(null)
                                .commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (json_listproduct!=null){
            return json_listproduct.length();
        }else {
            return 0;
        }
    }

}

class Holder_ListProduct extends RecyclerView.ViewHolder {

    TextView tvId, tvName, tvPrice, tvDesc;
    View rootView;

    public Holder_ListProduct(View itemView) {
        super(itemView);
        rootView = itemView;
        tvId = (TextView) rootView.findViewById(R.id.tvId);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        tvPrice = (TextView) rootView.findViewById(R.id.tvPrice);
        tvDesc = (TextView) rootView.findViewById(R.id.tvDesc);
    }
}
