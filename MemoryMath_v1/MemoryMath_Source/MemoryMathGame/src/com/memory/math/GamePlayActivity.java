package com.memory.math;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.gc.materialdesign.views.ProgressBarDetermininate;
import com.t4t.advertisments.AdsHelper;
import com.t4t.advertisments.ApplicationConstants;
import com.t4t.advertisments.GeneralHelpers;

public class GamePlayActivity extends Activity {

	String[] questions = {"1+2","3","5+6","11","3+9","12","4+2","6","3+9","12","4+2","6"};
	private ProgressBarDetermininate mProgress;
	public int progress;
	private Timer timer;
	private ProgressBarUpdateTask progressBarUpdateTask;
	public int difficulty=12;
	
	public int rightAnswers,numberOfFlips;
	private int bonus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);      
		 // Do the stuff that initialize() would do for you
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		 
		setContentView(R.layout.gameplay);
		
		AdsHelper.showBannerAds(this, findViewById(R.id.ads));
		initializeProgressBar();
		restartGame(false);
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
	
	private void initializeProgressBar() {
		//initialize progressbar
		progress = ApplicationConstants.GAME_TIME;
		
		mProgress = (ProgressBarDetermininate) findViewById(R.id.progressDeterminate);
		mProgress.setMax(progress);
		mProgress.setProgress(progress );
		timer = new Timer();
		progressBarUpdateTask = new ProgressBarUpdateTask();
		timer.schedule(progressBarUpdateTask, 20, 20);
	}

	class ProgressBarUpdateTask extends TimerTask {
		  @Override
		  public void run() {
		   runOnUiThread(new Runnable(){
		    @Override
		    public void run() {
		    	progress-=1;
		    	if(progress==0)
		    	{
		    		TimeOver();
		    	}
		    	mProgress.setProgress(progress);
		    }

			});
		  }

	 }
	private void TimeOver() {
		GeneralHelpers.playSound(this, R.raw.win);
		
		Toast.makeText(this, "Times Up!", Toast.LENGTH_LONG).show();
		
		//Calculate Score
		int score = rightAnswers*ApplicationConstants.SCORE_FACTOR_POSITIVE;
		score-= numberOfFlips*ApplicationConstants.SCORE_FACTOR_NEGATIVE;
		if(score<0)
			score=0;
		
		Intent intent = new Intent(this, ResultActivity.class);
		intent.putExtra("flips", numberOfFlips);
		intent.putExtra("score", score);
		intent.putExtra("pairs", rightAnswers);
		intent.putExtra("bonus", bonus);
		startActivity(intent);
		finish();
	}

	public void restartGame(boolean levelUp) {
		if(levelUp)
		{
			bonus++;
			GeneralHelpers.playSound(this, R.raw.star);
			ImageView logo = (ImageView)findViewById(R.id.imageViewLogo);
			logo.setImageResource(R.drawable.level_up);
			
			Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.fade_in_out);
			logo.clearAnimation();
            logo.setAnimation(animation1);
	        animation1.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					ImageView logo = (ImageView)findViewById(R.id.imageViewLogo);
					logo.setImageResource(R.drawable.logo_small);
					Animation animation1 = AnimationUtils.loadAnimation(GamePlayActivity.this, R.anim.fade_in);
					logo.clearAnimation();
		            logo.setAnimation(animation1);
		            logo.startAnimation(animation1);
				}
			});
	        logo.startAnimation(animation1);
		}
		
		questions = getQuestions();
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.gameHolder);
		layout.removeAllViews();
		
		GridView grid = new GridView(this);//(GridView) findViewById(R.id.gridView1);
		grid.setHorizontalSpacing(5);
		grid.setVerticalSpacing(5);
		
		if(difficulty%3!=0)
			grid.setNumColumns(4);
		else
			grid.setNumColumns(3);
		
		layout.addView(grid);

		GameGridAdapter adapter = new GameGridAdapter(this,questions);
		grid.removeAllViewsInLayout();
		grid.setAdapter(adapter);
		
	}

	private String[] getQuestions() {
		String[] questions = new String[difficulty];
		for(int i=0;i<difficulty;i++)
		{
			int num1 = getRandomNumber();
			int num2 = getRandomNumber();
			
			if(num1<num2)
			{
				int temp = num1;
				num1=num2;
				num2=temp;
			}
			if(getIntent().getBooleanExtra("add", false))
			{
				questions[i]=num1+"+"+num2;
				questions[++i] = ""+(num1+num2);
			}else if(getIntent().getBooleanExtra("sub", false))
			{
				questions[i]=num1+"-"+num2;
				questions[++i] = ""+(num1-num2);
			}else
			{
				if(getRandomNumber()%2==0)
				{
					questions[i]=num1+"+"+num2;
					questions[++i] = ""+(num1+num2);
				}else
				{
					questions[i]=num1+"-"+num2;
					questions[++i] = ""+(num1-num2);
				}
			}
				
			
		}
		
		//Shuffle array
		long seed = 0xDEADBEEFL;
		Random rng = new Random(seed);
		List<String> arr = Arrays.asList(questions);
		Collections.shuffle(arr, rng);
		arr.toArray(questions);
		
		return questions;
	}
	
	private int getRandomNumber() {
		int limit=10;
//		if(difficulty>=12)
//		  limit=15;
//		if(difficulty>=16)
//			limit=20;
//		if(difficulty>=20)
//			limit=50;
		
		int random = (int)(Math.random()*10000000)%limit;
		return random;
	}
	
	private void showConfirmationDialog()
	{
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage("Are you sure you want to quit Game??")
	               .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   AdsHelper.showFullPageAds(GamePlayActivity.this);
	                       finish();
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(timer!=null)
			timer.cancel();
	}
	
	
	
}
