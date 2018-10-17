package cn.e3mall.search.activemq;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.search.service.impl.SearchItemServiceImpl;

public class ItemChangeListener implements MessageListener {

	
	@Autowired
	private SearchItemServiceImpl searchItemServiceImpl;
	
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		// 取消息内容
		try {

			TextMessage textMessage = null;
			
			Long itemId = null;
			
			if (message instanceof TextMessage) {
				
				textMessage = (TextMessage) message;
				
				itemId = Long.parseLong(textMessage.getText()); 
			}
			
			//向索引库中添加文档
			searchItemServiceImpl.addDocument(itemId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
