package com.example.autismtheme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;
import java.util.Date;


public class selectAction extends Activity {

    SharedPreferences UserInfo;

    static Activity selectActionInstance;
    //created as keys for shared preferences
    String key_userinfo="User Info";
    //created as values inside shared preference
    String key_age="Age Key";
    String key_gender="Gender Key";
    String key_background="Background Key";
    String key_phonenum="Phone Num";
    String key_id="ID Key";

    JalaliCalendar jc = new JalaliCalendar();

    int childNum;

    private String buttonText(Integer i){
        try {
            UserInfo = getSharedPreferences(key_userinfo + i.toString(), Context.MODE_PRIVATE);
            String date = UserInfo.getString(key_age, "NULL");
            if(date.equals("NULL")){
                throw new Exception("null");
            }
            int genNum = UserInfo.getInt(key_gender, 0);

            //calculating year of birthday in georgian
            Date d = jc.getGregorianDate(date);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(d);
            Integer yearBirthday = cal1.get(Calendar.YEAR);

            //current year
            Calendar cal2 = Calendar.getInstance();
            int yearCurrent = cal2.get(Calendar.YEAR);

            //calculate child age
            Integer childAge = yearCurrent - yearBirthday;

            String gender = genNum == 0 ? "پسر" : "دختر";
            if(childAge>0) {
                return "کودک " + gender + "\n"  + childAge.toString()+" ساله";
            }
            else{
                int monthCurrent = cal2.get(Calendar.MONTH);
                int monthBirthday = cal1.get(Calendar.MONTH);
                Integer month = monthCurrent - monthBirthday;
                if(month>0) {
                    return "کودک " + gender + "\n" + month.toString() + " ماهه";
                }
                else{
                    int dayCurrent = cal2.get(Calendar.DAY_OF_MONTH);
                    int dayBirthday = cal1.get(Calendar.DAY_OF_MONTH);
                    Integer day = dayCurrent - dayBirthday;
                    return "کودک " + gender + "\n"  +day.toString() +" روزه";

                }
            }

        }
        catch (Exception e){
            String child = i==1?"اول":"دوم";
            return "کودک "+child+"\n";
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        selectActionInstance = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_action);
        selectActionInstance = this;

        String firstButton = buttonText(1);
        String secondButton = buttonText(2);

        AlertDialog.Builder dlgAlertFirst  = new AlertDialog.Builder(selectAction.this);
        dlgAlertFirst.setMessage("اطلاعاتی که میخواهید ضبط کنید مربوط به کدام فرزند شماست؟" );
        dlgAlertFirst.setTitle("انتخاب کودک");
        childNum=1;

        dlgAlertFirst.setPositiveButton(firstButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                //declaring buttons
                childNum=1;
                SharedPreferences PublicInfo;
                String key_child="childNumer";
                String key_public="publicInfos";

                SharedPreferences.Editor editor_publicInfo;

                PublicInfo=getSharedPreferences(key_public, Context.MODE_PRIVATE);
                editor_publicInfo = PublicInfo.edit();
                Log.e("ActivityRRR", "user" + childNum);
                editor_publicInfo.putInt(key_child, childNum);
                editor_publicInfo.apply();
//	       		showInfosDialog();

            }
        });
        dlgAlertFirst.setNegativeButton(secondButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                childNum = 2;
                SharedPreferences PublicInfo;
                String key_child = "childNumer";
                String key_public = "publicInfos";

                SharedPreferences.Editor editor_publicInfo;

                PublicInfo = getSharedPreferences(key_public, Context.MODE_PRIVATE);
                editor_publicInfo = PublicInfo.edit();
                Log.e("ActivityRRR", "user" + childNum);
                editor_publicInfo.putInt(key_child, childNum);
                editor_publicInfo.apply();
//	        	showInfosDialog();
            }
        });


        dlgAlertFirst.setCancelable(true);
        dlgAlertFirst.create().show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_action, menu);
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

    public void openCryTest(View v){
        Intent i=new Intent(selectAction.this,SelectRecordingType.class);
        i.putExtra("action", "cry");
        startActivity(i);

    }

    public void openLaughTest(View v){
        Intent i=new Intent(selectAction.this,SelectRecordingType.class);
        i.putExtra("action", "laugh");
        startActivity(i);
    }

    public void openTestWithInteract(View v){
        Intent i =new Intent(selectAction.this,ActivityInteract.class);
        startActivity(i);
    }


}
