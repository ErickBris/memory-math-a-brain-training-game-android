package com.t4t.advertisments;
import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

public class AdsHelper {
	private static StartAppAd startAppAd;
	private static InterstitialAd interstitial;
	public static void initaializeAds(final Context context, Bundle savedInstanceState)
	{
		
		if(ApplicationConstants.CURRENT_MARKET_PLACE==ApplicationConstants.MARKET_PLACE_ANDROID)
		{
			ApplicationConstants.Rate_Us_MARKET_URL = "market://details?id="+context.getPackageName();
			
			
		}
		else if(ApplicationConstants.CURRENT_MARKET_PLACE==ApplicationConstants.MARKET_PLACE_AMAJONE)
		{
			ApplicationConstants.Rate_Us_MARKET_URL = "http://www.amazon.com/gp/mas/dl/android?p="+context.getPackageName();
			ApplicationConstants.More_Apps_MARKET_URL = "http://www.amazon.com/gp/mas/dl/android?p="+context.getPackageName()+"&showAll=1";
			
		}else if(ApplicationConstants.CURRENT_MARKET_PLACE==ApplicationConstants.MARKET_PLACE_SAMSUNG)
		{
			ApplicationConstants.Rate_Us_MARKET_URL = "samsungapps://ProductDetail/"+context.getPackageName();
			ApplicationConstants.More_Apps_MARKET_URL = "samsungapps://ProductDetail/"+context.getPackageName();
		}
		
			
		if(ApplicationConstants.isAdsEnabled)
		{
			//Initialize StartApp
	       StartAppSDK.init(context, ApplicationConstants.STARTAPP_DEVELOPER_ID, ApplicationConstants.STARTAPP_APP_ID, true);
	       startAppAd = new StartAppAd(context);
	       //StartAppAd.showSplash((Activity) context, savedInstanceState);
	       startAppAd.loadAd();
	       
	     //Admob
	       if(ApplicationConstants.isAdmobEnabled)
	    	   initializeAdmob(context);
	       
		}
		
	}
	
	private static void initializeAdmob(Context context) {
		 interstitial = new InterstitialAd((Activity) context);
		 interstitial.setAdUnitId(ApplicationConstants.ADMOB_INTERSTITIAL_ID);
		 AdRequest adRequest = new AdRequest.Builder().tagForChildDirectedTreatment(true).build();
		 interstitial.setAdListener(new AdListener() {

				@Override
				public void onAdClosed() {
					// TODO Auto-generated method stub
					super.onAdClosed();
					interstitial.loadAd(new AdRequest.Builder().tagForChildDirectedTreatment(true).build());
				}

				@Override
				public void onAdFailedToLoad(int errorCode) {
					// TODO Auto-generated method stub
					super.onAdFailedToLoad(errorCode);
				}

				@Override
				public void onAdLeftApplication() {
					// TODO Auto-generated method stub
					super.onAdLeftApplication();
				}

				@Override
				public void onAdLoaded() {
					// TODO Auto-generated method stub
					super.onAdLoaded();
				}

				@Override
				public void onAdOpened() {
					// TODO Auto-generated method stub
					super.onAdOpened();
				}				
				
			});
		 interstitial.loadAd(adRequest);
	}
	

	public static void showFullPageAds(final Activity context)
	{		
		context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(ApplicationConstants.isAdsEnabled)
				{
					if(((int)(Math.random()*1000))%ApplicationConstants.adsFrequency == 0)
					{
					
						if(ApplicationConstants.isAdmobEnabled && interstitial!=null && interstitial.isLoaded())
							interstitial.show();
						else if(startAppAd!=null && startAppAd.isReady())
						{
							startAppAd.showAd();
							startAppAd.loadAd();
						}
					}
					
				}
			}
		});
	}

	public static void showAdsOnExit(Activity context)
	{
		if(ApplicationConstants.isAdmobEnabled && interstitial!=null && interstitial.isLoaded())
			interstitial.show();
		else if(ApplicationConstants.isAdsEnabled && startAppAd!=null && ApplicationConstants.isStartAppInterEnabled)
			startAppAd.onBackPressed();
		context.finish();
	}
	public static void onRateAppClick(Context context) {
		Uri uri = Uri.parse(ApplicationConstants.Rate_Us_MARKET_URL);
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(goToMarket);
	}
	
	public static void showBannerAds(Context context, View view)
	{
		if(ApplicationConstants.isAdsEnabled)
		{
				((LinearLayout) view).removeAllViews();
				 AdView mAdView = new AdView(context);
				 AdSize adSize = AdSize.SMART_BANNER;
				 mAdView.setAdSize(adSize);
				 mAdView.setAdUnitId(ApplicationConstants.ADMOB_BANNER_ID);
				 ((LinearLayout) view).addView(mAdView);
			    AdRequest adRequest = new AdRequest.Builder().tagForChildDirectedTreatment(true).build();
			    mAdView.loadAd(adRequest);
		}
		
	}

	public static void onMoreAppsClick(Context context) {
		Uri uri = Uri.parse(ApplicationConstants.More_Apps_MARKET_URL);
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(goToMarket);
	}
	
	public static void shareScore(Activity gameActivity, View dialogView) {
		View view = gameActivity.getWindow().getDecorView();
		Bitmap bitmap1 = getScreenShot(gameActivity,view);
		Bitmap bitmap2 = getScreenShot(gameActivity, dialogView);
		
		Uri uri = getUri(gameActivity,bitmap1,bitmap2);
		
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		shareIntent.setType("image/*");
		gameActivity.startActivity(Intent.createChooser(shareIntent, "Share Using.."));

		
	}
	private static Uri getUri(Activity gameActivity, Bitmap bitmap1, Bitmap bitmap2) {
		Bitmap bmOverlay = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(),  bitmap1.getConfig());
		Canvas canvas = new Canvas(bmOverlay);
		canvas.drawBitmap(bitmap1, 0, 0, null);
		canvas.drawBitmap(bitmap2, 0, 0, null);
		File imageFile = null;
		try{
		    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		    imageFile = new File(path, "score11.png");
		    FileOutputStream fileOutPutStream = new FileOutputStream(imageFile);
		    bmOverlay.compress(Bitmap.CompressFormat.PNG, 80, fileOutPutStream);

		    fileOutPutStream.flush();
		    fileOutPutStream.close();
	    }catch(Exception exception)
	    {
	    	exception.printStackTrace();
	    }
		return Uri.parse("file://" + imageFile.getAbsolutePath());
	}
	private static Bitmap getScreenShot(Activity gameActivity,View view) {
		// create bitmap screen capture
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
		            view.getHeight(), Config.ARGB_8888);
		    Canvas canvas = new Canvas(bitmap);
		    view.draw(canvas);
		    
		    return bitmap;//;
	}

	
}


