package com.example.autismtheme;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.IOException;

import io.fabric.sdk.android.Fabric;


//import com.squareup.picasso.Picasso;


public class main_activity extends Activity implements OnClickListener {

    PendingIntent pendingIntent;
    AlarmManager am;

    public static void sendToast(String input, Context context) {
        Toast.makeText(context, input, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        am.cancel(pendingIntent);
    }

    @Override
    public void onPause() {
        super.onPause();
        am.setRepeating(am.RTC_WAKEUP, System.currentTimeMillis() + am.INTERVAL_DAY * 10, am.INTERVAL_DAY * 10, pendingIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        File firstFolder = new File(getFilesDir() + "/1");
        if(!firstFolder.exists())
            firstFolder.mkdir();
        File secondFolder = new File(getFilesDir() + "/2");
        if(!secondFolder.exists())
            secondFolder.mkdir();
        if(firstFolder.exists()){
            Log.e("yes",firstFolder.toString());
        }
        if(secondFolder.exists()){
            Log.e("yes",secondFolder.toString());
        }

        Intent intent = new Intent(main_activity.this, Receiver.class);
        pendingIntent = PendingIntent.getBroadcast(main_activity.this, 1, intent, 0);

        am = (AlarmManager) getSystemService(ALARM_SERVICE);


        setContentView(R.layout.main_menu);
        final SharedPreferences sharedPreferences = getSharedPreferences("main", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("accept", -1) == -1) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setMessage(getResources().getString(R.string.confirmation))
                    .setCancelable(false)
                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("accept", 1);
                            editor.apply();
                            dialog.dismiss();
                        }
                    })
                    .setOnKeyListener(new DialogKeyListener()).show();
            TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
            textView.setTypeface(CustomFontsLoader.getTypeface(this));
            textView.setTextSize(20);
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(20);
        }


        //declaring main menu buttons
        Button about = (Button) findViewById(R.id.about_autism_button);
        Button help = (Button) findViewById(R.id.help_button);
        Button info = (Button) findViewById(R.id.info_button);
        Button upload = (Button) findViewById(R.id.upload_button);
        Button user = (Button) findViewById(R.id.user_button);
        Button record = (Button) findViewById(R.id.voice_recorder_button);

        //declaring onclicklistener functions
        about.setOnClickListener(this);
        help.setOnClickListener(this);
        info.setOnClickListener(this);
        upload.setOnClickListener(this);
        user.setOnClickListener(this);
        record.setOnClickListener(this);


        ImageView imageView = (ImageView) findViewById(R.id.image_view_background);
        Glide.with(this).load(R.drawable.main).into(imageView);
        //upload files
//				Context context = getApplication();
//				Intent intent = new Intent(context, UploadService.class);
//				context.startService(intent);


    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

        //declaring intents
        Intent int_about_autism = new Intent(main_activity.this, AboutAutismActivity.class);
        Intent int_help = new Intent(main_activity.this, HelpActivity.class);
        Intent int_info = new Intent(main_activity.this, InfoActivity.class);
        Intent int_upload = new Intent(main_activity.this, UserActivity2.class);
        Intent int_user = new Intent(main_activity.this, UserActivity1.class);
        Intent int_voice_recorder = new Intent(main_activity.this, selectAction.class);


        //starting corresponding intents
        if (arg0.getId() == R.id.about_autism_button) {
            startActivity(int_about_autism);
        }

        if (arg0.getId() == R.id.help_button) {
            startActivity(int_help);
        }
        if (arg0.getId() == R.id.info_button) {
            startActivity(int_info);
        }
        if (arg0.getId() == R.id.upload_button) {
            Log.e("BBBBB", "user activity1");
            startActivity(int_upload);

        }
        if (arg0.getId() == R.id.user_button) {
            Log.e("BBBBB", "user activity2");
            startActivity(int_user);
        }
        if (arg0.getId() == R.id.voice_recorder_button) {
            startActivity(int_voice_recorder);
        }

    }

    private class DialogKeyListener implements android.content.DialogInterface.OnKeyListener {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss();
                finish(); // or something like that
                return false;
            }
            return true;
        }
    }


}
