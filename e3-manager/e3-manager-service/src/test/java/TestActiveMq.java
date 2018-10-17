import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestActiveMq {

	@Test
	public void testSpringActiveMq(){
		
		
		ApplicationContext context = 
				new ClassPathXmlApplicationContext("classpath:"
						+ "spring/applicationContext-activemq.xml");
		
		//从spring容器中jmsTemplate对象
		JmsTemplate template = context.getBean(JmsTemplate.class);
		
		//从spring容器中获取desination对象
		
		 Destination destination =  (Destination) context.getBean("queueDestination");
		
		//使用JmsTemplate对象发送消息。
		template.send(destination,new MessageCreator() {
			//创建消息并返回
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				
				TextMessage textMessage = session.createTextMessage("spring activemq queue message");
				
				return textMessage;
			}
		});
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
