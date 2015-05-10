package com.jstt.PlayPhoto;

import com.jstt.AsyncLoadBitmap.BitmapWorkerTask;
import com.jstt.multimedia.R;
import com.jstt.photofling.ImageGalleryView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.Toast;

public class Photo3DShow extends Activity implements OnFocusChangeListener{
	private final static String tag ="PhotoShow";
	int flag = 2;

	public static int width;
	public static int height;
	
	public int screenFlag = 0;
	
	public static String[] albums=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.galleryflow);
		Intent intent = getIntent();
//		final String path = intent.getStringExtra("path"); 
//        String name = intent.getStringExtra("name");  
//        flag=intent.getIntExtra("flag",1);
//        Log.d(tag,"flag = "+flag);
        albums=intent.getStringArrayExtra("data");
        
//       获取屏幕横竖屏状态 
//        if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
//        {
//           //横屏
//        	screenFlag = 1;
//        }
//        else if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
//        {
//        	//竖屏
//        	screenFlag = 2;
//        }
        setPicSize();
        
        BitmapWorkerTask.init(this.getApplicationContext(),1);
        
        ImageAdapter adapter = new ImageAdapter(this,albums,width,height);

		GalleryFlow galleryFlow = (GalleryFlow) findViewById(R.id.gallery_flow);
		galleryFlow.setAdapter(adapter);
		galleryFlow.setOnFocusChangeListener(this);
		galleryFlow.setOnItemClickListener(listener);
		
		
	}
	
	AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			Toast.makeText(Photo3DShow.this, "选中了"+position, Toast.LENGTH_LONG).show();
			Intent intent = new Intent();
//			intent.setClass(Photo3DShow.this, PhotoPlay.class);
			intent.setClass(Photo3DShow.this, ImageGalleryView.class);
			intent.putExtra("POSITION", position);
			Photo3DShow.this.startActivity(intent);
		}
		
	};

	public String[] getPaths(String[] info)
	{
		String[] paths=new String[info.length];
		for(int i=0; i<info.length; i++)
		{
			String id=albums[i].split("&")[1];
			paths[i]=id;
		}
		return paths;
	}
		
	public void setPicSize() {
		// TODO Auto-generated method stub
		Log.d(tag,"get screen size in");
		Display display = getWindowManager().getDefaultDisplay();
		int screenHeight =(int) display.getHeight();
		int screenWidth = (int)display.getWidth();
		Log.d(tag,"screenHeight+"+screenHeight+"screenWidth+"+screenWidth);
		float screenBe = screenHeight/screenWidth;
		if(screenBe <1)
		{
			width = 2*screenHeight/9;
//			height = 5*width/3;
		}
		else if(screenBe>1)
		{
			width = 4*screenWidth/15;
		}
		else
		{
			width = 150;
		}
		height = 5*width/2;
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
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
	}
	
}
