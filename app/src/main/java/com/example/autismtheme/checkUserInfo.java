package com.example.autismtheme;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sajad on 5/18/16.
 */
public class checkUserInfo {


    //checking the completeness of the profile before sending data
    public static boolean isUserInfoComplete(Context context,String key_userinfo){
        String key_age="Age Key";
        String key_gender="Gender Key";
        String key_background="Background Key";
        String key_phonenum="Phone Num";
        String key_autistic="Autistic Key";
        SharedPreferences UserInfo =context.getSharedPreferences(key_userinfo, Context.MODE_MULTI_PROCESS);
        String birthiday = (UserInfo.getString(key_age, null));
        int gender = (UserInfo.getInt(key_gender, -1));
        int background = (UserInfo.getInt(key_background, -1));
        String phoneNum = (UserInfo.getString(key_phonenum, null));
        int autistic = (UserInfo.getInt(key_autistic, -1));
        if(birthiday == null || gender == -1 || background == -1 || phoneNum == null||autistic == -1) {
            return false;
        }
        return true;
    }

}
