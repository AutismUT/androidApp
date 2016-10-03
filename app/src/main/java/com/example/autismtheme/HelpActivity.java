package com.example.autismtheme;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;

public class HelpActivity extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_page);
		ArrayList<ItemHelp> items = prepareData();
		AdapterHelp adapterHelp = new AdapterHelp(this,items);
		ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandable_list_view);
		expandableListView.setAdapter(adapterHelp);
	}

	public ArrayList<ItemHelp> prepareData(){
		ArrayList<ItemHelp> items = new ArrayList<>();
		items.add(new ItemHelp(R.drawable.slide1,R.string.slide1,"صفحه اول برنامه"));
		items.add(new ItemHelp(R.drawable.slide2,R.string.slide2,"مشخصات کاربر"));
		items.add(new ItemHelp(R.drawable.slide3,R.string.slide3,"تست صدا"));
		items.add(new ItemHelp(R.drawable.slide4,R.string.slide4,"ضبط صدای گریه"));
		items.add(new ItemHelp(R.drawable.slide8,R.string.slide8,"تایید و بررسی صدا"));
		items.add(new ItemHelp(R.drawable.slide5,R.string.slide5,"تعامل با والدین در حین گریه"));
		items.add(new ItemHelp(R.drawable.slide7,R.string.slide6,"تعامل با سیستم در حین گریه"));
		items.add(new ItemHelp(R.drawable.slide6,R.string.slide7,"تست خنده"));
		items.add(new ItemHelp(R.drawable.slide9,R.string.slide9,"تست تعامل"));

		return items;
	}
}
