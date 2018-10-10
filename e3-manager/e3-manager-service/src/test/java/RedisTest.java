import org.csource.fastdfs.test.Test1;
import org.junit.Test;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.redis.JedisClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisTest {

	@Test
	public void Test1() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-redis.xml");
		JedisClient client = context.getBean(JedisClient.class);
		client.set("zheng", "1");
		String string = client.get("zheng");
		System.out.println(string);
	}
	
	/*@Test
	public void test2() throws Exception{
		
		JedisPool pool = new JedisPool("192.168.25.130",6379);
		Jedis jedis = pool.getResource();
		jedis.set("zheng","peng");
		String string = jedis.get("zheng");
		System.out.println(string);
		jedis.close();
		pool.close();
		
	}*/

}
