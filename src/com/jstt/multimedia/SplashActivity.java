package com.jstt.multimedia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;


public class SplashActivity extends Activity 
{
	private static final String tag = "splash";
	private Thread splashTimer;
	private long splashTime = 3000;
	private boolean isPaused = false;
	private boolean isSplashActive = true;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.splash);
        new Handler().postDelayed(new Runnable()
        {
        	@Override
        	public void run()
        	{
        		Intent intent =  new Intent(SplashActivity.this,MultiMediaActivity.class);
        		startActivity(intent);
        		SplashActivity.this.finish();
        	}
        }, 3000);//启动动画持续3s
    }
    public void setFullScreen()
    {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
    	Window myWindow = this.getWindow();
    	myWindow.setFlags(flag, flag);
    }
    public void startSplashTimer()
    {
    	splashTimer = new Thread()
    	{
    		public void run()
    		{
    			try
    			{
    				long ms = 0;
    				while(isSplashActive&&ms<splashTime)
    				{
    					sleep(100);
    					if(!isPaused)
    						ms += 100;
    				}
    				startActivity(new Intent("com.google.app.CLEARSPLASH"));
    			}
    			catch(Exception e)
    			{
    				e.printStackTrace();
    			}
    			finally
    			{
    				finish();
    			}
    		}
    	};
    	splashTimer.start();
    }
    protected void onPause()
    {
    	super.onPause();
    	isPaused = true;
    }
    protected void onResume()
    {
    	super.onResume();
    	isPaused = false;
    }
}