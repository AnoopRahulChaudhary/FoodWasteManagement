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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AgentRegistration extends AppCompatActivity {
    EditText mEditTextAgentName,mEditTextAgentPhone,mEditTextAgentPassword,mEditTextAgentLocality;
    String agentName,agentPhone,agentPassword,agentLocality;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_registration);
        mEditTextAgentName = (EditText)findViewById(R.id.agentRegistrationEditTextUserName);
        mEditTextAgentPhone = (EditText)findViewById(R.id.agentRegistrationEditTextUserPhone);
        mEditTextAgentPassword = (EditText) findViewById(R.id.agentRegistrationEditTextUserPassword);
        mEditTextAgentLocality = (EditText)findViewById(R.id.agentRegistrationEditTextUserLocality);
    }

    //Save Agent Data
    public void saveAgentData(View view){
        if(isConnected()){//Checking  Connection first
            if(isAgentDataValid()){//checking agent data
               new MyAsyncTaskToSaveAgentData().execute("https://yourclick.000webhostapp.com/foodWasteManagement/saveAgentData.php");
            }
        }else{
            Toast.makeText(this,"Network not available",Toast.LENGTH_LONG);
        }

    }
    //method to check network connection
    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    //method to validate agent data
    private boolean isAgentDataValid(){
        agentName = mEditTextAgentName.getText().toString().trim();
        agentPhone = mEditTextAgentPhone.getText().toString().trim();
        agentPassword = mEditTextAgentPassword.getText().toString().trim();
        agentLocality = mEditTextAgentLocality.getText().toString().trim();
        if(agentName.isEmpty()){
            mEditTextAgentName.requestFocus();
            mEditTextAgentName.setError("Empty");
            return false;
        }else if(agentPhone.length()!=10){
            mEditTextAgentPhone.requestFocus();
            mEditTextAgentPhone.setError("Phone number must be of 10 digit");
            return false;
        }else if(agentPassword.isEmpty()){
            mEditTextAgentPassword.requestFocus();
            mEditTextAgentPassword.setError("Empty");
            return false;
        }else if(agentLocality.isEmpty()){
            mEditTextAgentLocality.requestFocus();
            mEditTextAgentLocality.setError("Enter your Location");
            return false;
        }else {
            return true;
        }
    }

    //method to save data on server
    private String saveAgentDataOnServer(String url,String agentData) throws IOException {
        String returnResponse="Some error occur";
        BufferedWriter writer=null;
        BufferedReader reader=null;
        try {
            URL url1=new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json ; charset=UTF-8");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.write(agentData);
            writer.flush();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line="";
            returnResponse = "";
            while ((line=reader.readLine())!=null)
                returnResponse +=line;
            writer.close();
            reader.close();
            return returnResponse;
        } catch (MalformedURLException e) {
            writer.close();
            reader.close();
            return returnResponse;
        } catch (IOException e) {
            writer.close();
            reader.close();
            return returnResponse;
        }
    }

    //AsyncTask Subclass to perform network operation
    private class MyAsyncTaskToSaveAgentData extends AsyncTask<String,Void,String>{
        ProgressDialog pd=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(AgentRegistration.this);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("agentName",agentName);
                jsonObject.accumulate("agentPhone",agentPhone);
                jsonObject.accumulate("agentPassword",agentPassword);
                jsonObject.accumulate("agentLocality",agentLocality);
                return saveAgentDataOnServer(strings[0],jsonObject.toString());
            } catch (JSONException e) {
                return "Json Error";
            } catch (IOException e) {
                return "Json Error";
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String message=s;
            pd.dismiss();
            if(s.equals("Json Error")){
                message="Json Error";
                showMessageOfAgentDataSaveResponse(message);
            }else{
                showMessageOfAgentDataSaveResponse(message);
                mEditTextAgentName.setText("");
                mEditTextAgentPhone.setText("");
                mEditTextAgentLocality.setText("");
                mEditTextAgentPassword.setText("");
                Intent intent = new Intent(AgentRegistration.this,AgentLogin.class);
                startActivity(intent);
            }
        }

        private void showMessageOfAgentDataSaveResponse(String message){
            AlertDialog.Builder builder = new AlertDialog.Builder(AgentRegistration.this);
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
    }



}
