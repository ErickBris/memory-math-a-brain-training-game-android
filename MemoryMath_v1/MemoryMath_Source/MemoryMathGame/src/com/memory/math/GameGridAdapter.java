package com.memory.math;


import java.util.regex.Pattern;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.t4t.advertisments.ApplicationConstants;
import com.t4t.advertisments.GeneralHelpers;

public class GameGridAdapter extends BaseAdapter {
	
	private GamePlayActivity context;
	private String[] questions;
	private View open1;
	private int correct=1;
	
	
	public GameGridAdapter(GamePlayActivity context, String[] questions2){
		this.context = context;
		this.questions=questions2;
	}

	@Override
	public int getCount() {
		return questions.length;
	}

	@Override
	public Object getItem(int position) {		
		return questions[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, final View convertView1, ViewGroup parent) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            final View convertView = mInflater.inflate(R.layout.adapter_item_home, null);
            
        
        final TextView titleTextView = (TextView) convertView.findViewById(R.id.textViewAdapterTitle);
        titleTextView.setText("");
        
        convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GeneralHelpers.playSound(context, R.raw.click);
				context.numberOfFlips++;
				
				if(titleTextView.getText().toString().equals(""))
				{
					
					titleTextView.setText(""+questions[position]);
					convertView.setBackgroundResource(R.color.BLUE);
					
					Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.flip);
					convertView.clearAnimation();
		            convertView.setAnimation(animation1);
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
							CheckForCorrectAnswer(convertView,titleTextView);
						}
					});
			        convertView.startAnimation(animation1);
				}
				else
				{
					titleTextView.setText("");
					convertView.setBackgroundResource(R.color.RED);
					open1=null;
				}
				
			}

		});
        
        if(questions[position].equals("HINT"))
        {
        	titleTextView.setText(""+questions[position]);
        	convertView.setBackgroundResource(R.color.PUMPKIN);
        	convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "Show Hint", Toast.LENGTH_LONG).show();
					
				}
			});
        }
       
        return convertView;
	}
	
	private void CheckForCorrectAnswer(View convertView, TextView titleTextView) {
		
		if(open1==null)
			open1=convertView;
		else
		{
			TextView titleTextView1 = (TextView) open1.findViewById(R.id.textViewAdapterTitle);
			String text1 = titleTextView1.getText().toString();
			String text2 = titleTextView.getText().toString();

			if((text1.length()>2 && text2.length()>2) || (text1.length()<=2 && text2.length()<=2))
			{
				//Close prev
				titleTextView1.setText("");
				open1.setBackgroundResource(R.color.RED);
				
				//Close latest
				titleTextView.setText("");
				convertView.setBackgroundResource(R.color.RED);
				
				open1=null;
			}else
			{
				String big;
				String small;
				if(text1.length()>text2.length())
				{
					big = text1;
					small = text2;
				}
				else
				{
					big = text2;
					small = text1;
				}
				
				int answer = Integer.parseInt(small);
				String[] ques;
				int ans;
				if(big.contains("+"))
				{
					ques = big.split(Pattern.quote("+"));
					ans = Integer.parseInt(ques[0]) + Integer.parseInt(ques[1]);
				}
				else
				{
					ques = big.split(Pattern.quote("-"));
					ans = Integer.parseInt(ques[0]) - Integer.parseInt(ques[1]);
				}
				
				if(ans!=answer)
				{
					//Close prev
					titleTextView1.setText("");
					open1.setBackgroundResource(R.color.RED);
					
					//Close latest
					titleTextView.setText("");
					convertView.setBackgroundResource(R.color.RED);
					
					open1=null;
				}else
				{
					//Give bonus time on right answer
					context.progress+=ApplicationConstants.BONUS_TIME;
					
					context.rightAnswers++;
					
					correct+=2;
					
					open1.setClickable(false);
					convertView.setClickable(false);
					open1.setBackgroundResource(R.color.GREEN);
					convertView.setBackgroundResource(R.color.GREEN);
					if(correct>=questions.length)
					{
						correct=1;
						
						//Give extra bonus time on level clear
						context.progress+=ApplicationConstants.BONUS_TIME*2;
						
						if(context.difficulty<20)
							context.difficulty+=4;
					
						context.restartGame(true);
					}
					
					open1=null;
				}
						
			}
		}
		
	}

}
