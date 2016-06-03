package com.xiaocai_.romaunt_1;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaocai_.romaunt_1.SlideView.OnSlideListener;
import com.xiaocai_.romaunt_1.model.MessageItem;
import com.xiaocai_.romaunt_1.model.MessageItemList;

public class SlideAdapter extends BaseAdapter implements OnSlideListener{
	
	private static final String TAG = "SlideAdapter";
	private LayoutInflater mInflater;
	private MessageItemList list;
	private SlideView mLastSlideViewWithStatusOn;
	private Context context;
	
	SlideAdapter(Context context) {
		super();
		this.context = context;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		list = MessageItemList.getList();
	}
	
	public SlideView getmLastSlideViewWithStatusOn() {
		return mLastSlideViewWithStatusOn;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		SlideView slideView = (SlideView) convertView;
		
		/******�·���******/
		/*
		 * ��ͨ��adapter����viewʱ��convertView������һֱ��һ����Ҳ����˵�����if�������ж��
		 * ִ�еĴ���Ӧ�þ��ǵ�һ����ʾListViewʱ����Ļ����ʾ��item�ĸ���
		 * Ҳ����˵  ���߸�item��convertView ���ǵ�һ��item��convertView
		 */
		
		/*
		 * ����notifyDataSetChanged()����  �����µ���getView����
		 * ��������ListViewʱ��һ�������ʹ�õ� concertView������ǰ��
		 * Ҳ����˵ ��ε��� listView�������if���
		 */
		
		if(slideView == null){
			//Log.e(TAG,"��������");
			View itemView = mInflater.inflate(R.layout.listitem_story, null);
			slideView = new SlideView(context);
			slideView.setContentView(itemView);
			
			holder = new ViewHolder(slideView);
			slideView.setOnSlideListener(this);
			slideView.setTag(holder);
		} else {
			holder = (ViewHolder) slideView.getTag();
		}
		
		MessageItem item = list.get(position);
		item.slideView = slideView;
		//����notifyDataSetChanged()�����»���ListViewʱ ʹ�õĻ����ϴε�convertView
		//Ҳ����˵convertView��״̬����ı�  �����״̬ 
		//��ʱ����Ҫ�ֶ�����ȥ  Ҳ������һ�д���
		//ע��shrink()������smoothScrollTo() ������Ỻ���Ļ���ȥ  Ч������ɾ����item2
		//�ػ�ListView  ԭ����ʾitem2��Ϣ��convertView ��ʾitem3����Ϣ
		//���һ������� �� ���ر�
		//����Ӧ��ֱ�ӵ���  scrollTo()
		item.slideView.quickShrink();
		
		holder.tv_msg.setText(item.msg);
		//Ϊʲô������� �߳����⣿ Ϊʲô������getView�ж�tv_delete������
		holder.tv_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Log.e(TAG,"position"+position);
				list.delete(position);
				notifyDataSetChanged();
			}
		});
		return slideView;
	}

	@Override
	public void onSlide(View view, int status) {
		Log.e(TAG, "˭�ȣ�");
		if (mLastSlideViewWithStatusOn != null) {
			mLastSlideViewWithStatusOn.smoothShrink();
			/*ÿ�η�����Ҫ�½�һ�����󣬷������һ�´���
             *ERROR/AndroidRuntime(11761): java.lang.IllegalStateException: TimerTask is scheduled already
      		 *����ͬһ����ʱ������ֻ�ܱ�����һ��
      		 *���ڴ�ÿ����������һ��task
      		 *Timer�����ó�Ա����   �ڴ˴�û�ö���*/
			Timer timer = new Timer();
			TimerTask task = new TimerTask(){
				public void run(){
				mLastSlideViewWithStatusOn = null;
				}
			};
			timer.schedule(task, 150);
		}
		
		if(status == SLIDE_STATUS_ON) {
			mLastSlideViewWithStatusOn = (SlideView) view;
		}
		/*����������   ���绬��item4  ��ͨ�������Ļ���߻���item��û�л��� ʹ��item4�Զ��ر�
		 * ��ʱ item4 �޷�����  ����item���Ի��� ������item������ item4�ֿ��Ի���
		 * ����취���� */
	}
	
	 private static class ViewHolder {
	       public TextView tv_msg;
	       public TextView tv_delete;

	        ViewHolder(View view) {
	         tv_msg = (TextView)view.findViewById(R.id.tv_listitem);
	         tv_delete = (TextView)view.findViewById(R.id.delete_slide);
	        }
	    }

}
