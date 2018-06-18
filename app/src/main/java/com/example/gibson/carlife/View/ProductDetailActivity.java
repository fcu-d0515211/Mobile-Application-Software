package com.example.gibson.carlife.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gibson.carlife.Abstract.CustomActivity;
import com.example.gibson.carlife.Adapters.ImagePagerAdapter;
import com.example.gibson.carlife.Model.DataManagement;
import com.example.gibson.carlife.Model.Product.Product;
import com.example.gibson.carlife.R;
import com.example.gibson.carlife.Services.Order.OrderManagement;
import com.example.gibson.carlife.Services.Product.ProductPicturesManagement;
import com.example.gibson.carlife.Services.UserManagement;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends CustomActivity {
  private static final String TAG = "ProductDetailActivity";
  public static ArrayList<Bitmap> images;
  private static ImagePagerAdapter imagePagerAdapter;
  private ViewPager viewPager;
  public TextView typeTV, quantityTV, titleTV, introTV, priceTV, qtyTV, totalTV;
  public Button cartBtn, minusBtn, plusBtn;
  public boolean isFavorite;

  public static void addImage(Bitmap bitmap) {
    images.add(bitmap);
    imagePagerAdapter.notifyDataSetChanged();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_detail);
    isFavorite=false;

    int position = getIntent().getIntExtra("position", -1);
    final Product item = DataManagement.getProducts().get(position);

    viewPager = (ViewPager) findViewById(R.id.productImgs_viewPager);
    final ImageView heart = findViewById(R.id.heartIMG);
    typeTV = findViewById(R.id.typeTV);
    quantityTV = findViewById(R.id.quanitltyTV);
    titleTV = findViewById(R.id.titleTV);
    introTV = findViewById(R.id.introTV);
    priceTV = findViewById(R.id.priceTV);
    totalTV = findViewById(R.id.totalTV);
    qtyTV = findViewById(R.id.qtyTV);

    cartBtn = findViewById(R.id.cart);
    minusBtn = findViewById(R.id.minusBtn);
    plusBtn = findViewById(R.id.plusBtn);

    images = new ArrayList<>();
    if(item.product_type!=null)
      typeTV.setText(item.product_type);
    else
      typeTV.setText("Null");
    quantityTV.setText(""+item.quantity);
    titleTV.setText(item.name);
    priceTV.setText(getResources().getString(R.string.taiwan) + " " + item.sale_price);
    totalTV.setText(getResources().getString(R.string.taiwan) + " " + item.sale_price);
    introTV.setText(item.description);

    if(item.imgs.size() == 0)
      ProductPicturesManagement.requestProductImages(item.id);
    else {
      images = item.imgs;
      imagePagerAdapter.notifyDataSetChanged();
    }


    cartBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(!UserManagement.isLogin) {
          ProductDetailActivity.longTost(getResources().getString(R.string.you_are_not_login));
          return;
        }
        int quantity = Integer.valueOf(qtyTV.getText().toString());
        ProductDetailActivity.shortTost( getResources().getString(R.string.put_in_cart));
        OrderManagement.addProductToCart(item.id, quantity);
      }
    });

    heart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(!UserManagement.isLogin) {
          ProductDetailActivity.shortTost(getResources().getString(R.string.you_are_not_login));
          return;
        }
        if(isFavorite){
          heart.setImageResource(R.drawable.white_heart);
          isFavorite=false;
        }
        else {
          heart.setImageResource(R.drawable.white_heart_fill);
          isFavorite=true;
        }
      }
    });

    minusBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int quantity = Integer.valueOf(qtyTV.getText().toString());
        if(quantity > 1) {
          quantity -= 1;
          qtyTV.setText(String.valueOf(quantity));
          totalTV.setText(getResources().getString(R.string.taiwan) + " " + item.sale_price * quantity);
        }
      }
    });

    plusBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int quantity = Integer.valueOf(qtyTV.getText().toString());
        if(quantity < item.quantity) {
          quantity += 1;
          qtyTV.setText(String.valueOf(quantity));
          totalTV.setText(getResources().getString(R.string.taiwan) + " " + item.sale_price * quantity);
        }
      }
    });

    List<Integer> list = new ArrayList<Integer>();
    imagePagerAdapter = new ImagePagerAdapter(this, images);
    viewPager.setAdapter(imagePagerAdapter);
  }

}
