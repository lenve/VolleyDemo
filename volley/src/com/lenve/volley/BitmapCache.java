package com.lenve.volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapCache implements ImageCache {

	private LruCache<String, Bitmap> mCache;

	public BitmapCache() {
		int maxCache = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxCache / 8;
		mCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount();
			}
		};
	}

	@Override
	public Bitmap getBitmap(String key) {
		return mCache.get(key);
	}

	@Override
	public void putBitmap(String key, Bitmap bitmap) {
		mCache.put(key, bitmap);
	}
}