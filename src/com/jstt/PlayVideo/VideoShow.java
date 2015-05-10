package com.jstt.PlayVideo;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;

import com.jstt.multimedia.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jstt.AsyncLoadBitmap.BitmapWorkerTask;
import com.jstt.AsyncLoadBitmap.AsyncDrawable;

public class VideoShow extends Activity implements OnItemClickListener,
		OnItemSelectedListener {
	private static final String tag = "VideoShow";
	private ListView videolist = null;
	private ImageView videoThumbnail = null;
	private ImageView videoPlay = null;
	public static LinkedList<MovieInfo> playList = new LinkedList<MovieInfo>();
	private Uri videoListUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

	public class MovieInfo {
		String displayName;
		public String path;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videolist);
		Log.d(tag, "videoshow create");
		videolist = (ListView) findViewById(R.id.videolist);

		getPlayList();

		if(playList.size()<1)
		{
			Toast.makeText(this, "当前设备没有视频", Toast.LENGTH_LONG).show();
			return;
		}
		
		videolist.setOnItemClickListener(this);
		videolist.setOnItemSelectedListener(this);

		BitmapWorkerTask.init(this.getApplicationContext(),3);
		
		videolist.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return playList.size();
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public View getView(final int arg0, View convertView, ViewGroup arg2) {
				// TODO Auto-generated method stub
				if (convertView == null) {
					convertView = View.inflate(VideoShow.this,
							R.layout.videocell, null);
				}
				TextView text = (TextView) convertView
						.findViewById(R.id.videoName);
				text.setText(playList.get(arg0).displayName);
				videoThumbnail = (ImageView) convertView
						.findViewById(R.id.video_thumbnail);
				videoPlay = (ImageView) convertView
						.findViewById(R.id.videoplay);
				if (videoThumbnail == null) {
					Log.d(tag, "videoThumbnail is null");
				}

				loadBitmap(arg0, videoThumbnail);
				// videoThumbnail.setImageBitmap(getVideoThumbnail(playList.get(arg0).path,110, 80,MediaStore.Images.Thumbnails.MICRO_KIND));

				videoPlay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.d(tag, "image click");
						Toast.makeText(VideoShow.this, "启动视频播放器",
								Toast.LENGTH_LONG).show();
						Intent intent = new Intent();
						Bundle uriBundle = new Bundle();
						uriBundle.putInt("VIDEOID", arg0);
						intent.putExtras(uriBundle);
						intent.setClass(VideoShow.this, VideoPlay.class);
						startActivity(intent);
					}

				});
				return convertView;
			}

		});

	}

	public void loadBitmap(int resId, ImageView imageView) {
		if (AsyncDrawable.cancelPotentialWork(resId, imageView)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			Resources res = getResources();
			Log.d(tag,"loadbitmap0");
			Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.video_default0);
			Log.d(tag,"loadbitmap1");
			Log.d(tag,"resId="+resId);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(
					getResources(),bitmap, task);
//			final AsyncDrawable asyncDrawable = new AsyncDrawable(
//			getResources(),task);
			imageView.setImageDrawable(asyncDrawable);
			task.execute(resId);
		}
	}
	
	private void getPlayList() {
		getVideoFile(playList, new File("/sdcard/"));
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {

			Cursor cursor = getContentResolver()
					.query(videoListUri,
							new String[] { "_display_name", "_data" }, null,
							null, null);
			int n = cursor.getCount();
			cursor.moveToFirst();
			LinkedList<MovieInfo> playList2 = new LinkedList<MovieInfo>();
			for (int i = 0; i != n; ++i) {
				MovieInfo mInfo = new MovieInfo();
				mInfo.displayName = cursor.getString(cursor
						.getColumnIndex("_display_name"));
				mInfo.path = cursor.getString(cursor.getColumnIndex("_data"));
				playList2.add(mInfo);
				cursor.moveToNext();
			}

			if (playList2.size() > playList.size()) {
				playList = playList2;
			}
			cursor.close();
		}
	}

	private void getVideoFile(final LinkedList<MovieInfo> list, File file) {

		file.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				// TODO Auto-generated method stub
				String name = file.getName();
				int i = name.indexOf('.');
				if (i != -1) {
					name = name.substring(i);
					if (checkEndsWithInStringArray(name, getResources()
							.getStringArray(R.array.Video))) {
						MovieInfo mi = new MovieInfo();
						mi.displayName = file.getName();
						mi.path = file.getAbsolutePath();
						list.add(mi);
						return true;
					}
				} else if (file.isDirectory()) {
					getVideoFile(list, file);
				}
				return false;
			}
		});
	}

	// 检查文件类型分类
	public boolean checkEndsWithInStringArray(String checkItsEnd,
			String[] fileEndings) {
		for (String aEnd : fileEndings) {
			if (checkItsEnd.toLowerCase().equals(aEnd))
				return true;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		playList.clear();
		super.onDestroy();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		Log.d(tag, "item selsect");
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		Log.d(tag, "nothing select");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Log.d(tag, "item click");
		// Toast.makeText(VideoShow.this, "启动视频播放器", Toast.LENGTH_LONG).show();
		// Intent intent = new Intent(VideoShow.this,VideoPlay.class);
		// String videoUri = playList.get(arg2).path;
		// intent.putExtra("VIDEOPATH", videoUri);
		// startActivity(intent);
		// VideoChooseActivity.this.setResult(Activity.RESULT_OK,
		// intent);
		// VideoChooseActivity.this.finish();
	}
	
	
}
