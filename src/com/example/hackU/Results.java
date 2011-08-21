/**
 * 
 */
package com.example.hackU;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author pankaj
 *
 */
public class Results extends Activity {
	private ImageAdapter adapter;
	private Context context;
	private int width, height;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gallery);
		context = this;
		this.adapter = new ImageAdapter(this,HelloActivity.searchUrl);//), R.layout.item/*items_list_item*/, ItemManager.getLoadedItems());
		((ListView)findViewById(R.id.list)).setAdapter(this.adapter);
		ListView list = (ListView) findViewById(R.id.list);
		height = getWindowManager().getDefaultDisplay().getHeight();
		width = getWindowManager().getDefaultDisplay().getWidth();
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				String url1 = HelloActivity.fullImage[position];
				
				//Drawable image = ImageOperations(url1);
//				ImageView iv = new ImageView(context);
//				
//				iv.setImageDrawable(image);
//				iv.setAdjustViewBounds(true);
//				iv.setMaxHeight(height/3);
//
//				iv.setMaxWidth(width);
//				iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				Intent i = new Intent(context, FullScreen.class);
				i.putExtra("url", url1);
	    		startActivity(i);
				
			}
		});
	}

	private class ImageAdapter extends BaseAdapter {

		private Context mcontext;//[] items;
		private String[] searchUrl = new String[10];
		private int height, width;
		private ImageView[] images = new ImageView[10];
		public ImageAdapter(Context context, String[] searchUrl) {
			//super(context, textViewResourceId, items);
			this.mcontext = context;
			this.searchUrl = searchUrl;
			height = getWindowManager().getDefaultDisplay().getHeight();
			width = getWindowManager().getDefaultDisplay().getWidth();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//ImageView Image01 = null;
			if(images[position]!=null)return images[position];
			
			try{
				String url1 = searchUrl[position];     
				images[position] = new ImageView(mcontext);
				Drawable image = ImageOperations(url1);
				
				images[position].setImageDrawable(image);
				System.out.println(images[position].getHeight());
				images[position].setAdjustViewBounds(true);
				images[position].setMaxHeight(height/3);
				System.out.println(images[position].getHeight());

				images[position].setMaxWidth(width);
				images[position].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				//images[position].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				
				//Image01.setLayoutParams();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			return images[position];
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return searchUrl.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

	}
	public Object fetch(String address) throws MalformedURLException,
	IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}  

	private Drawable ImageOperations(String url1) {
		// TODO Auto-generated method stub

		try {
			InputStream is = (InputStream) this.fetch(url1);
			Drawable d = Drawable.createFromStream(is, "src");
			
			return d;
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}

	}
	//@Override
	//protected void onListItemClick(ListView l, View v, int position, long id) {
	//	this.adapter.getItem(position).click(this.getApplicationContext());
	//}
}
