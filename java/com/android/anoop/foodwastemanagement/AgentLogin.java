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

public class AgentLogin extends AppCompatActivity {
    EditText mEditTextAgentPhone,mEditTextAgentPassword;
    String agentPhone,agentPassword;
    String data;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_login);
        mEditTextAgentPhone = (EditText)findViewById(R.id.agentLoginEditTextUserPhone);
        mEditTextAgentPassword = (EditText) findViewById(R.id.agentLoginEditTextUserPassword);
        sp = getSharedPreferences("userDetails",MODE_PRIVATE);
        String checkLogin = sp.getString("whoLogin","N");
        if(!checkLogin.equals("N")){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }

    //Agent Login
    public void agentLogin(View view){
        NetworkUtility networkUtility = new NetworkUtility(this);
        if(networkUtility.isConnected()){
            if(validate()){
            networkUtility.new MyAsyncTask().execute("https://yourclick.000webhostapp.com/foodWasteManagement/agentLogin.php",data,"AgentLogin");
            }
        }else{
            Toast.makeText(this,"Network is not available",Toast.LENGTH_LONG).show();
        }
    }

    //Agent Registration
    public void agentRegistration(View view){
        Intent intent = new Intent(this , AgentRegistration.class);
        startActivity(intent);
    }
    //method to check agent enter data
    private boolean validate(){
        agentPassword = mEditTextAgentPassword.getText().toString().trim();
        agentPhone = mEditTextAgentPhone.getText().toString().trim();
        if(agentPhone.length()!=10){
            mEditTextAgentPhone.requestFocus();
            mEditTextAgentPhone.setError("Phone must be of 10 digit");
            return false;
        }else if(agentPassword.isEmpty()){
            mEditTextAgentPassword.requestFocus();
            mEditTextAgentPassword.setError("Empty");
            return false;
        }else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("agentPhone",agentPhone);
                jsonObject.accumulate("agentPassword",agentPassword);
                data = jsonObject.toString();
                return true;
            } catch (JSONException e) {
                Toast.makeText(this,"Error in putting data in json object",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }
}
