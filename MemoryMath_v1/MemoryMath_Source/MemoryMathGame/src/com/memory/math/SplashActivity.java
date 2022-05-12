package com.memory.math;
import com.t4t.advertisments.AdsHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {

	protected boolean _active = true;
	protected int _splashTime = 4000;


public void onCreate(Bundle savedInstanceState) 
{
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	super.onCreate(savedInstanceState);	
		
	showSplashPage();
	AdsHelper.initaializeAds(this, savedInstanceState);
}

private void showSplashPage() {
	setContentView(R.layout.flashscreen);
	Thread splashTread = new Thread()
	{
		@Override
		public void run()
		{
		try
		{
			int waited = 0;
			while (_active && (waited < _splashTime))
			{
				sleep(100);
				if (_active)
				{
					waited += 100;
				}
			}
		} catch (InterruptedException e)
		{
			
		} finally
			{
				_active = false;
				Intent intent=new Intent(SplashActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
								
			}
		}
	};
	splashTread.start();
	
}
	
}