package com.example.hackU;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WikiShow extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView webview = new WebView(this);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setPluginsEnabled(true);
		webview.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url){
		        view.loadUrl(url);
		        return false;
		   }
		});
		setContentView(webview);
		Intent i = getIntent();
		String value = i.getStringExtra("htmlcontent");
		webview.loadUrl(value);
	}
}
