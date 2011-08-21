package com.example.hackU;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class YoutubeResults extends Activity {
	private LayoutInflater mInflater;
	private Vector<RowData> data;
	private Context context;

	RowData rd;
	private CustomAdapter adapter;
	private String[] ntitles = new String[10];
	private String[] nUrls = new String[10];
	private String[] thumbnail = new String[10];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);
		this.context = this;
		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		Intent i = getIntent();
		nUrls = i.getStringArrayExtra("url");
		ntitles = i.getStringArrayExtra("title");
		thumbnail = i.getStringArrayExtra("thumbnail");
		data = new Vector<RowData>();
		for(int j=0;j<ntitles.length;j++){
			try {
				rd = new RowData(j,ntitles[j],nUrls[j]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			data.add(rd);
		}
		this.adapter = new CustomAdapter(this, R.layout.youtubelist,R.id.title, data);
		ListView list = ((ListView)findViewById(R.id.list));
		list.setAdapter(this.adapter);
		//getListView().setTextFilterEnabled(true);
		//}
		list.setOnItemClickListener(new OnItemClickListener() {

			//public void onListItemClick(ListView parent, View v, int position,long id) {   
			public void onItemClick(AdapterView<?> arg0, View v, int position,long id) {

				//	}
				//Toast.makeText(getApplicationContext(), "You have selected "
				//	+(position+1)+"th item",  Toast.LENGTH_SHORT).show();
				System.out.println("Clicked news item");
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(nUrls[position]));
				startActivity(browserIntent);
				//Intent i = new Intent(context, WikiShow.class);
				//i.putExtra("htmlcontent",Uri.parse(nUrls[position]).toString());
				//startActivity(i);
			}
		});
	}
	private class RowData {
		protected int mId;
		protected String mTitle;
		protected String mDetail;
		RowData(int id,String title,String detail){
			mId=id;
			mTitle = title;
			mDetail=detail;
		}
		@Override
		public String toString() {
			return mId+" "+mTitle+" "+mDetail;
		}
	}
	private class CustomAdapter extends ArrayAdapter<RowData> {
		public CustomAdapter(Context context, int resource,int textViewResourceId, List<RowData> objects) {               
			super(context, resource, textViewResourceId, objects);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {   
			ViewHolder holder = null;
			TextView title = null;
			TextView detail = null;
			ImageView i11=null;
			RowData rowData= getItem(position);
			if(null == convertView){
				convertView = mInflater.inflate(R.layout.youtubelist, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			title = holder.gettitle();
			title.setText(rowData.mTitle);
			detail = holder.getdetail();
			detail.setText(rowData.mDetail);                                                     
			i11=holder.getImage();
			Drawable image = ImageOperations(thumbnail[position]);

			i11.setImageDrawable(image);
			//i11.setImageResource(imgid[rowData.mId]);
			return convertView;
		}
		private class ViewHolder {
			private View mRow;
			private TextView title = null;
			private TextView detail = null;
			private ImageView i11=null; 
			public ViewHolder(View row) {
				mRow = row;
			}
			public TextView gettitle() {
				if(null == title){
					title = (TextView) mRow.findViewById(R.id.title);
				}
				return title;
			}     
			public TextView getdetail() {
				if(null == detail){
					detail = (TextView) mRow.findViewById(R.id.detail);
				}
				return detail;
			}
			public ImageView getImage() {
				if(null == i11){
					i11 = (ImageView) mRow.findViewById(R.id.img);
				}
				return i11;
			}
		}
	}
	public Object fetch(String address) throws MalformedURLException,
	IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}

	private Drawable ImageOperations( String url1) {
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


}