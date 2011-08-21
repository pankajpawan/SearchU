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
import android.widget.ImageView;

public class FullScreen extends Activity {
	private Context context;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);
        context = this;
        Intent i = getIntent();
        String url = i.getStringExtra("url");
        Drawable image = ImageOperations(url);
        ImageView img = (ImageView)findViewById(R.id.img);
        img.setImageDrawable(image);
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
}
