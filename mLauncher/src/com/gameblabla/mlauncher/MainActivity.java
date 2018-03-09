package com.gameblabla.mlauncher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;

public class MainActivity extends Activity {
	private List<AppDetail> apps = new ArrayList<AppDetail>();
	private List<AppDetail> bookmarks = new ArrayList<AppDetail>();
	private ListView list; 
	private ArrayAdapter<AppDetail> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		//---------------------adapter setup--------------------//
		adapter = new ArrayAdapter<AppDetail>(this, R.layout.list_item, apps) {
	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            if(convertView == null){
	                convertView = getListItem();
	            }
	            
	            if(apps.size() > position){
		            TextView appLabel = (TextView)convertView.findViewById(R.id.item_app_label);
		            appLabel.setText(apps.get(position).label);
		            appLabel.setCompoundDrawablesWithIntrinsicBounds(null, null, apps.get(position).icon, null);
	            }
	             
	            return convertView;
	        }
	    };
		
		//-----------------------list setup--------------------//
		list = (ListView)findViewById(R.id.apps_list);
	    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
	        	launchApp(apps.get(pos).name.toString(), apps.get(pos).activity.toString());
	        }
	    });

	    list.setAdapter(adapter);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
	    //------------------------apps-------------------------------//
		PackageManager manager = getPackageManager();
	    
	    List<ResolveInfo> availableActivities = manager.queryIntentActivities(new Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER), 0);
	    for(ResolveInfo anActivity:availableActivities){
	        AppDetail app = new AppDetail();
	        app.label = anActivity.loadLabel(manager);
	        app.name = anActivity.activityInfo.packageName;
	        app.activity = anActivity.activityInfo.name;
	        Bitmap bitmap = ((BitmapDrawable) anActivity.activityInfo.loadIcon(manager)).getBitmap();
	        app.icon = new BitmapDrawable(this.getResources(), Bitmap.createScaledBitmap(bitmap, 70, 70, true));
	        apps.add(app);
	    }
	    
	    Collections.sort(apps, new Comparator<AppDetail>() {
			@Override
			public int compare(AppDetail lhs, AppDetail rhs) {
				return lhs.label.toString().compareToIgnoreCase(rhs.label.toString());
			}
		});
	    
	    adapter.notifyDataSetChanged();

	}
	
	@Override
	protected void onStop() 
	{
		super.onStop();
		
		//----------------free memory--------------------//
		apps.clear();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		return super.onKeyDown(keyCode, event);
	}

	protected void launchApp(String pkg, String cls){
    	Intent i = new Intent(Intent.ACTION_MAIN, null);
    	i.addCategory(Intent.CATEGORY_LAUNCHER);
		i.setComponent( new ComponentName(pkg, cls) );
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		startActivity(i);
	}
	
	
	@SuppressLint("InflateParams")
	protected View getListItem(){
		return getLayoutInflater().inflate(R.layout.list_item, null);
	}
}
