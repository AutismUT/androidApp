package com.example.autismtheme;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class main_activity extends Activity implements OnClickListener {


	public static void sendToast(String input,Context context){
		Toast.makeText(context, input, Toast.LENGTH_SHORT).show();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);



				//declaring main menu buttons
				Button about=(Button) findViewById(R.id.about_autism_button);
				Button help=(Button) findViewById(R.id.help_button);
				Button info=(Button) findViewById(R.id.info_button);
				Button upload=(Button) findViewById(R.id.upload_button);
				Button user=(Button) findViewById(R.id.user_button);
				Button record=(Button) findViewById(R.id.voice_recorder_button);
				
				//declaring onclicklistener functions
				about.setOnClickListener(this);
				help.setOnClickListener(this);
				info.setOnClickListener(this);
				upload.setOnClickListener(this);
				user.setOnClickListener(this);
				record.setOnClickListener(this);
				
				
				//upload files			
//				Context context = getApplication();
//				Intent intent = new Intent(context, UploadService.class);
//				context.startService(intent);

		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
				//declaring intents
				Intent int_about_autism=new Intent(main_activity.this,AboutAutismActivity.class);
				Intent int_help=new Intent(main_activity.this,HelpActivity.class);
				Intent int_info=new Intent(main_activity.this,InfoActivity.class);
				Intent int_upload=new Intent(main_activity.this,UserActivity2.class);
				Intent int_user=new Intent(main_activity.this,UserActivity1.class);
				Intent int_voice_recorder=new Intent(main_activity.this,selectAction.class);

				
				//starting corresponding intents
				if (arg0.getId()==R.id.about_autism_button){
					startActivity(int_about_autism);
				}
				
				if (arg0.getId()==R.id.help_button){
					startActivity(int_help);
				}
				if (arg0.getId()==R.id.info_button){
					startActivity(int_info);
				}
				if (arg0.getId()==R.id.upload_button){
					Log.e("BBBBB","user activity1");
					startActivity(int_upload);			

				}
				if (arg0.getId()==R.id.user_button){
					Log.e("BBBBB","user activity2");
					startActivity(int_user);			
				}
				if (arg0.getId()==R.id.voice_recorder_button){
						startActivity(int_voice_recorder);
				}
				
	}
	

}
