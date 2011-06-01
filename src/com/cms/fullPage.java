package com.cms;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/***Activity for displaying full story 
 * 1-Get headline id hid of headline item
 * 2-Send this headline to dataProvider 
 * 3-Get ArrayList containing
 * {
 * 1-Heading text[Done]
 * 2-Images[1(done) or more images(??) at different locations]
 * 3-Body text[Done]
 * 4-More headline menu at bottom(??)
 * }
 * ***/
public class fullPage extends Activity{
	private ArrayList<String> flist;
	private dataProvider dp;

	final int ht=0;//heading text index
	final int bt=1;//body text index
	final int im=2;//image index in list
	
	//class for downloading images for fullpage
	private ImageDownloader imageDownloader = new ImageDownloader();

	
	//Constructor
	public fullPage(){
		flist= new ArrayList<String>();
	}

	public void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
	        
	        /***Title bar***/
	        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.fullpage_layout);
		    this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);


	        /***Get headline id(DB primary key)***/
			Bundle b= getIntent().getExtras();//get data
			String fid=b.getString("fid");
			
			/***Call dataProvider ***/
			dp= new dataProvider();
			flist=dp.getContent("fullpage",fid);
			
			 /***Divide into multiple strings
	         * 1-find index of special string
	         * 2-Extract substring from start to index
	         * 3-Repeat step 1 but starting from index
	         * 
	         * 
	         * ***/
	        //get full body text
	        String btxt= flist.get(bt).toString();
	        ArrayList<String> txtList=new ArrayList<String>();
		    int index=0;
		    int tem=0;
		    while(btxt.indexOf("%%")!=-1){
		    	//search for special string like %%
		    	index= btxt.indexOf("%%");//look for end of substring string
		    	String s1= btxt.substring(0,index);//take out sub string
		    	txtList.add(s1);//add to arraylist
		    	btxt=btxt.substring(index+2,btxt.length());//Reduce size of btxt
		    	//tem=index;
		    }
	        txtList.add(btxt);//add remaining text to the list
	        int txtcount=txtList.size();
	        /*******end of sub text*********/
	        
	        /****Extract image urls from image string****/
	        String imageurls=flist.get(im).toString();
	        ArrayList<String> imgurl= new ArrayList<String>();
	        while(imageurls.indexOf("%%")!=-1){
	        	index= imageurls.indexOf("%%");//look for end of substring string
		    	String s1= imageurls.substring(0,index);//take out sub string
		    	imgurl.add(s1);//add to arraylist
		    	imageurls=imageurls.substring(index+2,imageurls.length());//Reduce size of btxt
		    	
	        }
	        imgurl.add(imageurls);
	       
	        
	        /****End of extract image urls****/
	        
	        

	        /***Set heading, image, body etc..***/
	        TextView hText= (TextView)findViewById(R.id.heading);
	        ImageView imgV=(ImageView)findViewById(R.id.img);
	        
	        
	        
	        hText.setText(flist.get(ht).toString());
	        //bText.setText(marked_up.toString());

	        /***Set Heading Image in ImageView***/
	        /***Important leave no space between addresses***/
	        String url=imgurl.get(0).toString();
	        //String url="http://news.bbcimg.co.uk/media/images/52816000/jpg/_52816428_011996513-1.jpg";
	        imageDownloader.download(url,imgV);//??
			

	        /***Create multiple body TextViews and add ImageViews below TextView***/
	        //Relative layout object of fullpage_layout.xml 
	        LinearLayout lay= (LinearLayout)findViewById(R.id.rl);
	        //TextView tu= new TextView(this);
	        //tu.setId(0);//first TextView
	        for(int i=0;i<txtList.size();i++){
	        	/***Add TextView***/
	        	TextView tview= new TextView(this);
	        	

	        	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
	        	        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

	        	Spanned marked_up = Html.fromHtml(txtList.get(i).toString());//convert html to text
	        	
	        	//TextView Settings
	        	tview.setText(marked_up.toString());//set Text
	        	tview.setTextSize(15);//set text size
	        	tview.setPadding(2,2,2,2);//padding
	        	tview.setTextColor(-16777216);//black text
	        	lay.addView(tview,lp);//add new TextView
	        	
	        	/***Add ImageView***/
	        	if(i< imgurl.size()-1 ){//number of ImageView = (number of TextView -1)
		        	ImageView iview=new ImageView(this);
	
		        	LinearLayout.LayoutParams ipara = new LinearLayout.LayoutParams(
		        	        LinearLayout.LayoutParams.WRAP_CONTENT, 100);
		        	String u=imgurl.get(i+1).toString();//get second image (don't use heading image)
			       
		        	imageDownloader.download(u, iview);//add image over ImageView 
	
		        	lay.addView(iview,ipara);
	        	}
	        	
	        }
	        
	        /***End of adding multiple body TextViews***/
	        
	       // Spanned marked_up = Html.fromHtml(flist.get(bt).toString());
	        //((TextView) findViewById(R.id.text1)).setText(marked_up.toString());

	        
	}
}