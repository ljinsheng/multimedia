package com.jstt.PlayVideo;

import com.jstt.PlayVideo.VideoView.MySizeChangeLinstener;
import com.jstt.multimedia.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue.IdleHandler;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class VideoPlay extends Activity implements OnPreparedListener,OnErrorListener,OnCompletionListener
	{

	private static final String tag = "videoplay";
	private Uri videoUri = null;
	private PopupWindow controler = null;
	
	private boolean isOnline = false; 
	private boolean isChangedVideo = false;
	private boolean isControlerShow = true;
	private boolean isPaused = false;
	private boolean isFullScreen = true;
//	private boolean isSilent = false;
//	private boolean isSoundShow = false;
	
	private static int screenWidth = 0;
	private static int screenHeight = 0;
	private static int controlHeight = 0;  
	
	private ImageButton screenCtrlBtn = null;
	private ImageButton fwdCtrlBtn = null;
	private ImageButton playBtn =  null;
	private ImageButton nextCtrlBtn = null;
	private ImageButton infoBtn = null;
	
	private static int position ;
	private int playedTime;
	private View controlView = null;
	private VideoView mVideoView = null;
	private SeekBar seekBar = null;  
	private TextView durationTextView = null;
	private TextView playedTextView = null;
	
    private final static int Screen16_to_9 = 0;
    private final static int Screen4_to_3 = 1;
    
    private final static int PROGRESS_CHANGED = 0;
    private final static int HIDE_CONTROLER = 1;
    
	private final static int TIME = 6868; 
	
	private GestureDetector mGestureDetector = null;
//	private final boolean is4_to_3 = false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setFullScreen();
		setContentView(R.layout.videoplaypage);
		mVideoView = (VideoView)findViewById(R.id.myVideoView);
		Log.d(tag," videoplay create");
		Bundle getBundle = getIntent().getExtras();
		position = -1;
		if(getBundle!=null)
		{
			position = getBundle.getInt("VIDEOID");
			Log.d(tag,"videoID is "+position);
			mVideoView.setVideoPath(VideoShow.playList.get(position).path);
		}
		else
		{
			Log.d(tag, getIntent().toString());
			videoUri = getIntent().getData();
		}
		isOnline = false;
		isChangedVideo = true;
		
		Log.d(tag,"videoplay11");
		
		mVideoView.setOnPreparedListener(this);
		mVideoView.setOnErrorListener(this);
		mVideoView.setOnCompletionListener(this);
		
		mVideoView.setMySizeChangeLinstener(new MySizeChangeLinstener(){

			@Override
			public void doMyThings() {
				// TODO Auto-generated method stub
				Log.d(tag,"mysize changelistener");
				setVideoScale(Screen16_to_9);
			}
        	
        });
        Looper.myQueue().addIdleHandler(new IdleHandler(){

			@Override
			public boolean queueIdle() {				
				// TODO Auto-generated method stub
				if(controler != null && mVideoView.isShown()){
					Log.d(tag,"idleHandle looper");
					controler.showAtLocation(mVideoView, Gravity.BOTTOM, 0, 0);
					//controler.update(screenWidth, controlHeight);
					controler.update(0, 0, screenWidth, controlHeight);
				}
				
//				if(extralWindow != null && vv.isShown()){
//					extralWindow.showAtLocation(vv,Gravity.TOP,0, 0);
//					extralWindow.update(0, 25, screenWidth, 60);
//				}
//				
				//myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
				Log.d(tag,"idleHandle looper22");
				return false;  
			}
        });
        controlView = getLayoutInflater().inflate(R.layout.controler, null);
        controler = new PopupWindow(controlView);
        durationTextView = (TextView) controlView.findViewById(R.id.tvDTime);
        playedTextView = (TextView) controlView.findViewById(R.id.tvCurrTime);
        
      
        
        screenCtrlBtn = (ImageButton)controlView.findViewById(R.id.BtnFullScreen);
        fwdCtrlBtn = (ImageButton)controlView.findViewById(R.id.BtnSeekBack);
        playBtn = (ImageButton)controlView.findViewById(R.id.BtnPlay);
        nextCtrlBtn = (ImageButton)controlView.findViewById(R.id.BtnSeek);
        infoBtn = (ImageButton)controlView.findViewById(R.id.BtnInfo);
        seekBar = (SeekBar)controlView.findViewById(R.id.seek);
        
        Log.d(tag,"videoplay22");
        if(videoUri!=null){
        	Log.d(tag,"uri is "+videoUri);
        	mVideoView.stopPlayback();
        	mVideoView.setVideoURI(videoUri);
        	isOnline= true;
        	
        	playBtn.setImageResource(R.drawable.pause_normal);
        }else{
        	Log.d(tag,"videoUri is null");
        	playBtn.setImageResource(R.drawable.play_normal);
        }
        
        screenCtrlBtn.setAlpha(0xBB);
        fwdCtrlBtn.setAlpha(0xBB);  
        playBtn.setAlpha(0xBB);
        nextCtrlBtn.setAlpha(0xBB);
        
        Log.d(tag,"videoplay33");
        
        screenCtrlBtn.setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(tag,"screenCtrlBtn click");
				cancelDelayHide();
				if(isFullScreen)
				{
//					mVideoView.start();
					screenCtrlBtn.setImageResource(R.drawable.screen_43_normal);
					setVideoScale(Screen4_to_3);
				}
				else
				{
//					mVideoView.pause();
					screenCtrlBtn.setImageResource(R.drawable.screen_169_normal);
					setVideoScale(Screen16_to_9);
				}
				hideControlerDelay();
				isFullScreen=!isFullScreen;
				
			}
        	
        });
        
        fwdCtrlBtn.setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(tag,"fwdCtrlBtn click");
				isOnline = false;
				if(--position>=0)
				{
					mVideoView.setVideoPath(VideoShow.playList.get(position).path);
					cancelDelayHide();
					hideControlerDelay();
				}
				else
				{
					VideoPlay.this.finish();
				}
			}
        	
        });
        playBtn.setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(tag,"playBtn click");
				cancelDelayHide();
				if(isPaused)
				{
					mVideoView.start();
					playBtn.setImageResource(R.drawable.pause_normal);
					hideControlerDelay();
				}
				else
				{
					mVideoView.pause();
					playBtn.setImageResource(R.drawable.play_normal);
				}
				isPaused = !isPaused;
			}
        	
        });
        nextCtrlBtn.setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(tag,"nextCtrlBtn click");
				int n = VideoShow.playList.size();
				isOnline = false;
				if(++position<n)
				{
					mVideoView.setVideoPath(VideoShow.playList.get(position).path);
					cancelDelayHide();
					hideControlerDelay();
				}
				else
				{
					VideoPlay.this.finish();
				}
			}
        	
        });
        infoBtn.setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(tag,"infoBtn click");
			}
        	
        });
        
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				Log.d(tag,"seekbar change");
				if(fromUser)
				{
					if(!isOnline)
					{
						mVideoView.seekTo(progress);
					}
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				Log.d(tag,"onStartTrackingTouch");
				myHandler.removeMessages(HIDE_CONTROLER);
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				Log.d(tag,"onStopTrackingTouch");
				myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
			}
        	
        	
        });
        
        getScreenSize();

		mGestureDetector = new GestureDetector(new SimpleOnGestureListener() {

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				// TODO Auto-generated method stub
				Log.d(tag,"onDoubleTap");
				if (isFullScreen) {
					Log.d(tag,"onDoubleTap11");
					setVideoScale(Screen4_to_3);
				} else {
					Log.d(tag,"onDoubleTap22");
					setVideoScale(Screen16_to_9);
				}
				isFullScreen = !isFullScreen;
				Log.d(tag, "onDoubleTap33");

				if (isControlerShow) {
					Log.d(tag,"onDoubleTap44");
					showControler();
				}
				// return super.onDoubleTap(e);
				return true;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				// TODO Auto-generated method stub
				Log.d(tag, "onSingleTapConfirmed");
				if (!isControlerShow) {
					Log.d(tag, "onSingleTapConfirmed11");
					showControler();
					hideControlerDelay();
				} else {
					Log.d(tag, "onSingleTapConfirmed22");
					cancelDelayHide();
					hideControler();
				}
				// return super.onSingleTapConfirmed(e);
				return true;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				Log.d(tag,"onLongPress");
				if (isPaused) {
					Log.d(tag,"onLongPress11");
					mVideoView.start();
					playBtn.setImageResource(R.drawable.pause_normal);
					cancelDelayHide();
					hideControlerDelay();
				} else {
					Log.d(tag,"onLongPress22");
					mVideoView.pause();
					playBtn.setImageResource(R.drawable.play_normal);
					cancelDelayHide();
					showControler();
				}
				isPaused = !isPaused;
				// super.onLongPress(e);
			}
		});
        
        
	}

	private void getScreenSize() {
		// TODO Auto-generated method stub
		Log.d(tag,"get screen size in");
		Display display = getWindowManager().getDefaultDisplay();
		screenHeight = display.getHeight();
		screenWidth = display.getWidth();
		controlHeight = screenHeight / 5;
	}

	private void cancelDelayHide(){
		Log.d(tag,"cancel delayhide");
		myHandler.removeMessages(HIDE_CONTROLER);
	}
	
	private void hideControlerDelay(){
		Log.d(tag,"hide controler delay");
		myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
	}
	
	Handler myHandler = new Handler(){
	    
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			switch(msg.what){
			
				case PROGRESS_CHANGED:
					Log.d(tag,"PROGRESS_CHANGED");
					int i = mVideoView.getCurrentPosition();
					seekBar.setProgress(i);
					
					if(isOnline){
						Log.d(tag,"PROGRESS_CHANGED11online");
						int j = mVideoView.getBufferPercentage();
						seekBar.setSecondaryProgress(j * seekBar.getMax() / 100);
					}else{
						Log.d(tag,"PROGRESS_CHANGED22not");
						seekBar.setSecondaryProgress(0);
					}
					
					i/=1000;
					int minute = i/60;
					int hour = minute/60;
					int second = i%60;
					minute %= 60;
					playedTextView.setText(String.format("%02d:%02d:%02d", hour,minute,second));
					
					sendEmptyMessageDelayed(PROGRESS_CHANGED, 100);
					break;
					
				case HIDE_CONTROLER:
					Log.d(tag,"HIDE_CONTROLER");
					hideControler();
					break;
			}
			
			super.handleMessage(msg);
		}	
    };
	
	private void hideControler(){
		if(controler.isShowing()){
			Log.d(tag,"hideController");
			controler.update(0,0,0, 0);
			isControlerShow = false;
		}
//		if(mSoundWindow.isShowing()){
//			mSoundWindow.dismiss();
//			isSoundShow = false;
//		}
	}
    
	private void setFullScreen()
	{
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	private void setVideoScale(int flag){
    	
		Log.d(tag,"set video scale in");
    	LayoutParams lp = mVideoView.getLayoutParams();
    	
    	switch(flag){
    		case Screen16_to_9:
    			Log.d(tag,"set video scale full screen");    			
    			Log.d(tag, "screenWidth: "+screenWidth+" screenHeight: "+screenHeight);
    			mVideoView.setVideoScale(screenWidth, screenHeight);
    			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    			
    			break;
    		case Screen4_to_3:
    			Log.d(tag,"set video scale 4:3 screen");
//    			int videoHeight = mVideoView.getVideoHeight();
    			int mHeight = screenHeight;
    			int mWidth = mHeight*4/3;
    			
    			mVideoView.setVideoScale(mWidth, mHeight);
    			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); 
    			break;
    			
//    		case SCREEN_DEFAULT1:
//    			Log.d(tag,"set video scale default screen");
//    			int videoWidth = mVideoView.getVideoWidth();
//    			int videoHeight = mVideoView.getVideoHeight();
//    			int mWidth = screenWidth;
//    			int mHeight = screenHeight - 25;
//    			
//    			if (videoWidth > 0 && videoHeight > 0) {
//    	            if ( videoWidth * mHeight  > mWidth * videoHeight ) {
//    	                //Log.i("@@@", "image too tall, correcting");
//    	            	mHeight = mWidth * videoHeight / videoWidth;
//    	            } else if ( videoWidth * mHeight  < mWidth * videoHeight ) {
//    	                //Log.i("@@@", "image too wide, correcting");
//    	            	mWidth = mHeight * videoWidth / videoHeight;
//    	            } else {
//    	                
//    	            }
//    	        }
//    			
//    			mVideoView.setVideoScale(mWidth, mHeight);
//    			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);    			
//    			break;
    	}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		Log.d(tag,"ontouchevent");
		boolean result = mGestureDetector.onTouchEvent(event);

		if (!result) {
			if (event.getAction() == MotionEvent.ACTION_UP) {

				Log.d(tag,"ontouchevent11");
				/*
				 * if(!isControlerShow){ showControler();
				 * hideControlerDelay(); }else { cancelDelayHide();
				 * hideController(); }
				 */
			}
			Log.d(tag,"ontouchevent22");
			result = super.onTouchEvent(event);
		}

		Log.d(tag,"ontouchevent33");
		return result;
	}

	
	/*
	 * android:onConfigurationChanged实际对应的是Activity里的onConfigurationChanged()方法。
	 * 在AndroidManifest.xml中添加代码
	 * android:configChanges="orientation|keyboard|keyboardHidden"
	 * 的含义是表示在改变屏幕方向、弹出软件盘和隐藏软键盘时，
	 * 不再去执行onCreate()方法，而是直接执行onConfigurationChanged()。如果不申明此段代码，
	 * 按照Activity的生命周期，都会去执行一次onCreate()方法，而onCreate（）方法通常会在显示之前做一些初始化工作。
	 * 所以如果改变屏幕方向这样的操作都去执行onCreate()方法，就有可能造成重复的初始化，
	 * 降低程序效率是必然的了，而且更有可能因为重复的初始化而导致数据的丢失
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		Log.d(tag,"onConfigurationChanged00");
		getScreenSize();
		if (isControlerShow) {

			Log.d(tag,"onConfigurationChanged11");
			cancelDelayHide();
			hideControler();
			showControler();
			hideControlerDelay();
		}

		Log.d(tag,"onConfigurationChanged22");
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(tag,"onDestroy00");
		if (controler.isShowing()) {
			Log.d(tag,"onDestroy11");
			controler.dismiss();
			// extralWindow.dismiss();
		}
//		if (mSoundWindow.isShowing()) {
//			mSoundWindow.dismiss();
//		}

		Log.d(tag,"onDestroy22");
		myHandler.removeMessages(PROGRESS_CHANGED);
		myHandler.removeMessages(HIDE_CONTROLER);

		if (mVideoView.isPlaying()) {
			Log.d(tag,"onDestroy33");
			mVideoView.stopPlayback();
		}

		Log.d(tag,"onDestroy44");
		isFullScreen = true;
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d(tag,"onpause");
		playedTime = mVideoView.getCurrentPosition();
		mVideoView.pause();
		playBtn.setImageResource(R.drawable.play_normal);
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.d(tag,"onrestart");
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(tag,"onResume00");
		if (!isChangedVideo) {
			Log.d(tag,"onResume11");
			mVideoView.seekTo(playedTime);
			mVideoView.start();
		} else {
			Log.d(tag,"onResume22");
			isChangedVideo = false;
		}

		// if(vv.getVideoHeight()!=0){
		if (mVideoView.isPlaying()) {
			Log.d(tag,"onResume33");
			playBtn.setImageResource(R.drawable.pause_normal);
			hideControlerDelay();
		}
		Log.d("REQUEST", "NEW AD !");
		// adView.requestFreshAd();

		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			Log.d(tag,"onResume44");
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		Log.d(tag,"onResume55");
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.d(tag,"onstart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.d(tag,"onstop");
		super.onStop();
	}

	private void showControler() {
		Log.d(tag,"showControler");
		// TODO Auto-generated method stub
		controler.update(0,0,screenWidth,controlHeight);
		isControlerShow = true;
	}
	
	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		Log.d(tag,"video onprepared");
		setVideoScale(Screen16_to_9);
		isFullScreen = true;
		if (isControlerShow) {
			Log.d(tag,"video completion11");
			showControler();
		}

		Log.d(tag,"video completion22");
		int i = mVideoView.getDuration();
		Log.d("onCompletion", "" + i);
		seekBar.setMax(i);
		i /= 1000;
		int minute = i / 60;
		int hour = minute / 60;
		int second = i % 60;
		minute %= 60;
		durationTextView.setText(String.format("%02d:%02d:%02d", hour,
				minute, second));

		/*
		 * controler.showAtLocation(vv, Gravity.BOTTOM, 0, 0);
		 * controler.update(screenWidth, controlHeight);
		 * myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
		 */

		mVideoView.start();
		playBtn.setImageResource(R.drawable.pause_normal);
		hideControlerDelay();
		myHandler.sendEmptyMessage(PROGRESS_CHANGED);
	}

	

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		mVideoView.stopPlayback();
		new AlertDialog.Builder(VideoPlay.this)
        .setTitle("很抱歉")
        .setMessage("不支持该视频格式的播放")
        .setPositiveButton("确定",
                new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						
						mVideoView.stopPlayback();
						VideoPlay.this.finish();
						
					}
   
                })
        .setCancelable(false)
        .show();
		return false;

	}


	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		Log.d(tag,"video completion in");
		int videoLength = VideoShow.playList.size();
		isOnline = false;
		if(++position<videoLength)
		{
			Log.d(tag,"not completion,next video");
			mVideoView.setVideoPath(VideoShow.playList.get(position).path);			
		}
		else if(++position==videoLength)
		{
			Log.d(tag,"first video completion");
			mVideoView.setVideoPath(VideoShow.playList.get(0).path);
		}
		else
		{
			Log.d(tag,"video completion");
			mVideoView.stopPlayback();
			VideoPlay.this.finish();
		}
	}
	
}
