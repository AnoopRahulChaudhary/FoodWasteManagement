package com.android.anoop.foodwastemanagement;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class guestRegistrtion extends AppCompatActivity {
    EditText mGuestName,mGuestPassword,mGuestPhone;
    String guestName,guestPhone,guestPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_registrtion);
        mGuestName = (EditText)findViewById(R.id.guestRegistrationEditTextUserName);
        mGuestPhone = (EditText)findViewById(R.id.guestRegistrationEditTextUserPhone);
        mGuestPassword = (EditText)findViewById(R.id.guestRegistrationEditTextUserPassword);
    }

    //Save Guest Data
    public void saveGuestData(View view){
        if(isConnected()){//Check Network connection

            if(validateGuestData()){//Check Guest data
               new MyAsyncTaskToGuestRegistration().execute("https://yourclick.000webhostapp.com/foodWasteManagement/saveGuestData.php");
            }else{

            }

        }else{
          Toast.makeText(this,"Network not available",Toast.LENGTH_LONG).show();
        }
    }

    //Check connectivity
    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    //Validate Guest Data
    private boolean validateGuestData(){
       guestName = mGuestName.getText().toString().trim();
       guestPhone = mGuestPhone.getText().toString().trim();
       guestPassword = mGuestPassword.getText().toString().trim();
       if(guestName.isEmpty()){
           mGuestName.requestFocus();
           mGuestName.setError("Empty");
           return false;
       }else if(guestPhone.isEmpty() || guestPhone.length()!=10){
          mGuestPhone.requestFocus();
          mGuestPhone.setError("Maximum 10 digit require");
          return false;
       }else if(guestPassword.isEmpty()){
           mGuestPassword.requestFocus();
           mGuestPassword.setError("Empty");
           return false;
       }else {
           return true;
       }
    }
    //Save Guest data in Server database
    private String saveGuestDataInDatabase(String url,String data) throws IOException {
        String returnResult="Some error occur";
        BufferedReader reader=null;
        BufferedWriter writer=null;

        try {
            URL muUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) muUrl.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestMethod("POST");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.write(data);
            writer.flush();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line="";
            returnResult="";
            while((line=reader.readLine())!=null)
             returnResult += line;
            writer.close();
            reader.close();
            return returnResult.trim();

        } catch (MalformedURLException e) {
            Toast.makeText(this,"Error in making URL object",Toast.LENGTH_SHORT).show();
            writer.close();
            reader.close();
            return returnResult;
        } catch (IOException e) {
            Toast.makeText(this,"Error in making HttpURLConnection object",Toast.LENGTH_SHORT).show();
            writer.close();
            reader.close();
            return returnResult;
        }
    }

    //My AsyncTask to  network operation
    private class MyAsyncTaskToGuestRegistration extends AsyncTask<String,Void,String>{
        ProgressDialog pd = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(guestRegistrtion.this);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("guestName",guestName);
                jsonObject.accumulate("guestPhone",guestPhone);
                jsonObject.accumulate("guestPassword",guestPassword);
                return saveGuestDataInDatabase(urls[0],jsonObject.toString());
            } catch (JSONException e) {
                Toast.makeText(guestRegistrtion.this,"Error in putting value in JSONObject",Toast.LENGTH_SHORT).show();
                return "Json error";
            } catch (IOException e) {
                Toast.makeText(guestRegistrtion.this,"IOException in doInBackground",Toast.LENGTH_SHORT).show();
                return "Json error";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            String message = s;
            if(s.equals("Json error")){
                message="error in sending data";
                showMessage(message);
            }
            else {
                showMessage(message);
                mGuestName.setText("");
                mGuestPhone.setText("");
                mGuestPassword.setText("");
                Intent intent = new Intent(guestRegistrtion.this,GuestLogin.class);
                startActivity(intent);
            }
        }
        private void showMessage(String message){
            AlertDialog.Builder builder = new AlertDialog.Builder(guestRegistrtion.this);
            builder.setCancelable(false);
            builder.setMessage(message);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                     dialogInterface.dismiss();
                }
            });
            builder.show();
        }
    }
}
