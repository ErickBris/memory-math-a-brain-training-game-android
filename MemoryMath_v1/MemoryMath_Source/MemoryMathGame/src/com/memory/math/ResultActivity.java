package com.memory.math;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.t4t.advertisments.AdsHelper;
import com.t4t.advertisments.ApplicationConstants;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);      
		 // Do the stuff that initialize() would do for you
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		 
		setContentView(R.layout.results);
		
		AdsHelper.showFullPageAds(this);
		AdsHelper.showBannerAds(this, findViewById(R.id.ads));
		
		int bonusLevels = getIntent().getIntExtra("bonus", 0);
		((TextView)findViewById(R.id.TextViewLevels)).setText(""+bonusLevels+" x "+ApplicationConstants.LEVEL_BONUS_FACTOR);
		((TextView)findViewById(R.id.TextViewLevelsScore)).setText(""+(bonusLevels * ApplicationConstants.LEVEL_BONUS_FACTOR));
		
		int flips = getIntent().getIntExtra("flips", 0);
		((TextView)findViewById(R.id.TextViewFlips)).setText(""+flips+" x " + ApplicationConstants.SCORE_FACTOR_NEGATIVE);
		((TextView)findViewById(R.id.TextViewFlipsScore)).setText(""+(flips * ApplicationConstants.SCORE_FACTOR_NEGATIVE));
		
		int pairs = getIntent().getIntExtra("pairs", 0);
		((TextView)findViewById(R.id.TextViewPairs)).setText(""+pairs+" x " + ApplicationConstants.SCORE_FACTOR_POSITIVE);
		((TextView)findViewById(R.id.TextViewPairsScore)).setText(""+(pairs * ApplicationConstants.SCORE_FACTOR_POSITIVE));
		
		int score = (bonusLevels * ApplicationConstants.LEVEL_BONUS_FACTOR) + (flips * ApplicationConstants.SCORE_FACTOR_NEGATIVE) + (pairs * ApplicationConstants.SCORE_FACTOR_POSITIVE);
		((TextView)findViewById(R.id.TextViewScore)).setText(""+score);
	}
	
	public void onAddClick(View v)
	{
		Intent intent = new Intent(this,GamePlayActivity.class);
		intent.putExtra("add", true);
		startActivity(intent);
	}
	
	public void onRateUsClick(View v)
	{
		AdsHelper.onRateAppClick(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		AdsHelper.showFullPageAds(this);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		AdsHelper.showFullPageAds(this);
	}
	
	public void onHomeClick(View v)
	{
		onBackPressed();
	}
	
	public void onShareClick(View v)
	{
		AdsHelper.shareScore(this, findViewById(R.id.screenShotView));
	}
	

}
