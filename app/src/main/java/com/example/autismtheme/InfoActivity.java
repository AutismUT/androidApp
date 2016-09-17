package com.example.autismtheme;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class InfoActivity extends Activity{

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_page);
		ImageView logo1 = (ImageView)findViewById(R.id.image_view_logo1);
		Glide.with(this).load(R.drawable.logo2).into(logo1);
		ImageView logo2 = (ImageView)findViewById(R.id.image_view_logo2);
		Glide.with(this).load(R.drawable.logo3).into(logo2);
		ImageView logo3 = (ImageView)findViewById(R.id.image_view_logo3);
		Glide.with(this).load(R.drawable.logo1).into(logo3);
	}

	public void visibleMoreItems(View v){
		TextView textView3 = (TextView)findViewById(R.id.textView3);
		TextView textView4 = (TextView)findViewById(R.id.textView4);
		TextView textView5 = (TextView)findViewById(R.id.textView5);
		textView3.setVisibility(View.VISIBLE);
		textView4.setVisibility(View.VISIBLE);
		textView5.setVisibility(View.VISIBLE);
		v.setVisibility(View.GONE);
	}
}
