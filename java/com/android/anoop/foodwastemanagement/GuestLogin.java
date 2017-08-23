package com.android.anoop.foodwastemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class GuestLogin extends AppCompatActivity {
    private EditText mEditTextGuestPhone,mEditTextGuestPassword;
    private String guestPhone,guestPassword;
    String data,messageFromMyAsyncTask="";
    SharedPreferences sp;
    String checkLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);
        mEditTextGuestPhone = (EditText)findViewById(R.id.guestLoginEditTextUserPhone);
        mEditTextGuestPassword = (EditText)findViewById(R.id.guestLoginEditTextUserPassword);
        sp = getSharedPreferences("userDetails",MODE_PRIVATE);
        checkLogin = sp.getString("whoLogin","N");//N indicates no one has login
        if(!checkLogin.equals("N")){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }

    //Guest Login
    public void guestLogin(View view){
        NetworkUtility networkUtility = new NetworkUtility(this);
        if(networkUtility.isConnected()){
            if(validate()){
                networkUtility.new MyAsyncTask().execute("https://yourclick.000webhostapp.com/foodWasteManagement/guestLogin.php",data,"GuestLogin");
                }

        }else{
            Toast.makeText(this,"Network is not available",Toast.LENGTH_SHORT).show();
        }
    }

    //Guest Registration
    public void guestRegistration(View view){
        Intent intent = new Intent(this,guestRegistrtion.class);
        startActivity(intent);
    }
    public String getGuestPhone(){
        return guestPhone;
    }
    public void goToMainActivity(){
        mEditTextGuestPassword.setText("");
        mEditTextGuestPhone.setText("");
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validate(){
        guestPhone = mEditTextGuestPhone.getText().toString().trim();
        guestPassword = mEditTextGuestPassword.getText().toString().trim();
        if(guestPhone.isEmpty()){
            mEditTextGuestPhone.requestFocus();
            mEditTextGuestPhone.setError("Empty");
            return false;
        }else if(guestPassword.isEmpty()){
            mEditTextGuestPassword.requestFocus();
            mEditTextGuestPassword.setError("Empty");
            return false;
        }else{
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("guestPhone",guestPhone);
                jsonObject.accumulate("guestPassword",guestPassword);
                data = jsonObject.toString();
                return true;
            } catch (JSONException e) {
                Toast.makeText(this,"Error in putting value in Json Object",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
}
