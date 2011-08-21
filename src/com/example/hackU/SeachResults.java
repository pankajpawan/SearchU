package com.example.hackU;


import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SeachResults extends Activity  {
	private Context context;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchresults);
		Intent i = getIntent();
		query = i.getStringExtra("query");

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
				switch(position){
				case 0://fb
					facebook_feed();break;
				case 1: //flickr
				case 2: //wiki
				case 3: //twitter
				case 4: //youtube
				case 5: //newsfeed
				
				}
			}
		});
	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return mThumbIds.length;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			imageView.setImageResource(mThumbIds[position]);
			return imageView;
		}

		// references to our images
		private Integer[] mThumbIds = {
				R.drawable.fb, R.drawable.flickr,
				R.drawable.wiki, R.drawable.twitter,
				R.drawable.youtube, R.drawable.newsfeed
		};
	}
	String query= null;
public void facebook_feed(){
		
		final Facebook facebook = new Facebook("149549488464512");
		facebook.authorize(this, new DialogListener() {
			
			@Override
			public void onComplete(Bundle values) {
				try {
					Bundle parameters = new Bundle();
					//System.out.println(((EditText)findViewById(R.id.editText1)).getText().toString());
					parameters.putString("q", query/*((EditText)findViewById(R.id.editText1)).getText().toString()*/);
					parameters.putString("type", "page");
					parameters.putString("access_token",
							facebook.getAccessToken());
					parameters.putString("limit", "3");
					String str = facebook.request("search", parameters);
					JSONObject result = new JSONObject(str);
					JSONArray data = result.getJSONArray("data");
					String page_id, page, result_page_id = null;
					JSONObject page_object, result_page_object = null;
					int likes, max_likes = 0;
					for (int i = 0; i < data.length(); i++) {
						page_id = data.getJSONObject(i).getString("id");
						page = facebook.request(page_id);
						page_object = new JSONObject(page);
						likes = page_object.getInt("likes");
						if (max_likes < likes) {
							max_likes = likes;
							result_page_id = page_id;
							result_page_object = page_object;
						}
					}
					
					/*
					WebView view = new WebView(c);
					view.getSettings().setJavaScriptEnabled(true);
					view.getSettings().setLoadsImagesAutomatically(true);
					view.setWebViewClient(new WebViewClient() {
						public boolean shouldOverrideUrlLoading(WebView view,
								String url) {
							view.loadUrl(url);
							return false;
						}
					});
					view.loadUrl("http://www.facebook.com/" + result_page_id);
					*/
					System.out.println("http://www.facebook.com/" + result_page_id);
					Intent i = new Intent(context, WikiShow.class);
					i.putExtra("htmlcontent", "http://www.facebook.com/" + result_page_id);
					startActivity(i);
					System.out.println("ID: " + result_page_id + ", Likes: "
							+ max_likes);
	
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	
			@Override
			public void onFacebookError(FacebookError error) {
			}
	
			@Override
			public void onError(DialogError e) {
			}
	
			@Override
			public void onCancel() {
			}
	});
	}
}