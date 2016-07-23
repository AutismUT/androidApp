package com.example.autismtheme;


	 

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

import android.media.AudioRecord;
import android.media.MediaRecorder;
//import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class ActivityInteractProcess extends Activity{
	Handler handler_interact=new Handler();
	private Thread thread_interact = null;
	
	View layout_interact;
	ImageView imageV_intprocess_turn;
	TextView textV_intprocess_instruction;
	AlertDialog.Builder dialogbuilder_interact_finish;
	
	private static final int ParentTurn = 0;
	private static final int BabyTurn = 1;
	private static final long ParentPeriod = 5000;
	private static final long BabyPeriod = 10000;
	

	int TurnKeeper=ParentTurn;//inside activity. the variable should be global
	int RepeatTime=1;
	int delay;
	boolean LayoutChanged=false;
	boolean InteractFinished=false;
	
	//for recorder
	private AudioRecord recorder = null;
	private MediaRecorder myRecorder;
	int BufferElements2Rec = 4096; 

	private String[] outputFile;
;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.page_interact_process);


		layout_interact =(View) findViewById(R.id.layoutintprocess);
		imageV_intprocess_turn=(ImageView) findViewById(R.id.iv_intprocess_turn);
		textV_intprocess_instruction=(TextView) findViewById(R.id.tv_intprocess_instruction);
		
		
		
		outputFile = new String[3];
			
		outputFile[0] = getFilesDir()+ "/interact.3gpp";
		outputFile[1] = getFilesDir()+ "/interact1.3gpp";
		outputFile[2] = getFilesDir()+ "/interact2.3gpp";
		//Creating AlertDialog for the end of Interaction Test
		dialogbuilder_interact_finish=new AlertDialog.Builder(this);
		dialogbuilder_interact_finish.setMessage(R.string.FinishQuestion);
		dialogbuilder_interact_finish.setPositiveButton(R.string.Again,new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				InteractManager();
			}
		});//end of setPositiveButton
		
		dialogbuilder_interact_finish.setNegativeButton(R.string.BackToTests,new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			      //upload files			
			      Context context = getApplication();
			      Intent intent = new Intent(context, UploadService.class);
			      context.startService(intent);
			}
		});//end of setNegativeButton
			
		
		//exception handler
		Thread.setDefaultUncaughtExceptionHandler(
				new UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread thread, Throwable ex) {
						Log.e("Error", "Unhandled exception: " + ex.getMessage());
						Toast.makeText(getApplicationContext(), R.string.app_fatalError, Toast.LENGTH_LONG).show();
						System.exit(1);
					}
				});

}	//end of onCreate
	
	
	@Override
	protected void onResume() {	
		super.onResume();
		
		InteractManager();		
	};//end of onResume
	
	
	
	
	private void InteractManager() {
		
		InteractFinished=false;
		thread_interact=new Thread(runnable_interact,"Interact Manager Thread");
		thread_interact.start();//this line runs the whole thread		
	}

	
	
	//Main Runnable
			Runnable runnable_interact = new Runnable() {
	    public void run() {
	    	  
			while (RepeatTime<3)
		    {
		    	prepareRecorder();  
			    try {
					myRecorder.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					    	
		    	//start recording to the new file corresponding to RepeatTime
		    	
		    	
		    	//starting baby session
		    	TurnKeeper=BabyTurn;
		    	//renewing layout 
		    	LayoutChanged=false;
		    	
		    	handler_interact.postDelayed(runnable_handler_interact, ParentPeriod);
		    	
		    	
		    	
		    	
		    	while (!LayoutChanged){
		    		
		    	}
		    	
		    	//stopping baby session
		    	
		    	TurnKeeper=ParentTurn;
		    	LayoutChanged=false;
		    	startRecording();
		    	handler_interact.postDelayed(runnable_handler_interact, BabyPeriod);
		    	
		    	//stop recording
		    	stopRecording();
		    	
		    	while (!LayoutChanged){
		    	}
		    	
		    	RepeatTime++;
		    	myRecorder.release();
				myRecorder = null;
	        
		    }//end of while statement
		
	//	Context context = getApplication();
	//	Intent intent = new Intent(context, UploadService.class);
	//	context.startService(intent);
		InteractFinished=true;
		RepeatTime=1;

		
		}



		// setup recorder before recording
		private void prepareRecorder() {
			myRecorder = new MediaRecorder();
		    myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		    myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		    myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
		    myRecorder.setOutputFile(outputFile[RepeatTime]);
		   			
		}
			};
			
			
	    
		//Handler Runnable
		Runnable runnable_handler_interact = new Runnable() {	
	    public void run() {
		    	
	    	//renew layout
			if (TurnKeeper==ParentTurn) {
			layout_interact.setBackgroundColor(Color.WHITE);
			imageV_intprocess_turn.setImageResource(R.drawable.parent);
			textV_intprocess_instruction.setText(R.string.Parents);
			LayoutChanged=true;
	        }
			
			
			if (TurnKeeper==BabyTurn) {
	        layout_interact.setBackgroundColor(Color.WHITE);
	        imageV_intprocess_turn.setImageResource(R.drawable.baby);
	        textV_intprocess_instruction.setText(R.string.Baby);
	        LayoutChanged=true;
	        }
			if ((TurnKeeper!=ParentTurn)&&(TurnKeeper!=BabyTurn))
			{
				Log.d("!!!!","TurnKeeper Exception");
			}
	        
	    }
		};//end of runnable_handler_interact
	
		
		
	
	//Alarm Dialog runnable
	Runnable runnable_interact_dialog=new Runnable() {
			
		public void run() {
				//AlertDialog for the end of Interaction Test
				AlertDialog dialog_interact_finish=dialogbuilder_interact_finish.create();				
				dialog_interact_finish.show();
		}
	};//end of runnable_interact_dialog
		
		
	
	//start recording method
	private void startRecording() {
		try{

			myRecorder.start();

		} catch (IllegalStateException e) {
			// start:it is called before prepare()
			// prepare: it is called after start() or before setOutputFormat() 
			e.printStackTrace();
		}
	   

     
       
	}



	//stop recording method
	
	private void stopRecording() {
		try {
		    if (null != recorder) {
		        myRecorder.stop();
		        
		    }
		      
		      
		      
		   } catch (IllegalStateException e) {
				//  it is called before start()
				e.printStackTrace();
		   } catch (RuntimeException e) {
				// no valid audio/video data has been received
				e.printStackTrace();
		   }
	
	}


}