package com.example.autismtheme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

//import com.squareup.picasso.Picasso;


public class SelectRecordingType extends Activity {

    String action = null;
    static Activity selectRecordingTypeInstance = null;
    public void openCryTest(View v){
        Intent i = new Intent(SelectRecordingType.this,ActivityRecord.class);
        i.putExtra("action",action);
        i.putExtra("type","only");
        startActivity(i);

    }

    public void openCryTestWithInteract(View v){
        Intent i = new Intent(this,SelectingInteractType.class);
        i.putExtra("action",action);
        startActivity(i);
    }



    public void onDestroy(){
        super.onDestroy();
        selectRecordingTypeInstance = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcryingtype);
        selectRecordingTypeInstance = this;
        ImageView imageView = (ImageView)findViewById(R.id.image_view_voice);
        Glide.with(this).load(R.drawable.voice).into(imageView);
        action = getIntent().getExtras().getString("action");
        TextView btn1 = (TextView) findViewById(R.id.buttonOnlyRecord);
        TextView btn2 = (TextView) findViewById(R.id.buttonRecordingWithInteract);
        btn1.setTypeface(CustomFontsLoader.getTypeface(this));
        btn2.setTypeface(CustomFontsLoader.getTypeface(this));
        TextView tv = (TextView)findViewById(R.id.actionTypeTitle);
        if(action.equals("cry")) {
            btn1.setText("ضبط صدای گریه");
            btn2.setText("ضبط صدای گریه همراه با تعامل");
            tv.setText("تست گریه");
        }
        else if(action.equals("laugh")){
            btn1.setText("ضبط صدای خنده");
            btn2.setText("ضبط صدای خنده همراه با تعامل");
            tv.setText("تست خنده");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selectcryingtype, menu);
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
