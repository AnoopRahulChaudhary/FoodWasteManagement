package com.android.anoop.foodwastemanagement;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SubmitFoodDetailsByGuest extends AppCompatActivity {
    EditText mEditTextLocality,mEditTextAmountOfFood,mEditTextTypeOfFood;
    String guestLocality,guestMobile,amountOfFood,typeOfFood;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_food_details_by_guest);
        sp = getSharedPreferences("userDetails",MODE_PRIVATE);
        guestMobile = sp.getString("userMobile","1234567890");
        mEditTextLocality = (EditText)findViewById(R.id.submitFoodDetailsByGuestEditTextUserLocality);
        mEditTextAmountOfFood = (EditText)findViewById(R.id.submitFoodDetailsByGuestEditTextAmountOfFoodAvailable);
        mEditTextTypeOfFood = (EditText)findViewById(R.id.submitFoodDetailsByGuestEditTextTypeOfFood);
    }

    //Submit food Details By Guest
    public  void submitFoodDetailsByGuest(View view){
        Toast.makeText(this,"Submit food details by guest",Toast.LENGTH_SHORT).show();
        if(isConnected()){//checking network
            if(validate()){//checking data
                new MyAsyncTaskToSubmitFoodDetails().execute("https://yourclick.000webhostapp.com/foodWasteManagement/submitFoodDetails.php");
            }
        }else{
            Toast.makeText(this,"Network not available",Toast.LENGTH_LONG).show();
        }

    }
    //method to check network
    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    //method to validate user data
    private boolean validate(){
        guestLocality = mEditTextLocality.getText().toString().trim();
        amountOfFood = mEditTextAmountOfFood.getText().toString().trim();
        typeOfFood = mEditTextTypeOfFood.getText().toString().trim();
        if(guestLocality.isEmpty()){
            mEditTextLocality.requestFocus();
            mEditTextLocality.setError("Empty");
            return false;
        }else if(amountOfFood.isEmpty()){
            mEditTextAmountOfFood.requestFocus();
            mEditTextAmountOfFood.setError("Empty");
            return false;
        }else if(typeOfFood.isEmpty()){
            mEditTextTypeOfFood.requestFocus();
            mEditTextTypeOfFood.setError("Empty");
            return false;
        }else {
            return true;
        }
    }
    //Subclass extending AsyncTask to perform network operation
    private class MyAsyncTaskToSubmitFoodDetails extends AsyncTask<String,Void,String>{
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SubmitFoodDetailsByGuest.this);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("guestMobile",guestMobile);
                jsonObject.accumulate("guestLocality",guestLocality);
                jsonObject.accumulate("amountOfFood",amountOfFood);
                jsonObject.accumulate("typeOfFood",typeOfFood);
                return submitFoodDetailsOnServer(strings[0],jsonObject.toString());
            } catch (JSONException e) {
                return "Json Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            if(s.equals("Json Error"))
                showMessage("Json Error");
            else {
                mEditTextLocality.setText("");
                mEditTextTypeOfFood.setText("");
                mEditTextAmountOfFood.setText("");
                showMessage(s);
                //Intent intent = new Intent(SubmitFoodDetailsByGuest.this,MainActivity.class);
                //startActivity(intent);
            }
        }
        //method to show return response
        private void showMessage(String message){
            AlertDialog.Builder builder = new AlertDialog.Builder(SubmitFoodDetailsByGuest.this);
            builder.setMessage(message);
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
        //method to send data on server
        private String submitFoodDetailsOnServer(String url , String data){
            String returnResponse="Some error occur";
            BufferedWriter writer = null;
            BufferedReader reader = null;
            try {
                URL url1 = new URL(url);
                HttpURLConnection connection = (HttpURLConnection)url1.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json ; charset=UTF-8");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                writer.write(data);
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                returnResponse="";
                while ((line=reader.readLine())!=null)
                    returnResponse += line;
                writer.close();
                reader.close();
                return returnResponse;
            } catch (MalformedURLException e) {
                return "Error in making url object";
            } catch (IOException e) {
                return "Error in connection";
            }
        }
    }
}
