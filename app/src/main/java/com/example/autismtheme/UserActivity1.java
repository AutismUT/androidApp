package com.example.autismtheme;


//import AILab.tests.testdropdown.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.sax.TextElementListener;
import android.telephony.TelephonyManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class UserActivity1 extends Activity implements OnItemSelectedListener {

    //Variable definition

    //for saving subscriber ID (phone number)
    String myIMSI;

    //necessary object for taking subscriber ID (phone number)
    TelephonyManager tele;

    //created as keys for shared preferences
    String key_userinfo = "User Info1";
    //created as values inside shared preference
    String key_age = "Age Key";
    String key_gender = "Gender Key";
    String key_background = "Background Key";
    String key_phonenum = "Phone Num";
    String key_autistic = "Autistic Key";

    //shared preferences for saving user info for further use.
    SharedPreferences UserInfo;
    //editor for saving user info in the shared preference
    Editor editor_userinfo;

    AlertDialog.Builder alertDialog;

    Integer Year;
    //Views used for Dropdowns
    Spinner dropdown_gender;
    Spinner dropdown_background;
    Button store;
    Spinner dropdown_autistic;

    //EditText for entering phone number manually
    EditText edittext_phonenum;
    EditText day;
    EditText age;
    EditText month;

    String upload;
    String fileName;


    //for showing warning on exit
    boolean unSavedChanges = true;

    //birthday warning has been shown
    boolean isWarningShown = false;
//    View.OnFocusChangeListener vof = new View.OnFocusChangeListener() {

//        private void checkRange(Integer min, Integer max, EditText e) {
//            try {
//                int input = Integer.parseInt(e.getText().toString());
//                if (input < min) {
//                    main_activity.sendToast("تاریخ تولد وارد شده معتبر نبود", UserActivity1.this);
//                    e.setText("");
//                } else if (input > max) {
//                    e.setText("");
//                    main_activity.sendToast("تاریخ تولد وارد شده معتبر نبود", UserActivity1.this);
//                }
//
//            } catch (Exception ex) {
//                e.setText("");
//            }
//        }
//
//
//        @Override
//        public void onFocusChange(View v, boolean hasFocus) {
//            if (hasFocus && isWarningShown == false) {
//                showBirthdayWarning();
//                isWarningShown = true;
//            }
//            if (!hasFocus) {
//                switch (v.getId()) {
//                    case R.id.day:
//                        checkRange(1, 31, (EditText) v);
//                        break;
//                    case R.id.month:
//                        checkRange(1, 12, (EditText) v);
//                        break;
//                    case R.id.age:
//                        checkRange(Year-5,Year, (EditText) v);
//                        break;
//                }
//            }
//        }
//    };

//	private void doExit() {
//
//		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
//		dlgAlert.setMessage("اطلاعات شما هنوز ثبت نشده آیا میخواهید خارج شوید؟");
//		dlgAlert.setPositiveButton("بله", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				finish();
//			}
//		});
//		dlgAlert.setNegativeButton("خیر",null);
//		dlgAlert.setCancelable(false);
//		dlgAlert.create().show();
//	}
    TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            unSavedChanges = true;
        }
    };

    private void showBirthdayWarning() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("لطفا در انتخاب تاریخ تولد دقت کنید");
        dlgAlert.setTitle("هشدار");
        dlgAlert.setPositiveButton("باشه", null);
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


        UserInfo = getApplicationContext().getSharedPreferences(key_userinfo, Context.MODE_MULTI_PROCESS);
        editor_userinfo = UserInfo.edit();

        Log.e("userInfo"," is "+UserInfo.getAll().toString());


        setContentView(R.layout.user_page1);

        alertDialog = new AlertDialog.Builder(UserActivity1.this)
                .setTitle("هشدار")
                .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                    }
                });

        day = (EditText) findViewById(R.id.day);
        month = (EditText) findViewById(R.id.month);
        age = (EditText) findViewById(R.id.age);

        store = (Button) findViewById(R.id.storeData);
        store.setTypeface(CustomFontsLoader.getTypeface(this));

        day.addTextChangedListener(tw);
        month.addTextChangedListener(tw);
        age.addTextChangedListener(tw);
        if(getIntent().getExtras()!=null) {
            upload = getIntent().getExtras().getString("upload");
            fileName = getIntent().getExtras().getString("fileName");
        }
        //completing declaration of Shared Preference: User Info

        edittext_phonenum = (EditText) findViewById(R.id.editText_phoneNum);
        edittext_phonenum.addTextChangedListener(tw);
        ////////////Getting IMSI\\\\\\\\\\\\
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = manager.getLine1Number();
//		if(UserInfo.getString(key_phonenum,null)==null) {
//			if (phoneNumber != null) {
//				main_activity.sendToast(phoneNumber, this);
//				EditText et = (EditText) findViewById(R.id.editText_phoneNum);
//				et.setText(phoneNumber);
//				editor_userinfo = UserInfo.edit();
//				editor_userinfo.putString(key_phonenum, phoneNumber);
//				editor_userinfo.commit();
//			}
//		}

        JalaliCalendar jc = new JalaliCalendar();
        String date = jc.getJalaliDate(new Date(System.currentTimeMillis()));
        Year = Integer.parseInt(date.split("/")[0]);
        Log.e("year"," is "+Year);

        ////////////////Creating Dropdowns\\\\\\\\\\\\\\\\
        ////// Saving the phone number after the user has finished entering it\\\\\


        ///////DropDown Gender\\
        dropdown_gender = (Spinner) findViewById(R.id.spinner_gender);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.GenderValues, R.layout.spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_gender.setAdapter(adapter2);
        dropdown_gender.setOnItemSelectedListener(this);

        ///////DropDown Background\\\\\\\\\\\
        dropdown_background = (Spinner) findViewById(R.id.spinner_background);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.BackgroundValues, R.layout.spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_background.setAdapter(adapter3);
        dropdown_background.setOnItemSelectedListener(this);

        ///////DropDown Autistic\\\\\\\\\\\
        dropdown_autistic = (Spinner) findViewById(R.id.spinner_autistic);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.AutisticValues, R.layout.spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_autistic.setAdapter(adapter4);
        dropdown_autistic.setOnItemSelectedListener(this);

    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        //retrieving birthday

        try {
            String birthday = (UserInfo.getString(key_age, ""));
            if (birthday != "") {
                isWarningShown = true;
                String[] birth = birthday.split("/");
                age.setText(birth[0]);
                month.setText(birth[1]);
                day.setText(birth[2]);
            }


        } catch (ClassCastException d) {
            Log.d("!!!!", "there is a preference with this name that is not an int");
        } catch (Exception ex) {
            dropdown_gender.setSelection(0);
        }

        //retrieving gender
        try {
            dropdown_gender.setSelection(UserInfo.getInt(key_gender, 0));


        } catch (ClassCastException d) {
            Log.d("!!!!", "there is a preference with this name that is not an int");
        } catch (Exception ex) {
            dropdown_gender.setSelection(0);
        }

        //retrieving Background
        try {
            dropdown_background.setSelection(UserInfo.getInt(key_background, 0));
        } catch (ClassCastException d) {
            Log.d("!!!!", "there is a preference with this name that is not an int");
        } catch (Exception ex) {
            dropdown_background.setSelection(0);
        }

        //retrieving autistic
        try {
            dropdown_autistic.setSelection(UserInfo.getInt(key_autistic, 0));


        } catch (ClassCastException d) {
            Log.d("!!!!", "there is a preference with this name that is not an int");
        } catch (Exception ex) {
            dropdown_autistic.setSelection(0);
        }

        String number = UserInfo.getString(key_phonenum, null);
        if (number != null) {

            edittext_phonenum.setText(number);
        }


    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        //saves the current view states
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restores previous view states
        super.onRestoreInstanceState(savedInstanceState);
    }


