package com.example.autismtheme;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//for sending edit user info request to server
public class EditUserInfo extends Service {
    public EditUserInfo() {
    }

    int childNum;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("edit User","service");
        childNum = Integer.parseInt(intent.getExtras().getString("childNum"));
        ChangeInfo uft = new ChangeInfo();
        uft.execute();
        return Service.START_FLAG_REDELIVERY;
    }

    private Boolean isInternetConnect() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public class ChangeInfo extends AsyncTask<Void, Integer, Integer> {

        SharedPreferences UserInfo;

        private String serverIp = "http://helpautism.ir/autism-android/";

        //created as keys for shared preferences
        String key_userinfo = "User Info";

        //created as values inside shared preference
        String key_age = "Age Key";
        String key_gender = "Gender Key";
        String key_background = "Background Key";
        String key_phonenum = "Phone Num";
        String key_autistic = "Autistic Key";
        String key_id = "ID Key";
        private int uploaded_files;
        SharedPreferences.Editor editor_userinfo;


        @Override
        protected Integer doInBackground(Void... arg0) {


            UserInfo = getSharedPreferences(key_userinfo + childNum, Context.MODE_PRIVATE);
            editor_userinfo = UserInfo.edit();

            //every 10 second check internet  connection
            while (!isInternetConnect()) {
                android.os.SystemClock.sleep(10000);
                Log.e("ActivityRRR", "not connect");
            }

            try {

                int a= 2-UserInfo.getInt(key_background,0);
                Log.e("ffffffffffffffffffff", " is "+a);
                String urlString = serverIp+"/edit_user.php?"
                        + "&tel='"+UserInfo.getString(key_phonenum, null)+""
                        + "'&male="+(1- UserInfo.getInt(key_gender, 0))+""
                        + "&back="+(2 - UserInfo.getInt(key_background, 0))+""
                        + "&age='"+(UserInfo.getString(key_age, "not set"))+"'"
                        +"&id="+UserInfo.getString(key_id, null).trim()+""
                        +"&autistic="+UserInfo.getInt(key_autistic, 0);
                Log.e("url",urlString);
                String response = doHttpUrlConnectionAction(urlString);
                Log.e("response is",response);
                if(response.equals("-1"))
                    return -1;




            } catch (Exception ex) {
                return -1;
            }

            return 0;

        }


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }



    }


    private String doHttpUrlConnectionAction(String desiredUrl)
            throws Exception
    {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder;

        try
        {
            url = new URL(desiredUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(15*1000);
            connection.connect();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line + "\n");
            }
            return stringBuilder.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }

    }

}
