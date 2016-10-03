package com.example.autismtheme;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class UploadService extends Service {
    int started = -1;

    private int getChildNum(){
        // TODO Auto-generated method stub
        SharedPreferences PublicInfo;
        String key_child = "childNumer";
        String key_public = "publicInfos";
        PublicInfo = getSharedPreferences(key_public, Context.MODE_PRIVATE);
        return PublicInfo.getInt(key_child, 0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//		try {
        Log.e("service is run", "yes");
        if (started == -1 && intent == null) {
            ArrayList<Item> items;
            items = new ArrayList<>();
            File firstFolder = new File(getFilesDir() + "/1/");
            File[] files = firstFolder.listFiles();
            for (File file : files) {
                file.getPath();
                items.add(new Item(1, file.getPath()));
                Log.e("files", file.getPath());
            }
            File secondFolder = new File(getFilesDir() + "/2/");
            files = secondFolder.listFiles();
            for (File file : files) {
                file.getPath();
                items.add(new Item(2, file.getParent()));
                Log.e("files", file.getPath());
            }
            for (Item item : items) {
                UploadFileTask uft = new UploadFileTask();
                uft.execute(item.fileName, item.childNum);
            }
        }

        if (intent != null) {
            String fileName = intent.getExtras().getString("fileName");
            UploadFileTask uft = new UploadFileTask();
            uft.execute(getFilesDir() + "/" + getChildNum() + "/" + fileName, getChildNum());
        }
        started = 1;

        return Service.START_STICKY;
//		}
//		catch (Exception exception){
//			Log.e("error",);
//			return Service.START_FLAG_REDELIVERY;
//		}
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private Boolean isInternetConnect() {

        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("internet error", "Error checking internet connection", e);
            }
        } else {
            Log.e("internet error", "No network available!");
        }
        return false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class Item {
        int childNum;
        String fileName;

        public Item(int childNum, String fileName) {
            this.childNum = childNum;
            this.fileName = fileName;
        }
    }

    public class UploadFileTask extends AsyncTask<Object, Integer, Integer> {

        SharedPreferences UserInfo;
        String key_is_upload = "Is Upload";
        //created as keys for shared preferences
        String key_userinfo = "User Info";
        //created as values inside shared preference
        String key_age = "Age Key";
        String key_gender = "Gender Key";
        String key_background = "Background Key";
        String key_phonenum = "Phone Num";
        String key_autistic = "Autistic Key";
        String key_id = "ID Key";
        String existingFileName;
        Editor editor_userinfo;
        private String serverIp = "http://helpautism.ir/autism-android";
        private int uploaded_files;
        private int childNum;


        @Override
        protected Integer doInBackground(Object... arg0) {
            this.existingFileName = (String) arg0[0];
            this.childNum = (Integer) arg0[1];

            this.UserInfo = getSharedPreferences(key_userinfo + childNum, Context.MODE_PRIVATE);
            this.editor_userinfo = UserInfo.edit();

            //every 10 second check internet  connection
            while (!isInternetConnect()) {
                android.os.SystemClock.sleep(10000);
                Log.e("ActivityRRR", existingFileName);
            }

            try {

                if (doUpload(0) == -1)
                    return -1;

            } catch (Exception ex) {
//				setIsUpload();
                return -1;
            }

            return 0;

        }


        @Override
        protected void onPostExecute(Integer result) {
            if (result == 0) {
                File file = new File(existingFileName);
                if (file.exists()) {
                    file.delete();
                }
            }
        }


        private int doUpload(int first) {

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            DataInputStream inStream;
            InputStream inError;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;

            Log.e("Debug", "try to upload11");
            String urlString = serverIp + "/upload.php?"
                    + "tel='" + UserInfo.getString(key_phonenum, null) + ""
                    + "'&male=" + (1 - UserInfo.getInt(key_gender, 0)) + ""
                    + "&back=" + (2 - UserInfo.getInt(key_background, 0)) + ""
                    + "&age='" + (UserInfo.getString(key_age, "not set")) + "'"
                    + "&id=" + UserInfo.getString(key_id, null) + ""
                    + "&first=" + first
                    + "&childNum=" + childNum
                    + "&autistic=" + UserInfo.getInt(key_autistic, 0)
                    + "&response='not_response'";


            Log.e("NUMBER!!!!", urlString);


            try {

                //------------------ CLIENT REQUEST
                Log.e("fileExists", existingFileName);
                File file = new File(existingFileName);
                if (!file.exists()) {
                    Log.e("fileexists", "no");
                    return -1;
                }
                FileInputStream fileInputStream = new FileInputStream(new File(existingFileName));

                // open a URL connection to the Servlet
                URL url = new URL(urlString);
                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                // Allow Inputs
                conn.setDoInput(true);
                // Allow Outputs
                conn.setDoOutput(true);
                // Don't  use a cached copy.
                conn.setUseCaches(false);
                // Use a post method.
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + existingFileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                // close streams
                Log.e("Debug", "File is written");
                uploaded_files++;
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                setIsUpload();
                Log.e("Debug", "error: " + ex.getMessage(), ex);
                return -1;
            } catch (IOException ioe) {
                setIsUpload();
                Log.e("Debug", "error: " + ioe.getMessage(), ioe);
                return -1;
            } catch (Exception ex) {
                setIsUpload();
                Log.e("Debug", "error: " + ex.getMessage(), ex);
                return -1;
            }

            //------------------ read the SERVER RESPONSE
            try {


                int status = conn.getResponseCode();
                inError = null;
                if (status != HttpURLConnection.HTTP_OK) {
                    inError = conn.getErrorStream();
//					for (int i = 0; i < inError.available(); i++) {
//						Log.e("error"," is "+inError.read());
//					}
                }
                Log.e("status ", " is " + status);
                inStream = new DataInputStream(conn.getInputStream());
                String str;

                while ((str = inStream.readLine()) != null) {

                    Log.e("Debug", "Server Response " + str);

                    //get ID
                    String[] resp = str.split(" ");
                    if (Integer.parseInt(resp[0]) > 0) {
                        editor_userinfo.putString(key_id, resp[0].trim());
                        editor_userinfo.apply();

                    } else {
                        setIsUpload();
                    }

                    break;

                }

                inStream.close();
                return 0;

            } catch (NumberFormatException e) {
                setIsUpload();
                Log.e("Debug", "error: Wrong number");
                return -1;


            } catch (IOException ioex) {
                setIsUpload();
                Log.e("Debug", "error: " + ioex.getMessage(), ioex);
                return -1;
            }
            catch (Exception exception){
                Log.e("folan","error");
                return -1;
            }



        }


        private void setIsUpload() {


            editor_userinfo.putString(key_is_upload, "1");
            editor_userinfo.apply();


        }


    }
}
