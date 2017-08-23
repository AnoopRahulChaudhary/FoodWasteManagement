package com.android.anoop.foodwastemanagement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.json.JSONArray;
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

/**
 * Created by anoop on 8/22/2017.
 */

public class NetworkUtility {
    private static Context context;
    public NetworkUtility(Context context){
        this.context = context;
    }

    public class MyAsyncTask extends AsyncTask<String,Void,String>{
        ProgressDialog progressDialog = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {//strings[0] is url,strings[1] is data which we have to to send on server,strings[2] is class name which has requested to operation.
            try {
                return saveDataOnServer(strings[0],strings[1])+"#"+strings[2];
            } catch (IOException e) {
                return "error in input/output to calling saveDataOnServer#"+strings[2];
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            String[] values=s.split("#");
            if(values[1].equals("GuestLogin")){
                if(values[0].equals("invalid details"))
                    Toast.makeText(context,"Invalid Details",Toast.LENGTH_SHORT).show();
                else if(values[0].charAt(0)=='@'){
                    SharedPreferences sp = context.getSharedPreferences("userDetails",Context.MODE_PRIVATE);
                    SharedPreferences.Editor spe = sp.edit();
                    String[] name= values[0].split("@");
                    spe.putString("name",name[1]);
                    spe.putString("mobile",new GuestLogin().getGuestPhone());
                    spe.putString("whoLogin","guest");
                    spe.commit();
                    Toast.makeText(context,"Welcome "+name[1],Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent((Activity)context,MainActivity.class);
                    context.startActivity(intent);
                }else {
                    Toast.makeText(context,values[0],Toast.LENGTH_SHORT).show();
                }
            }else if(values[1].equals("AgentLogin")){
                if(values[0].equals("invalid details"))
                    Toast.makeText(context,"Invalid Details",Toast.LENGTH_SHORT).show();
                else if(values[0].charAt(0)=='@'){
                    SharedPreferences sp = context.getSharedPreferences("userDetails",Context.MODE_PRIVATE);
                    SharedPreferences.Editor spe = sp.edit();
                    String[] name= values[0].split("@");
                    spe.putString("name",name[1]);
                    spe.putString("mobile",new GuestLogin().getGuestPhone());
                    spe.putString("whoLogin","agent");
                    spe.commit();
                    Toast.makeText(context,"Welcome "+name[1],Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent((Activity)context,MainActivity.class);
                    context.startActivity(intent);
                }else {
                    Toast.makeText(context,values[0],Toast.LENGTH_SHORT).show();
                }
            }else if(values[1].equals("SearchFoodByAgent")){
              String response[]=values[0].split("@");
                if(response[0].equals("0")){
                    try {
                        JSONObject jsonObject = new JSONObject(response[1]);
                        JSONArray mobileArray=jsonObject.getJSONArray("mobile");
                        String text = "";
                        for(int i=0;i<mobileArray.length();i++){
                            JSONObject jsonObject1 = mobileArray.getJSONObject(i);
                            text += "mobile:"+jsonObject1.getString("mobile")+"\n";
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(text);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                              dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    } catch (JSONException e) {
                        Toast.makeText(context,"Json Parsing error in Search food by agent",Toast.LENGTH_LONG).show();
                    }
                }else if(response[0].equals("1")){
                    Toast.makeText(context,response[1],Toast.LENGTH_LONG).show();
                }else if(response[0].equals("2")){
                    Toast.makeText(context,response[1],Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context,response[0],Toast.LENGTH_LONG).show();
                }
            }
        }
        private String saveDataOnServer(String url , String data) throws IOException {
            String returnResponse="";
            BufferedReader reader = null;
            BufferedWriter writer = null;
            try {
                URL url1 = new URL(url);
                HttpURLConnection connection = (HttpURLConnection)url1.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);
                connection.setRequestProperty("Content-Type","application/json ; charset=UTF-8");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                writer.write(data);
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                while ((line=reader.readLine())!=null)
                    returnResponse += line;
                writer.close();
                reader.close();
                return returnResponse;

            } catch (MalformedURLException e) {
                writer.close();
                reader.close();
                return "Error in making url";
            } catch (IOException e) {
                writer.close();
                reader.close();
                return "Error in input output";
            }
        }
    }

    //method to check network connection
    public static boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}
