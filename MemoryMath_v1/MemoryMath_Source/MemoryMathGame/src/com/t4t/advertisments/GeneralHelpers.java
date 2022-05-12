package com.t4t.advertisments;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import com.memory.math.R;

public class GeneralHelpers {
	
	public static final String RATE_US = "Rate Us";
	private static final CharSequence NO_INTERNET_CONNECTION_MESSAGE = "\nSorry! No Active Internet Connection.\n\nPress settings and activate your data connection and try again.\n";
	public static final int SOUND_CLICK_SOUND = 1;
	public static final int SOUND_WIN_SOUND = 2;
	public static final int SOUND_GAME_START = 3;
	private static MediaPlayer mp;
	
	
	public static void playSound(Context context, int SOUND)
	{
		if(ApplicationConstants.enableSound)
		{
			if (mp != null) {
		        mp.release();
		        mp = null;
		    }
		    try {
		        mp = MediaPlayer
		    .create(context, SOUND); //BUT HERE I NEED DEFAULT SOUND!
		        mp.start();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
			
	}
	
	public static void showToast(Context context , String msg)
	{
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public static boolean isOnline(Activity context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni!=null && ni.isAvailable() && ni.isConnected()) {
			return true;
		} else {
			showNoInternetPopup(context);
			return false; 
		}
	}
	
	public static void showNoInternetPopup(final Activity context) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Error");
		builder.setMessage(NO_INTERNET_CONNECTION_MESSAGE)
		.setCancelable(false)
		.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {		        	   
				dialog.cancel();
				((Activity)context).finish();
			}
		});
		builder.setNegativeButton("Settings", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
				dialog.cancel();					
				((Activity)context).finish();
			}
		});
		
		
		context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				builder.show();
				
			}
		});
		
	}	
	
	public static void onRateAppClick(Activity context)
	{

		if(GeneralHelpers.isOnline(context))
		{
			String url = ApplicationConstants.Rate_Us_MARKET_URL;
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			
			if(i.resolveActivity(context.getPackageManager()) != null)
			{
				context.startActivity(i);
			}
		}
	}

	public static void updateSharedPreference(Context context,String key,boolean value)
	{
		SharedPreferences mkartPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
		Editor editor=mkartPref.edit();
		editor.putBoolean(key,value);
		editor.commit();
	}
	
	public static Boolean getSharedPreference(Context context,String key)
	{
		SharedPreferences mkartPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
		return mkartPref.getBoolean(key, false);
	}

	public static boolean isNotificationIconShown(Context context)
	{
		SharedPreferences mkartPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
		return mkartPref.getBoolean("icon", false);
	}
	
	public static void enableNotificationIcon(Context context)
	{
		updateSharedPreference(context, "icon", true);
	}
	
	public static void disableNotificationIcon(Context context)
	{
		updateSharedPreference(context, "icon", false);
	}
	
	public static void showRateUsPopup(final Activity context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(RATE_US);
		builder.setMessage(ApplicationConstants.Rate_Us_TEXT)
		.setCancelable(false)
		.setPositiveButton("Rate Now", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				updateSharedPreference(context, RATE_US, true);
				onRateAppClick(context);
				dialog.cancel();
				//((Activity)context).finish();
			}
		});
		builder.setNegativeButton("Later", new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();	
				//((Activity)context).finish();
			}
		});
		if(!getSharedPreference(context, RATE_US))
			builder.show();
	}
	
	public static void showExitGamePopup(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Confirmation");
		builder.setMessage("\nYou really want to exit?\n")
		.setCancelable(false)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				((Activity)context).finish();
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();	
			}
		});
		builder.show();
	}

	public static void shareScorOnFb(Activity m_context, int gaemMode,
			int highestScore) {
		// TODO Auto-generated method stub
		
		
		
	}
}
