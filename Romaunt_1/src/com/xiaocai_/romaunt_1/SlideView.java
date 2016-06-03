package com.xiaocai_.romaunt_1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class SlideView extends LinearLayout {
	
	private static  final String TAG = "SlideView";
	
	private Context mContext;
	
	//用来放置未滑动时view的容器
	private LinearLayout mViewContent;
	
	//用来放置滑动后也就是内置view的容器，比如删除按钮
	private RelativeLayout mHolder;
	
	//弹性滑动对象，提供弹性滑动效果
	private Scroller mScroller;
	
	//滑动回调接口，用了来向上层通知滑动事件
	//注意这部分，值得学习
	private OnSlideListener mOnSlideListener;
	
	//内置容器的宽度 单位dp
	private int mHolderWidth = 120;
	
	//分别记录上次滑动的坐标
	private int mLastX = 0;
	private int mLastY = 0;
	
	//用来控制滑动角度，仅当角度a满足如下条件才能进行滑动：tan a = deltaX / deltaY > 2
	private static final int TAN = 2;
	
	//用来标记是否进行  往两边自动化的判断  否则无法通过点击打开的自己关闭自己
	private int flag = 0;
	public interface OnSlideListener{
		//SlideView的三种状态：关闭，开始滑动，打开
		public static final int SLIDE_STATUS_OFF = 0;
		public static final int SLIDE_STATUS_START_SCROLL = 1;
		public static final int SLIDE_STATUS_ON = 2;
		
		public void onSlide(View view, int status);
	}
	
	public SlideView(Context context) {
		super(context);
		initView();
	}
	
	public SlideView(Context context, AttributeSet attrs){
		super(context, attrs);
		initView();
	}
	
	private void initView(){
		mContext = getContext();
		//初始化弹性滑动对象
		mScroller = new Scroller(mContext);
		//设置本类SlideView方向为横向
		setOrientation(LinearLayout.HORIZONTAL);
		//将slide_view_merge加载进来
		View.inflate(mContext, R.layout.slide_view_merge, this);
		mViewContent = (LinearLayout) findViewById(R.id.view_content_slide);
		//round是
		mHolderWidth = Math.round(TypedValue.applyDimension
				(TypedValue.COMPLEX_UNIT_DIP, mHolderWidth, getResources()
						.getDisplayMetrics()));
		
	}
	
	//设置按钮的内容
	public void setButtonText(CharSequence text){
		((TextView) findViewById(R.id.delete_slide)).setText(text);
	}
	//将view加入到ViewContent中
	public void setContentView(View view){
		mViewContent.addView(view);
	}
	//设置滑动回掉
	public void setOnSlideListener(OnSlideListener onSlideListener){
		mOnSlideListener = onSlideListener;
	}
	//将当前状态设置为关闭
	public void quickShrink(){
		if(getScrollX() != 0){
			this.scrollTo(0,0);
		}
	}
	
	public void smoothShrink(){
		if(getScrollX() != 0){
			this.smoothScrollTo(0, 0);
		}
	}
	//根据MotionEvent来进行滑动，这个方法的作用相当于onTouchEvent
	//如果你不需要来处理滑动冲突，可以直接重命名，照样能够正常工作
	public void onRequireTouchEvent(MotionEvent event){
		int x = (int) event.getX();
		int y = (int) event.getY();
		//getScrollX() 就是当前view的左上角相对于母视图的左上角的X轴偏移量
		int scrollX = getScrollX();
		Log.d(TAG,"x=" + x + "y=" + y);
		
		switch (event.getAction()){
		case MotionEvent.ACTION_DOWN:{
			Log.e(TAG, "down");
			flag = 1;
			//在慢慢滑 时 ACTION_DOWN会停止滑动
			if(!mScroller.isFinished()){
				mScroller.abortAnimation();
			}
			//如果有没有关闭的item  关闭它
			if(mOnSlideListener != null){
				mOnSlideListener.onSlide(this,
						OnSlideListener.SLIDE_STATUS_START_SCROLL);
			}
			break;
			}
		case MotionEvent.ACTION_MOVE:{
			//在滑动过程中会多次调用这个方法  很多次
			 flag = 0;
			 int deltaX = x - mLastX;  
             int deltaY = y - mLastY;  
             if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {  
                 // 滑动不满足条件，不做横向滑动  
                 break;  
             }  
  
             // 计算滑动终点是否合法，防止滑动越界  
             int newScrollX = scrollX - deltaX;  
             if (deltaX != 0) {  
                 if (newScrollX < 0) {  
                     newScrollX = 0;  
                 } else if (newScrollX > mHolderWidth) {  
                      newScrollX = mHolderWidth;  
                 }  
                 this.scrollTo(newScrollX, 0);  
             }  
             break;  
		}
		case MotionEvent.ACTION_UP:{
			Log.e(TAG, "up");
			if(0 == flag){
				int newScrollX = 0;  
	            // 这里做了下判断，当松开手的时候，会自动向两边滑动，具体向哪边滑，要看当前所处的位置  
	            if (scrollX - mHolderWidth * 0.75 > 0) {  
	                newScrollX = mHolderWidth;  
	            }  
	            // 慢慢滑向终点  
	            this.smoothScrollTo(newScrollX, 0);  
	            // 通知上层滑动事件  
	            if (mOnSlideListener != null) {  
	                mOnSlideListener.onSlide(this,  
	                        newScrollX == 0 ? OnSlideListener.SLIDE_STATUS_OFF  
	                                : OnSlideListener.SLIDE_STATUS_ON);  
	            }  
			}
            break;  
		}
		default:  
            break;  
		}
		mLastX = x;  
        mLastY = y;  
	}
	
	 private void smoothScrollTo(int destX, int destY) {  
	        // 缓慢滚动到指定位置  
	        int scrollX = getScrollX();  
	        int delta = destX - scrollX;  
	        // 以三倍时长滑向destX，效果就是慢慢滑动  
	        mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);  
	        //invalidate()是用来刷新View的，必须是在UI线程中进行工作。比如在修改某个view的显示时，
	        //调用invalidate()才能看到重新绘制的界面。invalidate()的调用是把之前的旧的view从主UI线程队列中pop掉。 
	        //系统会自动调用 View的onDraw()方法
	        invalidate();  
	    }  
	  	
	 		// 当想要知道新的位置时，调用此函数。如果返回true，表示动画还没有结束。位置改变以提供一个新的位置。
	 		//这个方法并没有搞得太清楚
	    @Override  
	    public void computeScroll() {  
	        if (mScroller.computeScrollOffset()) {  
	            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  
	            postInvalidate();  
	        }  
	    }  
}
