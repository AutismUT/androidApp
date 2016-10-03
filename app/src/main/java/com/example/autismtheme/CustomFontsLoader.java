package com.example.autismtheme;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by sajad on 8/4/16.
 */
public class CustomFontsLoader {
    private static boolean fontsLoaded = false;
    static Typeface font;
    //avoid loading fonts for each textView
    public static Typeface getTypeface(Context context) {
        if (!fontsLoaded) {
            font = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansLight.ttf");
            fontsLoaded = true;
        }
        return font;

    }
}
