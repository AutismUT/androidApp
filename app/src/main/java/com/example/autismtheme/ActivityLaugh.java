package com.example.autismtheme;


import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityLaugh extends Activity {

   private MediaRecorder myRecorder;
   private MediaPlayer myPlayer;
   private String outputFile = null;
   private Button startBtn;
   private Button stopBtn;
   private Button playBtn;
   private Button stopPlayBtn;
   private TextView help;
   private int childNum;

   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.page_laugh);
      
      childNum = getChildNum();
      // store it to sd card
      outputFile = getFilesDir()+"/laugh"+childNum+".3gpp";

      myRecorder = new MediaRecorder();
      myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
      myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
      myRecorder.setOutputFile(outputFile);
      
      help = (TextView) findViewById(R.id.txt_laugh_help);
      help.setText(R.string.helplaugh);
      
      startBtn = (Button)findViewById(R.id.btn_laugh_start);
      startBtn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			start(v);
		}
      });
      
      stopBtn = (Button)findViewById(R.id.btn_laugh_stop);
      stopBtn.setOnClickListener(new OnClickListener() {
  		
  		@Override
  		public void onClick(View v) {
  			
  			stop(v);
  		}
      });
      
      playBtn = (Button)findViewById(R.id.btn_laugh_play);
      playBtn.setOnClickListener(new OnClickListener() {
  		
  		@Override
  		public void onClick(View v) {
  			
				play(v);	
  		}
      });
      
      stopPlayBtn = (Button)findViewById(R.id.btn_laugh_stopPlay);
      stopPlayBtn.setOnClickListener(new OnClickListener() {
  		
  		@Override
  		public void onClick(View v) {

  			stopPlay(v);
  		}
      });
   }

   public void start(View view){
	   try {
          myRecorder.prepare();
          myRecorder.start();
//          setDoUpload();
       } catch (IllegalStateException e) {
          // start:it is called before prepare()
    	  // prepare: it is called after start() or before setOutputFormat() 
          e.printStackTrace();
       } catch (IOException e) {
          
           e.printStackTrace();
        }
	   

       startBtn.setEnabled(false);
       stopBtn.setEnabled(true);
       
       Toast.makeText(getApplicationContext(), "",
    		   Toast.LENGTH_SHORT).show();
   }

   public void stop(View view){
	   try {
	      myRecorder.stop();
	      myRecorder.release();
	      myRecorder  = null;
	      
	      stopBtn.setEnabled(false);
	      playBtn.setEnabled(true);

	      //upload files			
	      Context context = getApplication();
	      Intent intent = new Intent(context, UploadService.class);
	      context.startService(intent);
	      
	      Toast.makeText(getApplicationContext(), "���� ��� ���...",
	    		  Toast.LENGTH_SHORT).show();
	   } catch (IllegalStateException e) {
			//  it is called before start()
			e.printStackTrace();
	   } catch (RuntimeException e) {
			// no valid audio/video data has been received
			e.printStackTrace();
	   }
   }
  
   public void play(View view) {
	   try{
		   myPlayer = new MediaPlayer();
		   myPlayer.setDataSource(outputFile);
		   myPlayer.prepare();
		   myPlayer.start();
		   
		   playBtn.setEnabled(false);
		   stopPlayBtn.setEnabled(true);
		  
		   
		   Toast.makeText(getApplicationContext(), "���� ��� ���...", 
				   Toast.LENGTH_SHORT).show();
	   } catch (Exception e) {
			
			e.printStackTrace();
		}
   }
   
   public void stopPlay(View view) {
	   try {
	       if (myPlayer != null) {
	    	   myPlayer.stop();
	           myPlayer.release();
	           myPlayer = null;
	           playBtn.setEnabled(true);
	           stopPlayBtn.setEnabled(false);
	          
	           
	           Toast.makeText(getApplicationContext(), "���� ��� ���...", 
					   Toast.LENGTH_SHORT).show();
	       }
	   } catch (Exception e) {
	
			e.printStackTrace();
		}
   }
   private void setDoUpload() {
		// TODO Auto-generated method stub
	   SharedPreferences UserInfo;
	   String key_is_upload="Is Upload";
	   String key_userinfo="User Info"+childNum;
	   Editor editor_userinfo;
	   
	   UserInfo=getSharedPreferences(key_userinfo, Context.MODE_PRIVATE);
	   editor_userinfo = UserInfo.edit();
	   editor_userinfo.putString(key_is_upload, "1");
	   editor_userinfo.apply();
	}
   private int getChildNum() {
	// TODO Auto-generated method stub
	   SharedPreferences PublicInfo;
	   String key_child="childNumer";
	   String key_public="publicInfos";
	   PublicInfo=getSharedPreferences(key_public, Context.MODE_PRIVATE);
	   return PublicInfo.getInt(key_child, 0);
	  
   }
}
