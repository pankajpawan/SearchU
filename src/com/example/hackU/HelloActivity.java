package com.example.hackU;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

public class HelloActivity extends Activity {
	public static String searchUrl[] = new String[10];
	public static String fullImage[] = new String[10];

	String wikilink, twitterlink, fblink, movielink;
	Context c;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		c = this;
	}

	public void goSearch(View v) {
		fblink = null;
		twitterlink = null;
		wikilink = null;
		movielink = null;
		EditText et = (EditText) findViewById(R.id.editText1);
		Toast toast = Toast.makeText(getApplicationContext(), "Fetching data...",
				Toast.LENGTH_LONG);
		toast.show();
		String location = "";
		try {
			location = "http://query.yahooapis.com/v1/public/yql?q="
					+ "%20SELECT%20*%20FROM%20microsoft.bing.web%20WHERE%20query%3D%22"
					+ URLEncoder.encode(et.getText().toString(), "utf8")
					+ "%22%20|%20truncate(count%3D10)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			URL url = new URL(location);
			URLConnection uc = url.openConnection();
			// String encoded = Base64.encodeBase64String(new
			// String("rajam:Qwerty").getBytes());
			BufferedReader in = new BufferedReader(new InputStreamReader(uc
					.getInputStream()));
			String output = "", inputLine;
			while ((inputLine = in.readLine()) != null)
				// System.out.println(inputLine);
				output += inputLine;
			in.close();
			JSONObject temp = new JSONObject(output);
			temp = temp.getJSONObject("query").getJSONObject("results");
			JSONArray jarray = temp.getJSONArray("WebResult");
			for (int i = 0; i < jarray.length(); i++) {
				temp = jarray.getJSONObject(i);
				String s = temp.getString("Url");
				if (s.contains("wikipedia"))
					wikilink = s;
				else if (s.contains("http://twitter.com"))
					twitterlink = s;
				else if (s.contains("facebook.com"))
					fblink = s;
				else if (s.contains("themoviedb.org"))
					movielink = s;
				System.out.println(s);

			}
			if (twitterlink == null) {
				location = "http://query.yahooapis.com/v1/public/yql?q="
						+ "%20SELECT%20*%20FROM%20microsoft.bing.web%20WHERE%20query%3D%22"
						+ URLEncoder.encode(et.getText().toString()
								+ " twitter", "utf8")
						+ "%22%20|%20truncate(count%3D10)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
				output = fetchURL(location);
				temp = new JSONObject(output);
				temp = temp.getJSONObject("query").getJSONObject("results");
				jarray = temp.getJSONArray("WebResult");
				for (int i = 0; i < jarray.length(); i++) {
					temp = jarray.getJSONObject(i);
					String s = temp.getString("Url");
					if (s.contains("wikipedia"))
						wikilink = s;
					else if (s.contains("http://twitter.com"))
						twitterlink = s;
					else if (s.contains("facebook.com"))
						fblink = s;
					else if (s.contains("themoviedb.org"))
						movielink = s;
					System.out.println(s);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		toast = Toast.makeText(getApplicationContext(), "Fetching complete...",
				Toast.LENGTH_LONG);
		toast.show();

	}

	public String fetchURL(String location) {
		try {
			URL url = new URL(location);
			URLConnection uc = url.openConnection();
			// String encoded = Base64.encodeBase64String(new
			// String("rajam:Qwerty").getBytes());
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String output = "", inputLine;
			while ((inputLine = in.readLine()) != null)
				// System.out.println(inputLine);
				output += inputLine;
			in.close();
			return output;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void fetchTweets(View v) {
		Toast toast = Toast.makeText(getApplicationContext(), "connecting to twitter...",
				Toast.LENGTH_LONG);
		toast.show();
		String array[] = twitterlink.split("/");
		//Intent i = new Intent(this, WikiShow.class);
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/" + array[array.length - 1]));
		startActivity(browserIntent);
		//i.putExtra("htmlcontent", "http://www.twitter.com/" + array[array.length - 1]);
		//System.out.println("http://www.twitter.com/#!/" + array[array.length - 1]);
		//startActivity(i);
	}

	
	public void facebook_feed(View v){
		
		final Facebook facebook = new Facebook("149549488464512");
		Toast toast = Toast.makeText(getApplicationContext(), "Finding FB page...",
				Toast.LENGTH_LONG);
		toast.show();
		//System.out.println(fblink);
		//Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fblink));
		//startActivity(browserIntent);
		
		facebook.authorize(this, new DialogListener() {
			
			@Override
			public void onComplete(Bundle values) {
				try {
					Bundle parameters = new Bundle();
					System.out.println(((EditText)findViewById(R.id.editText1)).getText().toString());
					parameters.putString("q", ((EditText)findViewById(R.id.editText1)).getText().toString());
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
					view.loadUrl("http://www.facebook.com/" + result_page_id);*/
					
					System.out.println("http://www.face.com/" + result_page_id);
					Intent i = new Intent(c, WikiShow.class);
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
	
	
	public void youtube(View v) {
		EditText et = (EditText) findViewById(R.id.editText1);
		Toast toast = Toast.makeText(getApplicationContext(), "Searching Videos...",
				Toast.LENGTH_LONG);
		toast.show();
		String res = null;
		try {
			res = fetchURL("https://gdata.youtube.com/feeds/api/videos?q="+URLEncoder.encode(et.getText().toString(), "utf-8")+"&orderby=rating&max-results=10&alt=json");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String youtube_url[] = new String[10];
		String thumbnail[] = new String[10];
		String title[] = new String[10];
		try {
			//if(res == null)System.out.println("crash");
			//System.out.println(res);
			JSONObject jobj = new JSONObject(res);
			
			JSONArray videos = jobj.getJSONObject("feed").getJSONArray("entry");
			String url, id;
			JSONArray thumbnails;
			for (int i = 0; i < videos.length(); i++) {
				url = videos.getJSONObject(i).getJSONObject("id").getString("$t");
				title[i] = videos.getJSONObject(i).getJSONObject("title").getString("$t");
				//System.out.println
				id = url.split("/")[url.split("/").length - 1];
				youtube_url[i] = "http://www.youtube.com/watch?v=" + id;
				thumbnails = videos.getJSONObject(i)
						.getJSONObject("media$group")
						.getJSONArray("media$thumbnail");
				thumbnail[i] = thumbnails.getJSONObject(0).getString("url");
				//title[i]="";
			}
		
			Intent i = new Intent(this, YoutubeResults.class);
			i.putExtra("url", youtube_url);
			i.putExtra("thumbnail", thumbnail);
			i.putExtra("title", title);
			startActivity(i);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtube_url)));	
	}

	public void wiki(View v) {
		try {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Fetching content from wikipedia", Toast.LENGTH_LONG);
			toast.show();
			Intent i = new Intent(this, WikiShow.class);
			i.putExtra("htmlcontent", wikilink);
			startActivity(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onClickButton(View v) {
		EditText et = (EditText) findViewById(R.id.editText1);
		Toast toast = Toast.makeText(getApplicationContext(), "Searching...",
				Toast.LENGTH_LONG);
		toast.show();
		// String location = "http://query.yahooapis.com/v1/public/yql?"+
		// "q=select%20*%20from%20flickr.photos.search%20where%20text%3D%22"+et.getText()+
		// "%22%20limit%2010&diagnostics=true&format=json&sort=relevance";

		String location = "";
		try {
			location = "http://query.yahooapis.com/v1/public/yql?q="
					+ "%20SELECT%20*%20FROM%20microsoft.bing.image%20WHERE%20query%3D%22"
					+ URLEncoder.encode(et.getText().toString(), "utf8")
					+ "%22%20|%20truncate(count%3D10)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// String location = "http://www.google.com";
		// System.setProperty("http.proxyHost", "bsnlproxy.iitk.ac.in");
		// System.setProperty("http.proxyPort", "3128");

		try {
			URL url = new URL(location);
			URLConnection uc = url.openConnection();
			// String encoded = Base64.encodeBase64String(new
			// String("rajam:Qwerty").getBytes());
			BufferedReader in = new BufferedReader(new InputStreamReader(uc
					.getInputStream()));
			String output = "", inputLine;
			while ((inputLine = in.readLine()) != null)
				// System.out.println(inputLine);
				output += inputLine;
			in.close();
			JSONObject temp = new JSONObject(output);
			temp = temp.getJSONObject("query").getJSONObject("results");
			JSONArray jsonArray = temp.getJSONArray("ImageResult");
			for (int i = 0; i < jsonArray.length(); i++) {
				temp = jsonArray.getJSONObject(i);
				String photoURL = temp.getJSONObject("Thumbnail").getString(
						"Url");
				System.out.println(photoURL);
				searchUrl[i] = photoURL;
				fullImage[i] = temp.getString("MediaUrl");
			}
			Intent i = new Intent(this, Results.class);
			startActivity(i);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// new search

	public void onClickNewsButton(View v) {
		EditText et = (EditText) findViewById(R.id.editText1);
		Toast toast = Toast.makeText(getApplicationContext(), "Searching...",
				Toast.LENGTH_LONG);
		toast.show();

		String location = "";
		try {
			location = "http://query.yahooapis.com/v1/public/yql?"
					+ "q=%20SELECT%20*%20FROM%20microsoft.bing.news%20WHERE"
					+ "%20query%3D%22"
					+ URLEncoder.encode(et.getText().toString(), "utf8")
					+ "%22%20%7C%20truncate(count%3D10)&format=json&diagnostics"
					+ "=true&env=store%3A%2F%2Fdatatables.org%2Falltables"
					+ "withkeys";
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			URL url = new URL(location);
			URLConnection uc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc
					.getInputStream()));
			String output = "", inputLine;
			while ((inputLine = in.readLine()) != null)
				// System.out.println(inputLine);
				output += inputLine;
			in.close();
			JSONObject temp = new JSONObject(output);
			temp = temp.getJSONObject("query").getJSONObject("results");
			JSONArray jsonArray = temp.getJSONArray("NewsResult");
			String nUrls[] = new String[10];
			String nSnippets[] = new String[10];
			String ntitles[] = new String[10];
			for (int i = 0; i < jsonArray.length(); i++) {
				temp = jsonArray.getJSONObject(i);
				ntitles[i] = temp.getString("Title");
				nUrls[i] = new String(temp.getString("Url"));
				nSnippets[i] = new String(temp.getString("Snippet"));
				// System.out.println(nSnippets[i]);
			}
			Intent i = new Intent(this, NewsResults.class);
			i.putExtra("url", nUrls);
			i.putExtra("snippet", nSnippets);
			i.putExtra("title", ntitles);
			startActivity(i);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Object fetch(String address) throws MalformedURLException,
			IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}

	private Drawable ImageOperations(HelloActivity helloActivity, String url1) {
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