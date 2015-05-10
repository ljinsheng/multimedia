package com.jstt.PlayPhoto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.jstt.multimedia.R;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class PhotoAlbums extends ListActivity {
	LinkedList<ImageAlbumsInfo> bitmaps = null;
	Cursor cursor = null;
	private static final String TAG = "PhotoAlbums";
	private DBAdapter dbAdapter = null;
	private static String path = Environment.getExternalStorageDirectory()
			.getPath();
	private BroadcastReceiver mReceiver = null;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setTitle(R.string.album_scan);
		setContentView(R.layout.list_view);

		bitmaps = new LinkedList<ImageAlbumsInfo>();

		try {
			dbAdapter = new DBAdapter(this);
			dbAdapter.open();
			getThumbnailsPhotosInfo(path);
			if (dbAdapter != null)
				dbAdapter.close();
			setListAdapter(new ListViewAdapter(this, bitmaps));
		} catch (Exception err) {
			if (dbAdapter != null)
				dbAdapter.close();
			err.printStackTrace();
			Log.i(TAG, "get Thumbnails has err!");
			Toast.makeText(this, R.string.no_sdcard, Toast.LENGTH_LONG).show();
			return;
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		mReceiver = null;
		if (dbAdapter != null)
			dbAdapter.close();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		intentFilter.addDataScheme("file");

		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				try {
					getThumbnailsPhotosInfo("/sdcard");

					setListAdapter(new ListViewAdapter(PhotoAlbums.this,
							bitmaps));
				} catch (Exception err) {
					err.printStackTrace();
					Log.i(TAG, "get Thumbnails has err!");
					Toast.makeText(PhotoAlbums.this, R.string.no_sdcard,
							Toast.LENGTH_LONG).show();
					return;
				}

			}
		};
		registerReceiver(mReceiver, intentFilter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		 String name = bitmaps.get(position).displayName;
		 // name eg:高清美女图片
		 String path = bitmaps.get(position).path;
		 Log.i("ImageListView_onListItemClick", "the name="+name+"; path="+path);
		 // path eg:/mnt/sdcard/高清美女图片
		Intent intent = new Intent();
		// intent.setClass(ImageListView.this, ImageGridView.class);
		intent.setClass(PhotoAlbums.this, Photo3DShow.class);
		// intent.putExtra("name", name);
		// intent.putExtra("path", path);
		// intent.putExtra("flag", flag);

		List list = bitmaps.get(position).paths;
		intent.putExtra("data",
				(String[]) list.toArray(new String[list.size()]));
		PhotoAlbums.this.startActivity(intent);
	}

	private void getThumbnailsPhotosInfo(String path) {

		try {
			cursor = getContentResolver().query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
					null, null);
			if (cursor == null) {
				Toast.makeText(this, R.string.no_sdcard, Toast.LENGTH_LONG)
						.show();
				return;
			}
		} catch (Exception err) {
			if (cursor != null)
				cursor.close();
			Toast.makeText(this, R.string.no_sdcard, Toast.LENGTH_LONG).show();
			return;
		}

		ImageAlbumsInfo info = null;

		HashMap<String, LinkedList<String>> albums = ImageCommon
				.getAlbumsInfo(2,cursor);
		cursor.close();

		for (Iterator<?> it = albums.entrySet().iterator(); it.hasNext();) {
			Map.Entry e = (Map.Entry) it.next();
			Log.i(TAG, "key: " + e.getKey());
			Log.i(TAG, "value: " + e.getValue());
			LinkedList<String> album = (LinkedList<String>) e.getValue();

			if (album != null && album.size() > 0) {
				info = new ImageAlbumsInfo();
				info.displayName = (String) e.getKey();
				info.picturecount = String.valueOf(album.size());

				// 提取第0个记录中的信息。
				String[] photoinfo=album.get(0).split("&");
				String id = photoinfo[0];
				String albumpath = photoinfo[1];

				String name = albumpath
						.substring(albumpath.lastIndexOf("/") + 1);
				albumpath = albumpath.substring(0, albumpath.lastIndexOf("/"));

				info.icon = Thumbnails.getThumbnail(getContentResolver(),
						Integer.valueOf(id), Thumbnails.MICRO_KIND,
						new BitmapFactory.Options());

				info.path = albumpath;

				List list = new ArrayList();
				for(int i=0;i<album.size();i++)
				{
					String str= "";
					str=album.get(i).split("&")[1];
					list.add(str);				
				}
				info.paths = list;

				bitmaps.add(info);
			}
		}
		cursor.close();
	}
}
