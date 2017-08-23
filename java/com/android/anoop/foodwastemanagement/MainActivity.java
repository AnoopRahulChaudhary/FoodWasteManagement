package com.android.anoop.foodwastemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button mButtonGuestLogin,mButtonAgentLogin,mButtonCheckForFood,mButtonSubmitFoodDetails,mButtonLogout;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //find button ids
        mButtonAgentLogin = (Button)findViewById(R.id.mainActivityButtonAgentLogin);
        mButtonGuestLogin = (Button)findViewById(R.id.mainActivityButtonGuestLogin);
        mButtonCheckForFood = (Button)findViewById(R.id.mainActivityButtonCheckForFood);
        mButtonSubmitFoodDetails = (Button)findViewById(R.id.mainActivityButtonSubmitFoodDetails);
        mButtonLogout = (Button)findViewById(R.id.mainActivityButtonLogout);

        sp = getSharedPreferences("userDetails",MODE_PRIVATE);
        String whoLogin=sp.getString("whoLogin","N");
        if(whoLogin.equals("guest")){
            mButtonGuestLogin.setVisibility(View.GONE);
            mButtonAgentLogin.setVisibility(View.GONE);
            mButtonCheckForFood.setVisibility(View.GONE);
            mButtonSubmitFoodDetails.setVisibility(View.VISIBLE);
            mButtonLogout.setVisibility(View.VISIBLE);
        }else if(whoLogin.equals("agent")){
            mButtonGuestLogin.setVisibility(View.GONE);
            mButtonAgentLogin.setVisibility(View.GONE);
            mButtonCheckForFood.setVisibility(View.VISIBLE);
            mButtonSubmitFoodDetails.setVisibility(View.GONE);
            mButtonLogout.setVisibility(View.VISIBLE);
        }else{
            mButtonGuestLogin.setVisibility(View.VISIBLE);
            mButtonAgentLogin.setVisibility(View.VISIBLE);
            mButtonCheckForFood.setVisibility(View.GONE);
            mButtonSubmitFoodDetails.setVisibility(View.GONE);
            mButtonLogout.setVisibility(View.GONE);
        }

    }

    //go to guest login page
    public void goToGuestLoginPage(View view){
        Intent intent = new Intent(this,GuestLogin.class);
        startActivity(intent);
    }

    //go to agent login page
    public void goToAgentLoginPage(View view){
        Intent intent = new Intent(this,AgentLogin.class);
        startActivity(intent);
    }

    //go to SearchFoodByAgent Activity
    public void goToSearchFoodByAgent(View view){
        Intent intent = new Intent(this,SearchFoodByAgent.class);
        startActivity(intent);
    }

    //go to SubmitFoodDetailsByGuest Activity
    public void goToSubmitFoodDetailsByGuest(View view){
        Intent intent = new Intent(this,SubmitFoodDetailsByGuest.class);
        startActivity(intent);
    }

    //Guest or Agent logout method
    public void logout(View view){
      SharedPreferences.Editor spe = sp.edit();
      spe.putString("whoLogin","N");
      spe.commit();
      Intent intent = new Intent(this,MainActivity.class);
      startActivity(intent);
    }

}
