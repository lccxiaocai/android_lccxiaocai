package com.xiaocai_.romaunt_1;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener{
	
	private ImageView story;
	private ImageView find;
	private ImageView message;
	private ImageView my;
	private StoryFragment storyFragment;
	private FindFragment findFragment;
	private MessageFragment messageFragment;
	private MyFragment myFragment;
	private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();
        init();
        setTabselection(0);
    }
    
    @Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.story:
			setTabselection(0);
			break;
		case R.id.find:
			setTabselection(1);
			break;
		case R.id.message:
			setTabselection(2);
			break;
		case R.id.my:
			setTabselection(3);
			break;
		}
	}
	
	private void init(){
		story = (ImageView) findViewById(R.id.story);
		find = (ImageView) findViewById(R.id.find);
		message = (ImageView) findViewById(R.id.message);
		my = (ImageView) findViewById(R.id.my);
		
		story.setOnClickListener(this);
		find.setOnClickListener(this);
		message.setOnClickListener(this);
		my.setOnClickListener(this);
	}
	
	private void clearSelection(){
		story.setImageResource(R.drawable.img_story);
		find.setImageResource(R.drawable.img_findings);
		message.setImageResource(R.drawable.img_message);
		my.setImageResource(R.drawable.img_my);
	}
	
	private void hideFragments(FragmentTransaction transaction){
		if(storyFragment != null)
		{
			transaction.hide(storyFragment);
		}
		if(findFragment != null)
		{
			transaction.hide(findFragment);
		}
		if(messageFragment != null)
		{
			transaction.hide(messageFragment);
		}
		if(myFragment != null)
		{
			transaction.hide(myFragment);
		}
	}
	
	private void setTabselection(int index){
		//每次选中前先清除掉上次选中的状态
		clearSelection();
		//开启一个Fragment事物
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		//先隐藏掉所有的fragment,以防有多个fragment显示在界面上
		hideFragments(transaction);
		
		switch(index){
		case 0:
			story.setImageResource(R.drawable.img_story_choose);
			if(storyFragment == null){
				storyFragment = new StoryFragment();	
				transaction.add(R.id.framelayout, storyFragment);
			}
			else{
				transaction.show(storyFragment);
			}
			break;
		
	    case 1:
	    	find.setImageResource(R.drawable.img_findings_choose);
	    	if(findFragment == null){
	    		findFragment = new FindFragment();	
	    		transaction.add(R.id.framelayout, findFragment);
	    	}
		else{
			transaction.show(findFragment);
		}
		break;
	    
	    case 2:
			message.setImageResource(R.drawable.img_message_choose);
			if(messageFragment == null){
				messageFragment = new MessageFragment();	
				transaction.add(R.id.framelayout, messageFragment);
			}
			else{
				transaction.show(messageFragment);
			}
			break;
			
	    case 3:
			my.setImageResource(R.drawable.img_my_choose);
			if(myFragment == null){
				myFragment = new MyFragment();	
				transaction.add(R.id.framelayout, myFragment);
			}
			else{
				transaction.show(myFragment);
			}
			break;
	   }	
		
		transaction.commit();
  }
	
}
