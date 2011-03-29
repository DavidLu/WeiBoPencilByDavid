package com.doubleloop.weibopencil;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class PencilActivity extends Activity 
implements ColorPickerDialog.OnColorChangedListener{
	
	PencilView pv;
	
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
    	pv = new PencilView(this);
    	pv.background = bm;
    	//ImageView iv = new ImageView(this);
    	//iv.setImageDrawable(d);
    	setContentView(pv);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.pencilmenu, menu);
	    return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        mPaint.setXfermode(null);
//        mPaint.setAlpha(0xFF);
//
        switch (item.getItemId()) {
            case R.id.menu_change_color:
            	if(pv!=null){
            		new ColorPickerDialog(this, this, pv.getPencilPaint().getColor()).show();
            	}
            	else{
            		Toast.makeText(this, "PencilView is null", Toast.LENGTH_SHORT).show();
            	}
            	//
                return true;
            	
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void colorChanged(int color) {
		if(pv!=null){
			pv.setPencilPaintColor(color);
		}
		
	}
}
