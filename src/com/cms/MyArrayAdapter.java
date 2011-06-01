package com.cms;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] names;
	private ArrayList<String> blist;//??
	private int interval;//number of items{images,head text,body text}
	

	    
	 private final ImageDownloader imageDownloader = new ImageDownloader();
	   
	
	public MyArrayAdapter(Activity context, String[] names, ArrayList<String> alist, int i) {
		super(context, R.layout.rowlayout, names);
		this.context = context;
		this.names = names;
		
		this.blist=alist;//??
		this.interval=i;//??
		
	}

	// static to save the reference to the outer class and to avoid access to
	// any members of the containing class
	static class ViewHolder {
		public ImageView imageView;
		public TextView textView;
		public TextView textHead;//??
		public TextView textBody;//??
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ViewHolder will buffer the assess to the individual fields of the row
		// layout
		ViewHolder holder;//??
		
		
		// Recycle existing view if passed as parameter
		// This will save memory and time on Android
		// This only works if the base layout for all classes are the same
		View rowView = convertView;
		
			
			if (rowView == null) {
				
				LayoutInflater inflater = context.getLayoutInflater();
				rowView = inflater.inflate(R.layout.rowlayout, null, true);
				holder = new ViewHolder();
				
				holder.textHead = (TextView) rowView.findViewById(R.id.heading);
				
				holder.textBody=(TextView) rowView.findViewById(R.id.body); 
				
				holder.imageView = (ImageView) rowView.findViewById(R.id.icon);
			
				//send imageView to download function for setting its value 
				
				rowView.setTag(holder);
				//rowView.setBackgroundColor(0);
				
			} else {
				holder = (ViewHolder) rowView.getTag();
			}
			
		/**
		 * 0,1,2;3,4,5;
		 * */
		//if((position/3)*3==(position*3)/3 ){//check for interval of 3
				
			holder.textHead.setText(blist.get(position*interval+1).toString());
			holder.textBody.setText(blist.get(position*interval+2).toString());
			
			String url=(String)blist.get(position*interval+3).toString();
			imageDownloader.download(url, holder.imageView);//??
			
			//imageDownloader.download(URLS[position], holder.imageView);//??
			

		
		return rowView;
		
	}
}
