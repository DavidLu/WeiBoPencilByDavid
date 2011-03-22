package com.doubleloop.weibopencil;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class PencilActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = this.getIntent();
    	Bundle bundle = intent.getExtras();
    	
    	String link = bundle.getString("link");
    	Bitmap bm = null;
    	try {
    		URL aURL = new URL(link);
    		final URLConnection conn = aURL.openConnection();
            conn.connect();
            final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            bm = BitmapFactory.decodeStream(bis);
            bis.close();  
    	}
    	catch (Exception ex) { 
    		
    	}
    	PencilView pv = new PencilView(this);
    	pv.background = bm;
    	//ImageView iv = new ImageView(this);
    	//iv.setImageDrawable(d);
    	setContentView(pv);
	}

}
