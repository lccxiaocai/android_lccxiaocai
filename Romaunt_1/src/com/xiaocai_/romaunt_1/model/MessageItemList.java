package com.xiaocai_.romaunt_1.model;

import java.util.ArrayList;

public class MessageItemList extends ArrayList<MessageItem> {
	
	private static final String TAG = "MessageItemList";
	
	private static final long serialVersionUID = 1L;
	
	public static MessageItemList messageItemList = null;
	
	private MessageItemList(){
		
	}
	
	public static MessageItemList getList(){
		if(messageItemList == null){
			//ע��  ��Ҫ���������� ʵ���� messageItemList
			messageItemList = new MessageItemList();
			for (int i = 0; i < 10 ; i++) 
			{
				MessageItem item = new MessageItem();
				item.msg = "��������!!"+i;
				messageItemList.add(item);
			}
		}
		
		return messageItemList;
	}
	
	public boolean delete(int index){
		messageItemList.remove(index);
		return true;
	}
	
}
