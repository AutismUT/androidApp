package com.example.autismtheme;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class ActivityInteract extends Activity {

    //miliseconds of time passed
    FrameLayout babyInfo;
    FrameLayout parentInfo;


    ImageView imageBaby;
    ImageView imageParent;
    boolean recordStarted = false;
    String fileName;
    long startTime1;
    long startTime2;
    Integer time;
    Integer turnCount;
    SeekBar pb;
    String turn = null;
    ColorDrawable dark = null;
    ColorDrawable light = null;
    String turns;
    Handler hl = new Handler();
    //create runnable for updating progress bar
    Runnable run = new Runnable() {

        @Override
        public void run() {
            if (!recordStarted) {
                recordStarted = true;
                startTime1 = System.currentTimeMillis();

            }
            time = (int) (System.currentTimeMillis() - startTime1);

            TextView tv = null;
            TextView tv1 = null;
            if (turn.equals("parent")) {
                tv = (TextView) findViewById(R.id.text_parent_second);
                tv1 = (TextView) findViewById(R.id.text_parent_milisecond);
            } else if (turn.equals("baby")) {
                tv = (TextView) findViewById(R.id.text_baby_second);
                tv1 = (TextView) findViewById(R.id.text_baby_milisecond);
            }


            Integer miliseconds = time % 1000;
            miliseconds /= 10;
            Integer seconds = time / 1000;

            if (turn.equals("baby")) {
                seconds = 3 - seconds;
                miliseconds = 99 - miliseconds;
            }

            if (turn.equals("baby") || turn.equals("parent")) {
                if (miliseconds < 10)
                    tv1.setText("0" + miliseconds.toString());
                else
                    tv1.setText(miliseconds.toString());

                if (seconds < 10)
                    tv.setText("0" + seconds.toString());
                else
                    tv.setText(seconds.toString());
            }


            hl.postDelayed(run, 10);
        }

    };
    String outputFile;
    boolean isRecording = false;
    boolean finished = false;
    MediaPlayer mp = null;
    Runnable run2 = new Runnable() {
        @Override
        public void run() {
            pb.setProgress(mp.getCurrentPosition());
            hl.postDelayed(run2, 10);
        }
    };
    int current = 0;
    private ExtAudioRecorder myRecorder;
    private int childNum;
    private FrameLayout frameBaby;
    private FrameLayout frameParent;
    Runnable run3 = new Runnable() {
        @Override
        public void run() {
            frameParent.setForeground(new ColorDrawable(0x000000));
            imageParent.setEnabled(true);
            babyInfo.setForeground(new ColorDrawable(0x99000000));
            TextView tv = (TextView) findViewById(R.id.text_baby_second);
            tv.setText("00");
            tv = (TextView) findViewById(R.id.text_baby_milisecond);
            tv.setText("00");
            turn = "noOne";
        }
    };

    public void finishRecording(View v) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.linear_play);
        ll.setVisibility(View.VISIBLE);
        ll = (LinearLayout) findViewById(R.id.linear_finish);
        ll.setVisibility(View.GONE);
        Handler hl2 = new Handler();
        hl2.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRecording == true) {
                    myRecorder.stop();
                    isRecording = false;

                }
            }
        }, 500);
        hl.removeCallbacks(run3);
        hl.removeCallbacks(run);


        babyInfo.setForeground(new ColorDrawable(0x99000000));
        parentInfo.setForeground(new ColorDrawable(0x99000000));
        frameBaby.setForeground(new ColorDrawable(0x99000000));
        frameParent.setForeground(new ColorDrawable(0x99000000));
        imageParent.setEnabled(false);
        imageBaby.setEnabled(false);


    }


    //change file name before uploading based on turns and turnscount
    private void changeFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd#HH_mm_ss");
        String currentDateandTime = sdf.format(new Date());
        fileName = "interact#" + currentDateandTime + "#" + turnCount.toString() + "(" + turns + ")" + ".wav";
        File newFile = new File(getFilesDir() + "/" + getChildNum() + "/" + fileName);
        Log.e("newFile",newFile.getPath());
        File file = new File(outputFile);
        file.renameTo(newFile);
    }

    private void uploadFile() {

        final Intent intent = new Intent(this, UploadService.class);
        intent.putExtra("fileName", fileName);
        startService(intent);
        finish();
    }

    private void showMessageForCompleteingInfo() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("اطلاعات کودک شما ناقص است لطفا آن را کامل کنید");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = null;

                if (childNum == 1) {
                    i = new Intent(ActivityInteract.this, UserActivity1.class);
                } else if (childNum == 2) {
                    i = new Intent(ActivityInteract.this, UserActivity2.class);
                }
                i.putExtra("upload", "1");
                i.putExtra("fileName", fileName);
                startActivity(i);
                finish();
            }
        });
        dlgAlert.show();

    }

    public void accept(View v) {

        Log.e("acceptClicked","yes");
        String userinfo = "User Info" + getChildNum();
        Log.e("acceptingInteract","yes");
        changeFileName();
        if (checkUserInfo.isUserInfoComplete(this, userinfo)) {
            uploadFile();
        } else {
            showMessageForCompleteingInfo();
        }


    }

    public void recordAgain(View view) {
        File file = new File(outputFile);
        file.delete();
        if (mp != null && mp.isPlaying()) {
            mp.stop();

            hl.removeCallbacks(run2);
        }
        if (mp != null)
            mp.release();
        mp = null;
        AlertDialog.Builder dlgAlertFirst = new AlertDialog.Builder(this);
        dlgAlertFirst.setMessage("آیا میخواهید دوباره ضبط کنید؟");
        dlgAlertFirst.setCancelable(false);
        dlgAlertFirst.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                babyInfo.setForeground(new ColorDrawable(0x99000000));
                parentInfo.setForeground(new ColorDrawable(0x99000000));
                frameBaby.setForeground(new ColorDrawable(0x99000000));
                frameParent.setForeground(light);
                LinearLayout ll = (LinearLayout) findViewById(R.id.linear_finish);
                ll.setVisibility(View.GONE);
                ll = (LinearLayout) findViewById(R.id.linear_play);
                ll.setVisibility(View.GONE);
                imageParent.setEnabled(true);
                imageBaby.setEnabled(false);
                pb.setProgress(0);
                ImageButton ib = (ImageButton) findViewById(R.id.btn_play);
                ib.setImageResource(R.drawable.play);


            }
        });

        dlgAlertFirst.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });
        dlgAlertFirst.create().show();
    }

    public void stopParentStartBaby(View v) {
        Integer temp = ((int) (System.currentTimeMillis() - startTime2)) / 10;
        if (temp < 100) {
            turns += "_0" + temp.toString();
        } else {
            turns += "_" + temp.toString();
        }
        turnCount++;
        imageBaby.setEnabled(false);
        parentInfo.setForeground(new ColorDrawable(0x99000000));
        hl.postDelayed(run3, 4000);
        babyInfo.setForeground(new ColorDrawable(0x000000));
        frameBaby.setForeground(new ColorDrawable(0x99000000));
        recordStarted = false;
        turn = "baby";
    }

    @Override
    public void onPause() {
        super.onPause();
        if (myRecorder != null) {
            if (isRecording) {
                hl.removeCallbacks(run);
                isRecording = false;
                myRecorder.stop();
            }
            myRecorder.release();
            myRecorder = null;
        }
        hl.removeCallbacks(run2);
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
            mp = null;
        }
    }

    public void startRecordingInteract(View v) {

        try {
            turn = "parent";
            imageParent.setEnabled(false);
            imageBaby.setEnabled(true);
            LinearLayout ll = (LinearLayout) findViewById(R.id.linear_finish);
            ll.setVisibility(View.VISIBLE);

            if (isRecording == true) {
                //for setting in recorded file name
                turnCount++;
                //for setting in recorded file name
                Integer temp = ((int) (System.currentTimeMillis() - startTime2)) / 10;
                turns += "_" + temp.toString();
            }
            recordStarted = false;
            if (isRecording == false) {
                turnCount = 1;
                turns = "000";
                startTime2 = System.currentTimeMillis();
                if (myRecorder == null) {
                    myRecorder = ExtAudioRecorder.getInstanse(false);
                } else {
                    myRecorder.reset();
                }
                myRecorder.setOutputFile(outputFile);
                myRecorder.prepare();
                myRecorder.start();
                isRecording = true;
                //   setDoUpload();
                isRecording = true;

                File file = new File(outputFile);
                while (!(file.length() > 0)) {

                }
                run.run();
            }


            //make parent dark baby lightened
            parentInfo.setForeground(new ColorDrawable(0x000000));
            frameBaby.setForeground(new ColorDrawable(0x000000));
            frameParent.setForeground(new ColorDrawable(0x99000000));
            babyInfo.setForeground(new ColorDrawable(0x99000000));


        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            Log.e("Error", e.getMessage());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_interact);

        babyInfo = (FrameLayout) findViewById(R.id.frame_baby_info);
        parentInfo = (FrameLayout) findViewById(R.id.frame_parent_info);
        childNum = getChildNum();
        imageBaby = (ImageView) findViewById(R.id.baby);
        imageParent = (ImageView) findViewById(R.id.parent);

        frameBaby = (FrameLayout) findViewById(R.id.frame_baby);
        frameParent = (FrameLayout) findViewById(R.id.frame_parent);

        pb = (SeekBar) findViewById(R.id.progressBarPlay);
        pb.setProgress(0);
        pb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mp != null && mp.isPlaying()) {
                    mp.seekTo(seekBar.getProgress());
                } else {
                    current = seekBar.getProgress();
                }

            }
        });



        final SharedPreferences sharedPreferences = getSharedPreferences("main", Context.MODE_PRIVATE);
        //check for showing dialog or not
        if (sharedPreferences.getInt("interactAccept", -1) == -1) {
            //generating dialog
            final Dialog alertDialog = new Dialog(this);
            alertDialog.setTitle("تست تعامل");
            alertDialog.setCancelable(true);
            alertDialog.setContentView(R.layout.interact_dialogue);
            CheckBox checkBox = (CheckBox) alertDialog.findViewById(R.id.checkbox_interact);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        if (isChecked) {
                                                            editor.putInt("interactAccept", 1);
                                                        } else {
                                                            editor.putInt("interactAccept", -1);
                                                        }
                                                        editor.apply();
                                                    }
                                                }
            );
            Button button = (Button) alertDialog.findViewById(R.id.confirm);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }

        pb.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN);

        dark = new ColorDrawable(0x99000000);
        light = new ColorDrawable(0x000000);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd#HH_mm_ss");
        String currentDateandTime = sdf.format(new Date());

        fileName = "interact#" + currentDateandTime + ".wav";
        outputFile = getFilesDir() + "/" + getChildNum() + "/" + fileName;


    }

    public void playAudio(View v) {

        if (mp != null) {
            if (mp.isPlaying()) {
                current = mp.getCurrentPosition();
                mp.pause();
                hl.removeCallbacks(run2);
                ((ImageButton) v).setImageResource(R.drawable.play);
                return;
            } else {

                mp.seekTo(current);
                run2.run();
                mp.start();
                ((ImageButton) v).setImageResource(R.drawable.pause);
                return;
            }
        } else {
            try {
                current = 0;
                mp = new MediaPlayer();
                mp.setDataSource(outputFile);
                mp.prepare();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        hl.removeCallbacks(run2);
                        ImageButton ib = (ImageButton) findViewById(R.id.btn_play);
                        ib.setImageResource(R.drawable.play);
                        pb.setProgress(pb.getMax());
                        finished = true;
                        current = 0;
                    }
                });
                pb.setMax(mp.getDuration());
                mp.start();
                run2.run();

            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
            ((ImageButton) v).setImageResource(R.drawable.pause);
        }
    }




    @Override
    public void onResume() {
        super.onResume();

        ColorDrawable cd = new ColorDrawable(0x99000000);
        frameBaby.setForeground(cd);
        babyInfo.setForeground(cd);
        parentInfo.setForeground(cd);
        imageBaby.setEnabled(false);


    }


    private int getChildNum() {
        // TODO Auto-generated method stub
        SharedPreferences PublicInfo;
        String key_child = "childNumer";
        String key_public = "publicInfos";
        PublicInfo = getSharedPreferences(key_public, Context.MODE_PRIVATE);
        return PublicInfo.getInt(key_child, 0);

    }


}
