package com.jstt.PlayPhoto;

import android.app.Activity;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class PhotoPlay extends Activity implements OnTouchListener{

	private static final String tag = "PhotoPlay";
	private MyImageView myImageView = null;
	GestureDetector gestureDetector ;

	private int mode;
	private boolean firstflag = false;
	private float mStartX;

	private int pid = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		myImageView = new MyImageView(this);
		Intent intent = getIntent();

		pid = intent.getIntExtra("POSITION", 0);
		Log.d(tag, "pid" + pid);

		Bitmap bmp = BitmapFactory.decodeFile(Photo3DShow.albums[pid]);
		myImageView.setScreenSize(this, getWindowManager().getDefaultDisplay()
				.getWidth(),
				getWindowManager().getDefaultDisplay().getHeight(), bmp);

		setContentView(myImageView);
		myImageView.setOnTouchListener(this);
		
		
	}

	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		Log.d(tag, "event.getAction" + event.getAction());
		Log.d(tag, "event" + MotionEvent.ACTION_MASK);
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			firstflag = true;
			mode = 1;
			mStartX = event.getRawX();
			myImageView.init(event);
			Log.d(tag, "mode1 " + mode);
			return true;
		case MotionEvent.ACTION_UP:
			mode = 0;
			firstflag = false;
			Log.d(tag, "mode2 " + mode);
			return true;
		case MotionEvent.ACTION_POINTER_UP:
			mode -= 1;
			Log.d(tag, "mode3 " + mode);
			return true;
		case MotionEvent.ACTION_POINTER_DOWN:
			mode += 1;
			firstflag = false;
			Log.d(tag, "mode4 " + mode);
			myImageView.getOldDist(event);
			return true;
		case MotionEvent.ACTION_MOVE:
			if (mode >= 2) {
				Log.d(tag, "mode>=2");
				myImageView.zoom(event);
			} else {
				Log.d(tag, "mode=1");
				// 如果只有一只 手指滑动，则 移动图片，加上firstflag防止多点滑动放大缩小之后，一只手指也会移动图片
				if (firstflag) {
					myImageView.drag(event);
//					return gestureDetector.onTouchEvent(event); 
				}
			}
			return true;

		default:
			return false;

		}
	}

	private float calculate(float x1, float x2) {

		float pz = x1 - x2;// 计算两点间的距离
		return pz;
	}






}
