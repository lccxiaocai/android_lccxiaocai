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
	
	//��������δ����ʱview������
	private LinearLayout mViewContent;
	
	//�������û�����Ҳ��������view������������ɾ����ť
	private RelativeLayout mHolder;
	
	//���Ի��������ṩ���Ի���Ч��
	private Scroller mScroller;
	
	//�����ص��ӿڣ����������ϲ�֪ͨ�����¼�
	//ע���ⲿ�֣�ֵ��ѧϰ
	private OnSlideListener mOnSlideListener;
	
	//���������Ŀ�� ��λdp
	private int mHolderWidth = 120;
	
	//�ֱ��¼�ϴλ���������
	private int mLastX = 0;
	private int mLastY = 0;
	
	//�������ƻ����Ƕȣ������Ƕ�a���������������ܽ��л�����tan a = deltaX / deltaY > 2
	private static final int TAN = 2;
	
	//��������Ƿ����  �������Զ������ж�  �����޷�ͨ������򿪵��Լ��ر��Լ�
	private int flag = 0;
	public interface OnSlideListener{
		//SlideView������״̬���رգ���ʼ��������
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
		//��ʼ�����Ի�������
		mScroller = new Scroller(mContext);
		//���ñ���SlideView����Ϊ����
		setOrientation(LinearLayout.HORIZONTAL);
		//��slide_view_merge���ؽ���
		View.inflate(mContext, R.layout.slide_view_merge, this);
		mViewContent = (LinearLayout) findViewById(R.id.view_content_slide);
		//round��
		mHolderWidth = Math.round(TypedValue.applyDimension
				(TypedValue.COMPLEX_UNIT_DIP, mHolderWidth, getResources()
						.getDisplayMetrics()));
		
	}
	
	//���ð�ť������
	public void setButtonText(CharSequence text){
		((TextView) findViewById(R.id.delete_slide)).setText(text);
	}
	//��view���뵽ViewContent��
	public void setContentView(View view){
		mViewContent.addView(view);
	}
	//���û����ص�
	public void setOnSlideListener(OnSlideListener onSlideListener){
		mOnSlideListener = onSlideListener;
	}
	//����ǰ״̬����Ϊ�ر�
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
	//����MotionEvent�����л�������������������൱��onTouchEvent
	//����㲻��Ҫ����������ͻ������ֱ���������������ܹ���������
	public void onRequireTouchEvent(MotionEvent event){
		int x = (int) event.getX();
		int y = (int) event.getY();
		//getScrollX() ���ǵ�ǰview�����Ͻ������ĸ��ͼ�����Ͻǵ�X��ƫ����
		int scrollX = getScrollX();
		Log.d(TAG,"x=" + x + "y=" + y);
		
		switch (event.getAction()){
		case MotionEvent.ACTION_DOWN:{
			Log.e(TAG, "down");
			flag = 1;
			//�������� ʱ ACTION_DOWN��ֹͣ����
			if(!mScroller.isFinished()){
				mScroller.abortAnimation();
			}
			//�����û�йرյ�item  �ر���
			if(mOnSlideListener != null){
				mOnSlideListener.onSlide(this,
						OnSlideListener.SLIDE_STATUS_START_SCROLL);
			}
			break;
			}
		case MotionEvent.ACTION_MOVE:{
			//�ڻ��������л��ε����������  �ܶ��
			 flag = 0;
			 int deltaX = x - mLastX;  
             int deltaY = y - mLastY;  
             if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {  
                 // �����������������������򻬶�  
                 break;  
             }  
  
             // ���㻬���յ��Ƿ�Ϸ�����ֹ����Խ��  
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
	            // �����������жϣ����ɿ��ֵ�ʱ�򣬻��Զ������߻������������ı߻���Ҫ����ǰ������λ��  
	            if (scrollX - mHolderWidth * 0.75 > 0) {  
	                newScrollX = mHolderWidth;  
	            }  
	            // ���������յ�  
	            this.smoothScrollTo(newScrollX, 0);  
	            // ֪ͨ�ϲ㻬���¼�  
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
	        // ����������ָ��λ��  
	        int scrollX = getScrollX();  
	        int delta = destX - scrollX;  
	        // ������ʱ������destX��Ч��������������  
	        mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);  
	        //invalidate()������ˢ��View�ģ���������UI�߳��н��й������������޸�ĳ��view����ʾʱ��
	        //����invalidate()���ܿ������»��ƵĽ��档invalidate()�ĵ����ǰ�֮ǰ�ľɵ�view����UI�̶߳�����pop���� 
	        //ϵͳ���Զ����� View��onDraw()����
	        invalidate();  
	    }  
	  	
	 		// ����Ҫ֪���µ�λ��ʱ�����ô˺������������true����ʾ������û�н�����λ�øı����ṩһ���µ�λ�á�
	 		//���������û�и��̫���
	    @Override  
	    public void computeScroll() {  
	        if (mScroller.computeScrollOffset()) {  
	            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  
	            postInvalidate();  
	        }  
	    }  
}
