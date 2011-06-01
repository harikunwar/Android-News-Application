//dataProvider will fetch data content(text,image,headings,date,time etc) from MySql database
/*Form of data:-
 * 1-Title content(text,images url)
 * 2-Heading Content(text)
 * 3-Headline Content(text,images url)
 * */
package com.cms;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class dataProvider{
//	private String query;
	private ArrayList<String> content= new ArrayList<String>();//reuse content multiple times
	
	//constructors
	public dataProvider(){
	//	query=null;				
	}
	
	//Function to get Headline or Full page content
	//id is full page id
	public ArrayList<String> getContent(String part,String id){
		//content.clear();
		//get Text and Images
		content=getDB(part,id);
		
		return content;
	}
	
	
	//Function for getting db data
	private ArrayList<String> getDB(String part,String id){
		//content.clear();//Reusing a single ArrayList for saving memory 
    	
		InputStream is = null; 
        String result = "";
        //the year data to send
        
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("table",part));//table name= part e.g headlines
        
        //For pulling full page content we need full page id
        nameValuePairs.add(new BasicNameValuePair("key",id));//table name= part e.g headlines
        
        /***http post***/
        try{
			    HttpClient httpclient = new DefaultHttpClient();
		
		        HttpPost httppost = new HttpPost("http://www.kritisolutions.com/demo/cms/dbtest.php");
		
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));//send parameters

		        HttpResponse response = httpclient.execute(httppost);//get response
		
		        HttpEntity entity = response.getEntity();

                is = entity.getContent();

  			}catch(Exception e){
                Log.e("log_tag", "Error in http connection "+e.toString());
        }
  			  
        /***convert response to string****/
        try{
                
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                }
                is.close();
         
                result=sb.toString();
        }catch(Exception e){
                Log.e("log_tag", "Error converting result "+e.toString());
        }
         
        /***parse json data***/
        try{
                JSONArray jArray = new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        if(part=="headlines"){
	                        content.add(json_data.getString("fid"));//headline id
	                        content.add(""+json_data.getString("headline"));
	                        content.add(json_data.getString("text"));
	                        content.add(""+json_data.getString("images"));
                        } 
                        else{
                        	if(part=="fullpage"){
                        	    content.add(""+json_data.getString("heading"));
      	                        content.add(json_data.getString("textbody"));
      	                        content.add(""+json_data.getString("image"));
                             	
                        	}
                        	
                        }
                        
     
                }
        }
        catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
    }
        
        return content;//arraylist with all data
    }
}