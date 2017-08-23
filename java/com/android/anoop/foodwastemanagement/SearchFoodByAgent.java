package com.android.anoop.foodwastemanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchFoodByAgent extends AppCompatActivity {
    EditText mEditTextForLocality;
    String foodLocality;
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food_by_agent);
        mEditTextForLocality = (EditText)findViewById(R.id.searchFoodByAgentEditTextForLocality);
    }

    //Search food by agent
    public void search(View view){
        NetworkUtility networkUtility = new NetworkUtility(this);
        if(networkUtility.isConnected()){
            if(validate()){
                new NetworkUtility(this).new MyAsyncTask().execute("https://yourclick.000webhostapp.com/foodWasteManagement/foodInformation.php",data,"SearchFoodByAgent");
            }
        }else{
            Toast.makeText(this,"Network is not available",Toast.LENGTH_SHORT).show();
        }
    }
    private boolean validate(){
        foodLocality = mEditTextForLocality.getText().toString().trim();
        if(foodLocality.isEmpty()){
            mEditTextForLocality.requestFocus();
            mEditTextForLocality.setError("Enter Location");
            return false;
        }else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("foodLocality",foodLocality);
                data = jsonObject.toString();
                return true;
            } catch (JSONException e) {
                Toast.makeText(this,"Error in putting data in json object",Toast.LENGTH_SHORT);
                return false;
            }
        }
    }
}
