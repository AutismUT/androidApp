package com.example.autismtheme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class SelectingInteractType extends Activity {

    static Activity selectingInteractType =null;
    String action = null;
    public void openCryTestInteractWithParent(View v){
        Intent i = new Intent(this,ActivityRecord.class);
        i.putExtra("action",action);
        i.putExtra("type","interactWithParent");
        startActivity(i);
    }

    public void openCryTestInteractWithSystem(View v){
        Intent i = new Intent(this,ActivityRecord.class);
        i.putExtra("action",action);
        i.putExtra("type","interactWithSystem");
        startActivity(i);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        selectingInteractType = null;
    }

    public void exit(View v){
        finish();
        if(selectAction.selectActionInstance != null)
            selectAction.selectActionInstance.finish();
        if(SelectRecordingType.selectRecordingTypeInstance != null)
            SelectRecordingType.selectRecordingTypeInstance.finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecting_interact_type);
        action = getIntent().getExtras().getString("action");
        TextView tv = (TextView)findViewById(R.id.actionTypeTitle);
        selectingInteractType = this;
        if(action.equals("cry")){
            tv.setText("تست گریه همراه تعامل");
        }
        else if(action.equals("laugh")){
            tv.setText("تست خنده همراه با تعامل");
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selecting_interact_type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