//    public void checkUnSetValues() {
//        if (UserInfo.getInt(key_gender, -1) == -1)
//            editor_userinfo.putInt(key_gender, 0);
//        if (UserInfo.getInt(key_background, -1) == -1)
//            editor_userinfo.putInt(key_background, 0);
//        if (UserInfo.getInt(key_autistic, -1) == -1)
//            editor_userinfo.putInt(key_autistic, 0);
//        editor_userinfo.commit();
//    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {

        //editor for saving user info in the shared preference


        switch (arg0.getId()) {
            case (R.id.spinner_gender): {
                unSavedChanges = true;
                Log.d("!!!!", "onItemSelected-spinner_gender Ran");//for debug
                break;

            }
            case (R.id.spinner_background): {
                unSavedChanges = true;
                Log.d("!!!!", "onItemSelected-spinner_bkgnd Ran");//for debug
                break;

            }
            case (R.id.spinner_autistic): {
                unSavedChanges = true;
                Log.d("!!!!", "onItemSelected-spinner_autistic Ran");//for debug
                break;

            }

        }
    }







    private boolean checkValidation(){
        try {
            int ageNumber = Integer.parseInt(age.getText().toString());
            int monthNumber = Integer.parseInt(month.getText().toString());
            int dayNumber = Integer.parseInt(day.getText().toString());
            if(monthNumber > 12 || monthNumber < 1 || dayNumber < 1 || dayNumber > 31){
                alertDialog.setMessage("تاریخ تولد صحیح نیست").show();
                return false;
            }
            if (ageNumber < Year-5 || ageNumber > Year) {
                alertDialog.setMessage("سن کودک باید کمتر از ۵ سال باشد").show();
                return false;
            }
        } catch (NumberFormatException nfe) {
            alertDialog.setMessage("تاریخ تولد صحیح یا کامل نیست").show();

            return false;
        }


        if (edittext_phonenum.getText().toString().length() < 11) {
            alertDialog.setMessage("شماره تلفن معتبر نیست").show();
            return false;
        }
        try {
            Double.parseDouble(edittext_phonenum.getText().toString());
        } catch (NumberFormatException exception) {
            alertDialog.setMessage("شماره تلفن معتبر نیست").show();
            return false;
        }
        return true;
    }

    private void saveData() {

        String birthDay =  age.getText().toString() + "/" + month.getText().toString()
                + "/" + day.getText().toString();

        if(!birthDay.equals(UserInfo.getString(key_age,""))){
            editor_userinfo.putString(key_age,birthDay);
        }
        if(!edittext_phonenum.getText().toString().equals(UserInfo.getString(key_phonenum,null))) {
            editor_userinfo.putString(key_phonenum, edittext_phonenum.getText().toString());
        }
        if(!(dropdown_gender.getSelectedItemPosition()==UserInfo.getInt(key_gender,-1))) {
            editor_userinfo.putInt(key_gender, dropdown_gender.getSelectedItemPosition());
        }
        if(!(dropdown_autistic.getSelectedItemPosition()==UserInfo.getInt(key_autistic,-1))) {
            editor_userinfo.putInt(key_autistic, dropdown_autistic.getSelectedItemPosition());
        }
        if(!(dropdown_gender.getSelectedItemPosition()==UserInfo.getInt(key_gender,-1))) {
            editor_userinfo.putInt(key_gender, dropdown_gender.getSelectedItemPosition());
        }
        if(!(dropdown_background.getSelectedItemPosition()==UserInfo.getInt(key_background,-1))) {
            editor_userinfo.putInt(key_background, dropdown_background.getSelectedItemPosition());
        }
        editor_userinfo.apply();
    }

    private boolean dataChanged(){
        boolean temp = false;

        String birthDay =  age.getText().toString() + "/" + month.getText().toString()
                + "/" + day.getText().toString();

        if(!birthDay.equals(UserInfo.getString(key_age,""))){
            temp = true;
        }
        if(!edittext_phonenum.getText().toString().equals(UserInfo.getString(key_phonenum,null))) {
            temp = true;
        }
        if(!(dropdown_gender.getSelectedItemPosition()==UserInfo.getInt(key_gender,-1))) {
            temp = true;
        }
        if(!(dropdown_autistic.getSelectedItemPosition()==UserInfo.getInt(key_autistic,-1))) {
            temp = true;
        }
        if(!(dropdown_gender.getSelectedItemPosition()==UserInfo.getInt(key_gender,-1))) {
            temp = true;
        }
        if(!(dropdown_background.getSelectedItemPosition()==UserInfo.getInt(key_background,-1))) {
            temp = true;
        }
        return temp;
    }

    private void showWarningDialoge() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("گزینه مورد نظر خود را انتخاب کنید");
        dlgAlert.setPositiveButton("در حال وارد کردن اطلاعات کودک جدید هستم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor_userinfo.remove("ID Key").apply();
                saveData();
                uploadFile();
            }
        });
        dlgAlert.setNegativeButton("در حال تصحیح اطلاعات کودک قبلی هستم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent in = new Intent(UserActivity1.this, EditUserInfo.class);
                in.putExtra("childNum", "1");
                UserActivity1.this.startService(in);
                saveData();
                uploadFile();
            }
        });
        dlgAlert.create().show();
    }

    private void uploadFile(){
        if(upload!= null && upload.equals("1")) {
            Intent intent = new Intent(this, UploadService.class);
            intent.putExtra("fileName", fileName);
            startService(intent);
        }
        finish();
    }



    public void storeData(View v) {
        if(!checkValidation()){
            return;
        }
        if(dataChanged()) {
            String key_id = "ID Key";
            String id = UserInfo.getString(key_id, null);
            if (id != null) {
                showWarningDialoge();
            } else {
                saveData();
                uploadFile();
            }
            Log.e("all keys"," is "+UserInfo.getAll().toString());
        }
        else {
            finish();
        }

//


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}