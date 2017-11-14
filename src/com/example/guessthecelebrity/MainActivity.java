package com.example.guessthecelebrity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	ImageView myimg;
	Button b1;
	Button b2;
	Button b3;
	Button b4;
	
	Button play;
	HashMap<String,String> map;
	ArrayList<Button> buttons;
	
	ArrayList<Integer> tempran;
	
	List<String> keys;
	int mapsize;
	int mapcounter = 0;
	
    String d;
    String answer="";
	
    Random ran;
    List<String> objects;
	
	public class DownloadImage extends AsyncTask<String,Void,Bitmap>{

		@Override
		protected Bitmap doInBackground(String... urls) {
			try
			{
			URL url = new URL(urls[0]);
			
		HttpURLConnection	connection = (HttpURLConnection)
					url.openConnection();
			
			connection.connect();
			InputStream is  = connection.getInputStream();

			Bitmap image = BitmapFactory.decodeStream(is);
			
			Log.i("Working",String.valueOf(connection.getResponseCode()));
 
			return image;
			
			}
			
			catch(Exception e){
				
			}
			
			return null;
		}
		
		
		
	}
	
	public class DownloadTask extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... urls) {
			try{

String result="";				
URL url = new URL(urls[0]);
			
HttpURLConnection connection = (HttpURLConnection)
url.openConnection();


//result = "Code is : "+connection.getResponseCode();

InputStream is  = connection.getInputStream();

//InputStreamReader isr = new InputStreamReader(is);

BufferedReader buffer = new BufferedReader(
       new InputStreamReader(is));

//int data = isr.read();

String data = buffer.readLine();

while(data!=null){
	
// char current = (char) data;
 
 result = result + data;
 
 //data = isr.read();
 data = buffer.readLine();
	
}



return result;
						
			}
			catch(Exception e){
				
				
			}
			
			
			
			
			return null;
		}
		
		
		
		
		
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ran = new Random();
	   objects = new ArrayList<String>();
	   
	   tempran = new ArrayList<Integer>();
		
		myimg = (ImageView)findViewById(R.id.imageView1);
		b1 = (Button)findViewById(R.id.button1);
		b2 = (Button)findViewById(R.id.button2);
		b3 = (Button)findViewById(R.id.button3);
		b4 = (Button)findViewById(R.id.button4);
		
		play = (Button)findViewById(R.id.button5);
		
		play.setVisibility(View.INVISIBLE);
		
		map = new HashMap<String,String>();
		
		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		b3.setOnClickListener(this);
		b4.setOnClickListener(this);
		
		play.setOnClickListener(this);
		
		buttons = new ArrayList<Button>();
	    buttons.add(b1);
	    buttons.add(b2);
	    buttons.add(b3);
	    buttons.add(b4);
		
	try{
			
DownloadTask dwt = new DownloadTask();

String res = dwt.execute("https://celebritybucks.com/developers/export/JSON?limit=15").get();
					
Log.i("Working2",res);

JSONObject jobj = new JSONObject(res);

String celebvalue = jobj.getString("CelebrityValues");

//Log.i("Celeb values",celebvalue);

JSONArray jarr  = new JSONArray(celebvalue);

for(int i=0;i<jarr.length();i++){
	
	JSONObject j1 = jarr.getJSONObject(i);
	
	map.put(j1.getString("celebId"), j1.getString("name"));
	
	//Log.i("celebid",j1.getString("celebId"));
	//Log.i("name",j1.getString("name"));
	
	
}
Log.i("Map size",String.valueOf(map.size()));

keys = new ArrayList<String>(map.keySet());

shuffleKeySet();

DownloadImage dwi = new DownloadImage();
modifyButtonText();

Bitmap img = dwi.execute("https://celebritybucks.com/images/celebs/full/"+d+".jpg").get();	
Log.i("Key",keys.get(mapcounter));

myimg.setImageBitmap(img);

	
					
	}
	catch(Exception e){
		
	}
		
		
	}

	private void modifyButtonText() {
		
		d=keys.get(mapcounter);
		answer = map.get(d);
		
		objects.clear();
		
		for(int k=0;k<map.size();k++){
			
			
			if(k!=mapcounter){
				
				tempran.add(k);
				
				
			}
			
			
		}
		
		Collections.shuffle(tempran);
		
		objects.add(answer);
		objects.add(map.get(keys.get(tempran.get(0))));
		objects.add(map.get(keys.get(tempran.get(1))));
		objects.add(map.get(keys.get(tempran.get(2))));
		
		tempran.clear();
		
		Collections.shuffle(objects);
		
		for (int i = 0; i < objects.size(); i++) {
		    buttons.get(i).setText(objects.get(i).toString());
		}
		
		mapcounter++;
	}

	private void shuffleKeySet() {
	
		
		Collections.shuffle(keys);
		
		
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {
		
		
		Button b = (Button)view;
		
		String btext = b.getText().toString();
		
		if(!(b.getText().toString().equals("Play Again"))){
			
		
		
		if(btext.equals(answer)){
			
			
			Toast.makeText(getApplicationContext(), "Correct"+" "+mapcounter, Toast.LENGTH_LONG).show();
		}
		
		else{
			
			Toast.makeText(getApplicationContext(), "InCorrect"+" "+mapcounter, Toast.LENGTH_LONG).show();	
		}
		
		
		try{
	    
			  if(mapcounter == 15){
			    	
			    	Toast.makeText(getApplicationContext(), "Quiz Done", Toast.LENGTH_LONG).show();	
			    	
			    	b1.setEnabled(false);
					b2.setEnabled(false);
					b3.setEnabled(false);
					b4.setEnabled(false);
					
					play.setVisibility(View.VISIBLE);
			    	
			    }
	    
	    if(mapcounter<15){
	    	
	    
		DownloadImage dwi1 = new DownloadImage();
		modifyButtonText();
     Bitmap bimg = dwi1.execute("https://celebritybucks.com/images/celebs/full/"+d+".jpg").get();
     myimg.setImageBitmap(bimg);
     
	    }
     
		}
		catch(Exception e){
			
			
		}
		
		
		}
		
		else{
			
			try
			{
			mapcounter =0;
			
			b1.setEnabled(true);
			b2.setEnabled(true);
			b3.setEnabled(true);
			b4.setEnabled(true);
			
			play.setVisibility(View.INVISIBLE);
	    	
			
			
			shuffleKeySet();

			DownloadImage dwip = new DownloadImage();
			modifyButtonText();

			Bitmap pimg = dwip.execute("https://celebritybucks.com/images/celebs/full/"+d+".jpg").get();	


			myimg.setImageBitmap(pimg);
			
			
			}
			
			catch(Exception e){
				
			}
			
		}
		
		
		
	}
}
