package com.android.volley.toolbox;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public class XMLRequest extends Request<XmlPullParser> {

	private Listener<XmlPullParser> mListener;

	public XMLRequest(String url, Listener<XmlPullParser> mListener,
			ErrorListener listener) {
		this(Method.GET, url, mListener, listener);
	}

	public XMLRequest(int method, String url,
			Listener<XmlPullParser> mListener, ErrorListener listener) {
		super(method, url, listener);
		this.mListener = mListener;
	}

	@Override
	protected Response<XmlPullParser> parseNetworkResponse(
			NetworkResponse response) {
		String parsed;
		try {
			parsed = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(new StringReader(parsed));
			return Response.success(parser,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void deliverResponse(XmlPullParser response) {
		if (mListener != null) {
			mListener.onResponse(response);
		}
	}

	@Override
	protected void onFinish() {
		super.onFinish();
		mListener = null;
	}

}
