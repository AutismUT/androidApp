package com.example.autismtheme;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.KeyEvent;

/**
 * Created by sajad on 8/25/16.
 */
public class DialogKeyListener implements android.content.DialogInterface.OnKeyListener
{
    Activity activity;
    public DialogKeyListener(Activity activity){
        this.activity = activity;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            dialog.dismiss();
             // or something like that
            activity.finish();
            return false;
        }
        return true;
    }
}
