package com.example.autismtheme;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityRecord extends Activity {

    SharedPreferences UserInfo;
    String fileName = null;
    String type = null;
    String action = null;
    long startTime;
    boolean isRecording = false;
    int cryingReason = -1;
    TextView textViewTestNumber;
    Editor editor_userinfo;
    Handler hl = new Handler();
    Integer numberOfSong;
    Handler hl2 = new Handler();
    private ExtAudioRecorder myRecorder = null;
    private MediaPlayer myPlayer = null;
    Runnable playSong = new Runnable() {
        @Override
        public void run() {
//				main_activity.sendToast(numberOfSong.toString()+".wav",ActivityRecord.this);
            int id = 1;
            switch (numberOfSong) {
                case 1:
                    id = R.raw.first;
                    break;
                case 2:
                    id = R.raw.second;
                    break;
                case 3:
                    id = R.raw.third;
                    break;
                case 4:
                    id = R.raw.forth;
                    break;
            }
            if (myPlayer != null) {
                myPlayer.reset();
            }
            myPlayer = MediaPlayer.create(ActivityRecord.this, id);
            AudioManager am =
                    (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            am.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                    0);
//				myPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//				mp.prepare();
            myPlayer.start();
        }
    };
    private String outputFile = null;
    private Button startBtn;
    private Button stopBtn;
    private Button playBtn;
    Runnable run2 = new Runnable() {
        @Override
        public void run() {
            stop(null);
        }
    };
    private Button stopPlayBtn;
    private int childNum;
    private boolean recordStarted = false;
    //create runnable for updating progress bar
    Runnable run = new Runnable() {

        @Override
        public void run() {
            if (!recordStarted) {
                recordStarted = true;
                startTime = System.currentTimeMillis();

            }
            Integer time = (int) (System.currentTimeMillis() - startTime);
            ProgressBar pb = (ProgressBar) findViewById(R.id.pb2);
            pb.setProgress(time);

            TextView tv = (TextView) findViewById(R.id.textSecond);
            TextView tv1 = (TextView) findViewById(R.id.textMili);

            Integer miliseconds = time % 1000;
            miliseconds /= 10;
            time /= 1000;

            if (miliseconds < 10)
                tv1.setText("0" + miliseconds.toString());
            else
                tv1.setText(miliseconds.toString());

            if (time < 10)
                tv.setText("0" + time.toString());
            else
                tv.setText(time.toString());
            hl.postDelayed(run, 10);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_cry);

        String key_userinfo = "User Info" + getChildNum();
        UserInfo = getSharedPreferences(key_userinfo, Context.MODE_PRIVATE);
        editor_userinfo = UserInfo.edit();
        textViewTestNumber = (TextView) findViewById(R.id.text_view_test_number);
        numberOfSong = UserInfo.getInt("lastPlayed", -1);
        if (numberOfSong == -1) {
            editor_userinfo.putInt("lastPlayed", 1);
            editor_userinfo.apply();
            numberOfSong = 1;
        }


        TextView tv = (TextView) findViewById(R.id.txt_help);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.RelativeLayout1);
//	   ImageView iv = (ImageView)findViewById(R.id.imageView1);
        type = (String) getIntent().getExtras().get("type");
        action = (String) getIntent().getExtras().get("action");

        TextView titleText = (TextView) findViewById(R.id.titleText);

        String middle = null;
        if (action.equals("cry")) {
            rl.setBackgroundColor(getResources().getColor(R.color.mainGreen));
//		   iv.setImageResource(R.drawable.cry_title);
            middle = "گریه ";

        } else if (action.equals("laugh")) {
            rl.setBackgroundColor(getResources().getColor(R.color.laughBlue));
//		   iv.setImageResource(R.drawable.laugh_title);
            LinearLayout ll = (LinearLayout) findViewById(R.id.topLinearLayout1);
            ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.laugh_top));
            ProgressBar pb = (ProgressBar) findViewById(R.id.pb2);
            pb.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_circle));
            ll = (LinearLayout) findViewById(R.id.topLinearLayout2);
            ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.laugh_bottom));
            RelativeLayout rl2 = (RelativeLayout) findViewById(R.id.relativeQuestion);
            rl2.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_curved));
            middle = "خنده ";

        }
        String title = null;
        if (type.equals("interactWithParent")) {
            String first = "لطفا زمانی که کودک در حال ";
            String second = "است دکمه ضبط صدا را زده و پس از شروع ضبط صدا یکبار نام او را به گونه " +
                    " ای که متوجه شود صدا زده و منتظر بمانید تا ضبط صدا " +
                    " پایان یابد.";
            tv.setText(first + middle + second);
            title = "تعامل با والدین";
        } else if (type.equals("interactWithSystem")) {
            String first = "لطفا زمانی که کودک در حال ";
            String second = "است دکمه ضبط صدا را زده و ترجیحا منتظر بمانید تا ضبط صدا پایان یابد";
            tv.setText(first + middle + second);
            title = "تعامل با سیستم ";
            textViewTestNumber.setVisibility(View.VISIBLE);
            textViewTestNumber.setText("تست شماره " + numberOfSong);

        } else if (type.equals("only")) {
            String first = "لطفا زمانی که کودک در حال ";
            String second = "است صدای او را ضبط کنید. سعی کنید محیط عاری از هرگونه " +
                    "صدای اضافه باشد و ترجیحا سعی کنید زمان ضبط به صورت اتوماتیک توسط " +
                    "برنامه پایان یابد";
            tv.setText(first + middle + second);
        }
        if (type.equals("only")) {
            titleText.setText("تست " + middle);
        } else {
            titleText.setText(title + " ( " + middle + " )");

        }


        playBtn = (Button) findViewById(R.id.btn_play);
        stopPlayBtn = (Button) findViewById(R.id.btn_play_stop);
        //set width of progress bar equal to height of it
        final ProgressBar pb = (ProgressBar) findViewById(R.id.pb2);
        pb.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = pb.getMeasuredHeight();
                pb.getLayoutParams().width = height;
                pb.requestLayout();
            }
        });
        pb.setProgress(0);
        //set height of linear layout


        childNum = getChildNum();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd#HH_mm_ss");
        String currentDateandTime = sdf.format(new Date());

        fileName = action + "#" + type + "#" + currentDateandTime + ".wav";
        outputFile = getFilesDir() + "/" + getChildNum() + "/" + fileName;


        //define buttons
        startBtn = (Button) findViewById(R.id.btn_cry_start);
        startBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                start(v);

            }
        });

        stopBtn = (Button) findViewById(R.id.btn_cry_stop);
        stopBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stop(v);
            }
        });
        stopBtn.setEnabled(false);

    }

    private int getChildNum() {
        // TODO Auto-generated method stub
        SharedPreferences PublicInfo;
        String key_child = "childNumer";
        String key_public = "publicInfos";
        PublicInfo = getSharedPreferences(key_public, Context.MODE_PRIVATE);
        return PublicInfo.getInt(key_child, 0);

    }

    public void start(View view) {


        run.run();
        if (type.equals("interactWithSystem")) {
            hl2.postDelayed(playSong, 2000);
            hl2.postDelayed(playSong, 11000);
        }
        //change color of button
        FrameLayout fl = (FrameLayout) findViewById(R.id.recordButtonChangeColor);
        ColorDrawable cd = new ColorDrawable(0x50000000);
        fl.setForeground(cd);
        fl = (FrameLayout) findViewById(R.id.frameLayoutStopRecord);
        cd = new ColorDrawable(0x00000000);
        fl.setForeground(cd);

        try{
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

        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            Log.e("Error", e.getMessage());
        }


        startBtn.setEnabled(false);
        stopBtn.setEnabled(true);

        Toast.makeText(getApplicationContext(), "ضبط شروع شد",
                Toast.LENGTH_SHORT).show();
        hl.postDelayed(run2, 20000);

    }

    public void changingUI() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.topLinearLayout1);
        ll.setVisibility(View.GONE);
        ll = (LinearLayout) findViewById(R.id.topLinearLayout2);
        ll.setVisibility(View.VISIBLE);
        FrameLayout fl = (FrameLayout) findViewById(R.id.recordButtonChangeColor);
        fl.setForeground(new ColorDrawable(0x00000000));
        ProgressBar pb = (ProgressBar) findViewById(R.id.pb2);
        pb.setVisibility(View.GONE);
        TextView tv = (TextView) findViewById(R.id.textMili);
        tv.setVisibility(View.GONE);
        tv = (TextView) findViewById(R.id.textSecond);
        tv.setVisibility(View.GONE);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeQuestion);
        rl.setVisibility(View.VISIBLE);

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
                    i = new Intent(ActivityRecord.this, UserActivity1.class);
                } else if (childNum == 2) {
                    i = new Intent(ActivityRecord.this, UserActivity2.class);
                }
                i.putExtra("upload", "1");
                i.putExtra("fileName", fileName);
                startActivity(i);
                finish();
            }
        });
        dlgAlert.show();

    }

    private void changeFileName() {
        if (action.equals("cry") && type.equals("only")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd#HH_mm_ss");
            String currentDateandTime = sdf.format(new Date());
            fileName = action + "#" + type + "#" + currentDateandTime + "#" + cryingReason + ".wav";
            File newFile = new File(getFilesDir() + "/" + getChildNum() + "/" + fileName);
            File file = new File(outputFile);
            file.renameTo(newFile);
        } else if (type.equals("interactWithSystem")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd#HH_mm_ss");
            String currentDateandTime = sdf.format(new Date());
            fileName = action + "#" + type + "#" + currentDateandTime + "#" + numberOfSong + ".wav";
            File newFile = new File(getFilesDir() + "/" + getChildNum() + "/" + fileName);
            File file = new File(outputFile);
            file.renameTo(newFile);
        }
    }

    private void uploadFile() {
//		setDoUpload();

        final Intent intent = new Intent(this, UploadService.class);
        intent.putExtra("fileName", fileName);
        startService(intent);
    }

    public void acceptInteractOrNot(View v) {


        if (action.equals("cry") && type.equals("only")) {
            askingReasonOfCrying();
        } else {
            Log.e("numberOfSong", "yes1");
            if (type.equals("interactWithSystem")) {
                Log.e("numberOfSong", "yes2");
                if (numberOfSong == 4) {
                    editor_userinfo.putInt("lastPlayed", 1);
                    editor_userinfo.apply();
                    numberOfSong = 1;
                } else {

                    numberOfSong += 1;
                    Log.e("numberofsong", "is " + numberOfSong);
                    editor_userinfo.putInt("lastPlayed", numberOfSong);
                    editor_userinfo.apply();
                }
            }
            gauideforCompletingInfo();
        }


    }


    private void gauideforCompletingInfo() {
        String userinfo = "User Info" + childNum;
        changeFileName();
        if (checkUserInfo.isUserInfoComplete(this, userinfo)) {
//			main_activity.sendToast("is complete", this);
            uploadFile();
            suggestingNextPart();
        } else {
//			main_activity.sendToast("not complete", this);
            showMessageForCompleteingInfo();
        }
    }

    public void askingReasonOfCrying() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRecord.this);
        builder.setTitle("دلیل گریه کودک چیست ؟");
        builder.setCancelable(false);
        builder.setItems(new CharSequence[]
                        {"گرسنگی", "تشنگی", "خواب آلودگی یا کم خوابی", "درد یا بیماری",
                                "کثیف بودن", "بهانه گیری", "عدم تمایل به همکاری با والدین", "سایر", "نمیدانم"
                                , "پاسخ نمیدهم"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                cryingReason = 0;
                                break;
                            case 1:
                                cryingReason = 1;
                                break;
                            case 2:
                                cryingReason = 2;
                                break;
                            case 3:
                                cryingReason = 3;
                                break;
                            case 4:
                                cryingReason = 4;
                                break;
                            case 5:
                                cryingReason = 5;
                                break;
                            case 6:
                                cryingReason = 6;
                                break;
                            case 7:
                                cryingReason = 7;
                                break;
                            case 8:
                                cryingReason = 8;
                                break;
                            case 9:
                                cryingReason = 9;
                                break;
                            case 10:
                                cryingReason = 10;
                                break;
                        }
                        gauideforCompletingInfo();


                    }
                });
        builder.create().show();
    }

    public void suggestingNextPart() {

        if (type.equals("interactWithSystem")) {

            finish();
        } else {
            AlertDialog.Builder dlgAlertFirst = new AlertDialog.Builder(this);
            dlgAlertFirst.setCancelable(false);

            String persianAction = null;
            if (action.equals("cry")) {
                persianAction = "گریه";
            } else {
                persianAction = "خنده";
            }


            if (type.equals("only")) {
                dlgAlertFirst.setMessage((Html.fromHtml("در صورتی که امکان و تمایل انجام تست" + "<b>" + "تعامل در حین " + persianAction + "</b>" + " را دارید ادامه را انتخاب کنید ")));

//				dlgAlertFirst.setMessage((Html.fromHtml("Hello " + "<b>" + "World" + "</b>")));
            } else if (type.equals("interactWithParent")) {
                dlgAlertFirst.setMessage(Html.fromHtml("در صورتی که امکان و تمایل انجام تست " + "<b>" + "تعامل با سیستم در حین " + persianAction + "</b>" + " را دارید ادامه را انتخاب کنید"));
            }
            dlgAlertFirst.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if(checkData()){
                    //upload
                    //}
                    //else{
                    //open user activity
                    //}

                    finish();
                }
            });
            dlgAlertFirst.setPositiveButton("ادامه", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (SelectingInteractType.selectingInteractType == null) {
                        Intent i = new Intent(ActivityRecord.this, SelectingInteractType.class);
                        i.putExtra("action", action);
                        startActivity(i);
                    }
                    finish();
                }
            });
            dlgAlertFirst.create().show();
        }

    }


    public void cancelRecord(View v) {
        File file = new File(outputFile);
        file.delete();
        AlertDialog.Builder dlgAlertFirst = new AlertDialog.Builder(this);
        dlgAlertFirst.setMessage("آیا میخواهید دوباره ضبط کنید؟");
        dlgAlertFirst.setCancelable(false);
        dlgAlertFirst.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File f = new File(outputFile);
                f.delete();
                LinearLayout ll = (LinearLayout) findViewById(R.id.topLinearLayout1);
                ll.setVisibility(View.VISIBLE);
                ll = (LinearLayout) findViewById(R.id.topLinearLayout2);
                ll.setVisibility(View.INVISIBLE);
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeQuestion);
                rl.setVisibility(View.GONE);
                ProgressBar pb = (ProgressBar) findViewById(R.id.pb2);
                pb.setVisibility(View.VISIBLE);
                pb.setProgress(0);
                TextView tv = (TextView) findViewById(R.id.textMili);
                tv.setVisibility(View.VISIBLE);
                tv.setText("00");
                tv = (TextView) findViewById(R.id.textSecond);
                tv.setVisibility(View.VISIBLE);
                tv.setText("00");
                startBtn.setEnabled(true);
                FrameLayout fl = (FrameLayout) findViewById(R.id.frameLayoutStopRecord);
                ColorDrawable cd = new ColorDrawable(0x50000000);
                fl.setForeground(cd);
                recordStarted = false;
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

    public void stop(View view) {
        //do nesessary changes of ui
        changingUI();
        hl2.removeCallbacks(playSong);
        if (myPlayer != null && myPlayer.isPlaying())
            myPlayer.stop();
        hl.removeCallbacks(run);
        hl.removeCallbacks(run2);

        try {
            hl.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isRecording == true) {
                        myRecorder.stop();
                        isRecording = false;
                    }

                }
            }, 500);


            stopBtn.setEnabled(false);
            playBtn.setEnabled(true);

            //upload files
            Context context = getApplication();


            Toast.makeText(getApplicationContext(), "پایان ضبط",
                    Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            //  it is called before start()
            e.printStackTrace();
        } catch (RuntimeException e) {
            // no valid audio/video data has been received
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (myRecorder != null) {
            if (isRecording) {
                hl.removeCallbacks(run2);
                isRecording = false;
                myRecorder.stop();
            }
            myRecorder.release();
            myRecorder = null;
        }
        if (myPlayer != null) {
            if (myPlayer.isPlaying()) {
                myPlayer.stop();
            }
            myPlayer.release();
            myPlayer = null;
        }
    }


    public void play(View view) {
        FrameLayout fl = (FrameLayout) findViewById(R.id.playButtonChangeColor);
        ColorDrawable cd = new ColorDrawable(0x50000000);
        fl.setForeground(cd);
        fl = (FrameLayout) findViewById(R.id.frameLayoutStopPlay);
        cd = new ColorDrawable(0x00000000);
        fl.setForeground(cd);
        playBtn.setEnabled(false);
        stopPlayBtn.setEnabled(true);
        try {
            if (myPlayer == null) {

                myPlayer = new MediaPlayer();
            } else {
                myPlayer.reset();
            }

            myPlayer.setDataSource(outputFile);
            myPlayer.prepare();
            myPlayer.start();
            main_activity.sendToast("شروع پخش", this);
            myPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playBtn.setEnabled(true);
                    stopPlayBtn.setEnabled(false);
                    FrameLayout fl = (FrameLayout) findViewById(R.id.playButtonChangeColor);
                    ColorDrawable cd = new ColorDrawable(0x00000000);
                    fl.setForeground(cd);
                    fl = (FrameLayout) findViewById(R.id.frameLayoutStopPlay);
                    cd = new ColorDrawable(0x50000000);
                    fl.setForeground(cd);
                    main_activity.sendToast("پایان پخش", ActivityRecord.this);
                }
            });


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void stopPlay(View view) {
        FrameLayout fl = (FrameLayout) findViewById(R.id.playButtonChangeColor);
        ColorDrawable cd = new ColorDrawable(0x00000000);
        fl.setForeground(cd);
        fl = (FrameLayout) findViewById(R.id.frameLayoutStopPlay);
        cd = new ColorDrawable(0x50000000);
        fl.setForeground(cd);
        Toast.makeText(getApplicationContext(), "پایان پخش",
                Toast.LENGTH_SHORT).show();
        try {
            if (myPlayer != null && myPlayer.isPlaying()) {
                myPlayer.stop();
                playBtn.setEnabled(true);
                stopPlayBtn.setEnabled(false);
                Toast.makeText(getApplicationContext(), "پایان پخش",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


}
