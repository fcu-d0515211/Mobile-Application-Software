package com.example.gibson.carlife.Services.Product;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.gibson.carlife.MainActivity;
import com.example.gibson.carlife.Model.ProductBrand;
import com.example.gibson.carlife.Services.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductBrandManagement extends RequestManager {
    public static void requestProductBrand() {
        final String url = host + "/product_brand";
        MainActivity.showLoading("Loading");
        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int id = object.getInt("id");

                                MainActivity.productbrands.add(
                                        new ProductBrand(
                                                id,
                                                object.getInt("grade"),
                                                object.getString("name"),
                                                object.getString("created_at"),
                                                object.getString("updated_at")
                                        ));
                                getImage(id ,i);
                                }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        MainActivity.dismissLoading();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        MainActivity.dismissLoading();
                    }
                }
        );
        MainActivity.volleyQueue.add(request);
    }

    public static void getImage(int id, final int i) {
        final String url = host + "/product_brand_img/" + id;

        ImageRequest request = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        MainActivity.productbrands.get(i).setImg(response);
                    }
                },
                64,
                64,
                ImageView.ScaleType.FIT_CENTER,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        MainActivity.volleyQueue.add(request);
    }
}