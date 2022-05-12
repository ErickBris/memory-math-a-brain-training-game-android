package com.memory.math;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.t4t.advertisments.AdsHelper;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);      
		 // Do the stuff that initialize() would do for you
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		 
		setContentView(R.layout.home_activity);
		
		AdsHelper.showFullPageAds(this);
		AdsHelper.showBannerAds(this, findViewById(R.id.ads));

		
	}
	
	public void onAddClick(View v)
	{
		Intent intent = new Intent(this,GamePlayActivity.class);
		intent.putExtra("add", true);
		startActivity(intent);
	}
	
	public void onSubClick(View v)
	{
		Intent intent = new Intent(this,GamePlayActivity.class);
		intent.putExtra("sub", true);
		startActivity(intent);
	}
	
	public void onRandomClick(View v)
	{
		Intent intent = new Intent(this,GamePlayActivity.class);
		intent.putExtra("random", true);
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
		showConfirmationDialog();
	}
	
	private void showConfirmationDialog()
	{
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage("Are you sure you want to Exit??")
	               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   AdsHelper.showAdsOnExit(HomeActivity.this);
	                   }
	               })
	               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	  dialog.cancel();
	                   }
	               });
	        // Create the AlertDialog object and return it
	         builder.create().show();;
	}

}
