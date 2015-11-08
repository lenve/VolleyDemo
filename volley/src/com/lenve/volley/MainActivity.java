package com.lenve.volley;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.XMLRequest;

public class MainActivity extends Activity {

	private ImageView iv, iv3;
	private NetworkImageView niv;
	private RequestQueue mQueue;
	private TextView tv;
	private String XMLURL = "http://wthrcdn.etouch.cn/WeatherApi?city=%E8%A5%BF%E5%AE%89";
	private String HTTPURL = "http://litchiapi.jstv.com/api/GetFeeds?column=3&PageSize=20&pageIndex=1&val=100511D3BE5301280E0992C73A9DEC41";
	private String IMAGEURL = "http://e.hiphotos.baidu.com/image/w%3D230/sign=8010102c1f950a7b753549c73ad1625c/0d338744ebf81a4c9d17a7d3d52a6059252da687.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) this.findViewById(R.id.tv);
		iv = (ImageView) this.findViewById(R.id.iv1);
		niv = (NetworkImageView) this.findViewById(R.id.iv2);
		iv3 = (ImageView) this.findViewById(R.id.iv3);
		mQueue = Volley.newRequestQueue(this);
		// stringRequest();
		// jsonRequest();
		// imageLoader();
		// networkImageView();
		// imageRequest();
		xmlRequest();
	}

	private void xmlRequest() {
		XMLRequest xr = new XMLRequest(XMLURL,
				new Response.Listener<XmlPullParser>() {

					@Override
					public void onResponse(XmlPullParser parser) {
						try {
							int eventType = parser.getEventType();
							while (eventType != XmlPullParser.END_DOCUMENT) {
								switch (eventType) {
								case XmlPullParser.START_DOCUMENT:
									break;
								case XmlPullParser.START_TAG:
									String tagName = parser.getName();
									if ("city".equals(tagName)) {
										Log.i("lenve",
												new String(parser.nextText()));
									}
									break;
								case XmlPullParser.END_TAG:
									break;
								}
								eventType = parser.next();
							}
						} catch (XmlPullParserException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});
		mQueue.add(xr);
	}

	private void imageRequest() {
		ImageRequest ir = new ImageRequest(IMAGEURL, new Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap response) {
				iv3.setImageBitmap(response);
			}
		}, 0, 0, ScaleType.CENTER, Bitmap.Config.ARGB_8888,
				new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});
		mQueue.add(ir);
	}

	private void networkImageView() {
		niv.setDefaultImageResId(R.drawable.ic_launcher);
		ImageLoader il = new ImageLoader(mQueue, new BitmapCache());
		niv.setImageUrl(IMAGEURL, il);
	}

	private void imageLoader() {
		ImageLoader il = new ImageLoader(mQueue, new BitmapCache());
		ImageListener listener = ImageLoader.getImageListener(iv,
				R.drawable.ic_launcher, R.drawable.ic_launcher);
		il.get(IMAGEURL, listener);
	}

	private void jsonRequest() {
		JsonObjectRequest jsonReq = new JsonObjectRequest(HTTPURL,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONObject jo = response.getJSONObject("paramz");
							JSONArray ja = jo.getJSONArray("feeds");
							for (int i = 0; i < ja.length(); i++) {
								JSONObject jo1 = ja.getJSONObject(i)
										.getJSONObject("data");
								Log.i("lenve", jo1.getString("subject"));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});
		mQueue.add(jsonReq);
	}

	private void stringRequest() {
		StringRequest sr = new StringRequest("http://www.baidu.com",
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("lenve", response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

					}
				}) {
			/**
			 * 重写getParams(),可以自己组装post要提交的参数
			 */
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("params1", "value1");
				map.put("params1", "value1");
				return map;
			}
		};
		mQueue.add(sr);
	}
}
