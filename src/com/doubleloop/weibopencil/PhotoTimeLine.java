package com.doubleloop.weibopencil;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import weibo4andriod.Paging;
import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.AccessToken;
import weibo4andriod.http.RequestToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PhotoTimeLine extends Activity {
	
	private List<Status> imagesTimeLine;
	private int picwidth;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phototimeline); 
		
		//Validate if we are in.
		Uri uri=this.getIntent().getData();
		try {
			RequestToken requestToken= OAuthConstant.getInstance().getRequestToken();
			AccessToken accessToken=requestToken.getAccessToken(uri.getQueryParameter("oauth_verifier"));
			OAuthConstant.getInstance().setAccessToken(accessToken);
			//TextView textView = (TextView) findViewById(R.id.TextView01);
			//textView.setText("得到AccessToken的key和Secret,可以使用这两个参数进行授权登录了.\n Access token:\n"+accessToken.getToken()+"\n Access token secret:\n"+accessToken.getTokenSecret());
		} catch (WeiboException e) {
			e.printStackTrace();
			return;
		}
		
		//Fetch FriendsTimeline
		Weibo weibo=OAuthConstant.getInstance().getWeibo();
		weibo.setToken(OAuthConstant.getInstance().getToken(), OAuthConstant.getInstance().getTokenSecret());
		List<Status> friendsTimeline;
		imagesTimeLine = new ArrayList<Status>();
		try {
			for (int i = 1; i < 4; i++) {
				Paging paging = new Paging(i);
				friendsTimeline = weibo.getFriendsTimeline(paging);
				//StringBuilder stringBuilder = new StringBuilder("1");
				for (Status status : friendsTimeline) {
					//stringBuilder.append(status.getUser().getScreenName() + "说:"
					//		+ status.getText() + "\n");
					if (status.getThumbnail_pic().length() != 0) {
						imagesTimeLine.add(status);
					}
				}
			}
			//TextView textView = (TextView) findViewById(R.id.TextView01);
			//textView.setText(stringBuilder.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
			return;
		}
		
				
		GridView g = (GridView) findViewById(R.id.myGrid);
		g.setAdapter(new ImageAdapter(this));
		
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		picwidth = dm.widthPixels / 3 - 4;
	}
	//Disply
	class ImageAdapter extends BaseAdapter {
        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return imagesTimeLine.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }
        
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(picwidth, picwidth));
                imageView.setAdjustViewBounds(false);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(1, 1, 1, 1);
            } else {
                imageView = (ImageView) convertView;
            }

            try {
            	Status s = imagesTimeLine.get(position);
				URL thumb_u = new URL(s.getThumbnail_pic()); 
				Drawable drawable = Drawable.createFromStream(thumb_u.openStream(), "src");
				imageView.setImageDrawable(drawable);
			}
			catch (Exception ex){
				
			}
			
			imageView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(PhotoTimeLine.this, PencilActivity.class);
					Bundle bundle = new Bundle();
					Status s = imagesTimeLine.get(position);
					bundle.putString("link", s.getBmiddle_pic());
					intent.putExtras(bundle);
					startActivity(intent);
				}});

            return imageView;
        }

        private Context mContext;
        
	}

				

	
}
