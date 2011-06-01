//Main window class 
/*
 * CMS for a News Application for Android.
 * Inspired by BBC news, NYT news, Amazon.
 1-Get application content using DataBase[Options- XML file using pull parser, MySQL Database]
 1.1- Get Title Image, Heading and headlines[done]
 1.2- Get FullPage and images[working]
 2-Get title bar[Options- Company name,log,images][working]
 3-Get Header[Options- fixed,scrollable,tabs]
 4-Get HomePage Layout for headlines [Options- ListLayout, Scrollable pages]
  
 */

package com.cms;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

public class contentMS extends ListActivity {
	private dataProvider dp;//get part(screen section or table) content
    private int interval=4;//(number of text and image items in the ArrayList belonging to same story)
    
 	private ArrayList<String> alist=new ArrayList<String>();//??
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		/***Title bar test****/
	    /**Test
	     * For the below code to work in ListActivity
	     * 1-Must have setContentView() function below requestWindowFeature()
	     * 2-main layout must have a ListView item
	     * **/
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	    setContentView(R.layout.main);
	    this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //setContentView(R.layout.rowlayout);
	    //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

	    //Get Headlines
        dp= new dataProvider();
        alist= dp.getContent("headlines","");      		
		int count=alist.size();
		
		/***ArrayList list interval(number of text and image items belonging to same story)***/		
		String[] tt= new String[count/interval];//interval(fid,headline,text,images) array list
		
		/*size of tt will decide the number of list items
		divide the list into 3 
		*/
		this.setListAdapter(new MyArrayAdapter(this, tt ,alist,interval));
		
	
	}
	
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
//		Toast.makeText(this, "You selected: "+position, Toast.LENGTH_LONG)
//				.show();
    	Intent myIntent = new Intent(v.getContext(), fullPage.class);
    	Bundle b =new Bundle();
    	
    	//get full page id for DB access
    	String fid= alist.get(position*interval);//????
    	b.putString("fid",fid);
        myIntent.putExtras(b);//send headline id
    	
        startActivityForResult(myIntent, 0);
	}
    
   
}