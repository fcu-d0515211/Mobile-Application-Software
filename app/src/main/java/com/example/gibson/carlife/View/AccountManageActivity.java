package com.example.gibson.carlife.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gibson.carlife.Abstract.CustomActivity;
import com.example.gibson.carlife.MainActivity;
import com.example.gibson.carlife.Model.user.User;
import com.example.gibson.carlife.R;
import com.example.gibson.carlife.Services.UserManagement;

public class AccountManageActivity extends CustomActivity implements View.OnClickListener {

  public View view;
  EditText username_ET, email_ET, phone_ET, address_ET;
  String email = "", phone = "", address = "";
  Button confirm_BTN;
  //private static final int REQUEST_CODE = 8;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_account_manage);
    init();
  }

  public void init() {

    //set EditText with the component from layout
    username_ET = findViewById(R.id.username_ET);
    phone_ET = findViewById(R.id.phone_ET);
    address_ET = findViewById(R.id.address_ET);
    email_ET = findViewById(R.id.email_ET);

    //get user input
    if (UserManagement.isLogin) {
      username_ET.setText(MainActivity.userObj.username);
      email_ET.setText(MainActivity.userObj.email);
      phone_ET.setText(MainActivity.userObj.phone);
      address_ET.setText(MainActivity.userObj.addresses.get(0).address);
    }


    //confirm button definition
    confirm_BTN = findViewById(R.id.confirm_BTN);
    confirm_BTN.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.confirm_BTN:
        Toast.makeText(view.getContext(), R.string.confirm, Toast.LENGTH_SHORT).show();
        //  Intent intent=new Intent(AccountManageActivity.this,AccountFragment.class);
        //  need to update the information of user in server
        //  haven do
        //  back to Account Fragment.java
        //startActivity(intent);
        String newAddress = address_ET.getText().toString();
        User.Address address = MainActivity.userObj.addresses.get(0);
        if (!newAddress.equals("")) {
          if (!address.address.equals("")) {
            UserManagement.updateAddress(address.id, newAddress);
          } else {
            UserManagement.addAddress(newAddress);
          }
        }
        finish();
        break;
      default:
        Toast.makeText(this, "default run", Toast.LENGTH_SHORT).show();
        break;
    }

  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    finish();
  }


}